package mddev0.mafiacraft.abilities;

import mddev0.mafiacraft.MafiaCraft;
import mddev0.mafiacraft.player.MafiaPlayer;
import mddev0.mafiacraft.player.StatusData;
import org.bukkit.Bukkit;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Map;
import java.util.UUID;

public final class Inquisition extends BukkitRunnable {

    private final MafiaCraft plugin;

    public Inquisition(MafiaCraft plugin) {
        this.plugin = plugin;
    }

    @Override
    public void run() {
        if (!plugin.getActive()) return; // DO NOTHING IF NOT ACTIVE!
        for (Map.Entry<UUID, MafiaPlayer> seer : plugin.getLivingPlayers().entrySet()) {
            if (seer.getValue().getRole().getAbilities().contains(Ability.INQUISITION)) {
                Player s = Bukkit.getPlayer(seer.getKey());
                assert s != null;
                for (Map.Entry<UUID, MafiaPlayer> target : plugin.getLivingPlayers().entrySet()) {
                    if (target.getValue().getStatus().hasStatus(StatusData.Status.UNHOLY)) {
                        Player t = Bukkit.getPlayer(target.getKey());
                        if (t != null) {
                            s.spawnParticle(Particle.TOWN_AURA, t.getLocation().add(0, 1, 0), 15, 0.7, 0.7, 0.7, 0.0);
                            s.spawnParticle(Particle.SCULK_SOUL, t.getLocation().add(0, 1, 0), 2, 1.5, 1.5, 1.5, 0.0);
                        }
                    }
                }
            }
        }
    }
}
