package mddev0.mafiacraft.abilities;

import mddev0.mafiacraft.MafiaCraft;
import mddev0.mafiacraft.util.MafiaPlayer;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Map;
import java.util.UUID;

public final class NightOwl extends BukkitRunnable {

    private final MafiaCraft plugin;

    public NightOwl(MafiaCraft plugin) {
        this.plugin = plugin;
    }

    @Override
    public void run() {
        if (!plugin.getActive()) return; // DO NOTHING IF NOT ACTIVE!
        long worldTime = plugin.getServer().getWorlds().get(0).getTime();
        if (plugin.getServer().getWorlds().get(0).getTime() >= 0 || worldTime <= 12000) { //At 6:00 AM through 6:00 PM
            for (Map.Entry<UUID, MafiaPlayer> p : plugin.getLivingPlayers().entrySet()) {
                if (p.getValue().getRole().hasAbility(Ability.NIGHT_OWL)) {
                    long duskDifference = 12000 - worldTime; //Difference between current time and Dusk
                    Player affected = plugin.getServer().getPlayer(p.getKey());
                    if (affected != null && affected.isOnline()) {
                        affected.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, (int) duskDifference, 1, false, false, true)); //Apply until Dusk
                    }
                }
            }
        }
    }
}
