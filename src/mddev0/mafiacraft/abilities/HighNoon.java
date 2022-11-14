package mddev0.mafiacraft.abilities;

import mddev0.mafiacraft.MafiaCraft;
import mddev0.mafiacraft.util.MafiaPlayer;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Map;
import java.util.UUID;

public final class HighNoon extends BukkitRunnable {

    private final MafiaCraft plugin;

    public HighNoon(MafiaCraft plugin) {
        this.plugin = plugin;
    }

    @Override
    public void run() {
        // Do nothing if it is not noon
        if (plugin.getServer().getWorlds().get(0).getTime() == 6000L) {
            // IT'S HIGH NOON!
            Map<UUID, MafiaPlayer> living = plugin.getLivingPlayers();
            for (Map.Entry<UUID, MafiaPlayer> p : living.entrySet()) {
                if (p.getValue().getRole().hasAbility(Ability.HIGH_NOON)) {
                    Player affected = plugin.getServer().getPlayer(p.getKey());
                    if (affected != null && affected.isOnline()) {
                        affected.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 12000, 1, false, false, true));
                    }
                }
            }
        }
    }
}
