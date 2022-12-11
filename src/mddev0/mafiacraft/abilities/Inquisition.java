package mddev0.mafiacraft.abilities;

import mddev0.mafiacraft.MafiaCraft;
import mddev0.mafiacraft.util.MafiaPlayer;
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
        for (Map.Entry<UUID, MafiaPlayer> seer : plugin.getPlayerList().entrySet()) {
            if (seer.getValue().getRole().hasAbility(Ability.INQUISITION)) {
                Player s = plugin.getServer().getPlayer(seer.getKey());
                assert s != null;
                for (Map.Entry<UUID, MafiaPlayer> target : plugin.getLivingPlayers().entrySet()) {
                    if (target.getValue().isUnholySuspect()) {
                        Player t = plugin.getServer().getPlayer(target.getKey());
                        assert t != null;
                        s.spawnParticle(Particle.TOWN_AURA, t.getLocation().add(0,1,0), 20, 1, 1, 1);
                        s.spawnParticle(Particle.SCULK_SOUL, t.getLocation().add(0,1,0), 2, 2, 2, 2);
                    }
                }
            }
        }
    }
}
