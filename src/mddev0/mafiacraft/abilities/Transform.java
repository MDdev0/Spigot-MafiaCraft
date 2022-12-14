package mddev0.mafiacraft.abilities;

import mddev0.mafiacraft.MafiaCraft;
import mddev0.mafiacraft.roles.Werewolf;
import mddev0.mafiacraft.util.MafiaPlayer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Objects;

public final class Transform extends BukkitRunnable {

    private final MafiaCraft plugin;

    public Transform(MafiaCraft plugin) {
        this.plugin = plugin;
    }

    @Override
    public void run() {
        if (!plugin.getActive()) return; // DO NOTHING IF NOT ACTIVE!
        // Do nothing if it is not night
        Bukkit.broadcastMessage(ChatColor.DARK_GRAY + "TEST 0"); // TODO: REMOVE ME
        long dayTime = plugin.getServer().getWorlds().get(0).getTime();
        if (dayTime >= 19000 && dayTime < 23000) {
            Bukkit.broadcastMessage(ChatColor.DARK_GRAY + "TEST 1"); // TODO: REMOVE ME
            long fullTime = plugin.getServer().getWorlds().get(0).getFullTime();
            int phase = (int) (fullTime/24000)%8;
            Bukkit.broadcastMessage(ChatColor.DARK_GRAY + "TEST 2: " + phase); // TODO: REMOVE ME
            if (phase == 0) { // Full Moon
                for (MafiaPlayer play : plugin.getLivingPlayers().values()) {
                    Bukkit.broadcastMessage(ChatColor.DARK_GRAY + "TEST 3"); // TODO: REMOVE ME
                    if (play.getRole().hasAbility(Ability.TRANSFORM)) {
                        Bukkit.broadcastMessage(ChatColor.DARK_GRAY + "TEST 4"); // TODO: REMOVE ME
                        // player can transform
                        if (!((Werewolf) play.getRole()).getTransformed()) {
                            Bukkit.broadcastMessage(ChatColor.DARK_GRAY + "TEST 5"); // TODO: REMOVE ME
                            Bukkit.getPlayer(play.getID()).sendMessage(ChatColor.DARK_RED + "You feel hungry. It is a full moon!");
                            ((Werewolf) play.getRole()).setTransformed(true);
                            play.setUnholy();
                        }
                    }
                }
                return; // after completing full moon tasks
            }
        }
        for (MafiaPlayer play : plugin.getLivingPlayers().values()) {
            if (play.getRole().hasAbility(Ability.TRANSFORM)) {
                // player can transform
                if (((Werewolf) play.getRole()).getTransformed()) {
                    Objects.requireNonNull(Bukkit.getPlayer(play.getID())).sendMessage(ChatColor.DARK_GRAY + "You feel normal again.");
                    ((Werewolf) play.getRole()).setTransformed(false);
                    Objects.requireNonNull(Bukkit.getPlayer(play.getID())).removePotionEffect(PotionEffectType.INCREASE_DAMAGE);
                    // see Rampage.java
                }
            }
        }
    }
}
