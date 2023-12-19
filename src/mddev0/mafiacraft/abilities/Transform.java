package mddev0.mafiacraft.abilities;

import mddev0.mafiacraft.MafiaCraft;
import mddev0.mafiacraft.player.RoleData;
import mddev0.mafiacraft.player.StatusData;
import mddev0.mafiacraft.player.MafiaPlayer;
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
        long dayTime = plugin.getServer().getWorlds().get(0).getTime();
        if (dayTime >= 13000 && dayTime < 23000) {
            long fullTime = plugin.getServer().getWorlds().get(0).getFullTime();
            int phase = (int) (fullTime/24000)%8;
            if (phase == 0) { // Full Moon
                for (MafiaPlayer play : plugin.getLivingPlayers().values()) {
                    if (play.getRole().getAbilities().contains(Ability.TRANSFORM)) {
                        // player can transform
                        if (!(Boolean)play.getRoleData().getData(RoleData.DataType.WEREWOLF_TRANSFORM) && Bukkit.getPlayer(play.getID()) != null) {
                            Objects.requireNonNull(Bukkit.getPlayer(play.getID())).sendMessage(ChatColor.DARK_RED + "You feel hungry. It is a full moon!");
                            play.getRoleData().setData(RoleData.DataType.WEREWOLF_TRANSFORM, true);
                            play.getStatus().startStatus(StatusData.Status.UNHOLY, 48000L); // Two days of unholy
                        }
                    }
                }
                return; // after completing full moon tasks
            }
        }
        for (MafiaPlayer play : plugin.getLivingPlayers().values()) {
            if (play.getRole().getAbilities().contains(Ability.TRANSFORM)) {
                // player can transform
                if ((Boolean)play.getRoleData().getData(RoleData.DataType.WEREWOLF_TRANSFORM)) {
                    Objects.requireNonNull(Bukkit.getPlayer(play.getID())).sendMessage(ChatColor.DARK_GRAY + "You feel normal again.");
                    play.getRoleData().setData(RoleData.DataType.WEREWOLF_TRANSFORM, false);
                    Objects.requireNonNull(Bukkit.getPlayer(play.getID())).removePotionEffect(PotionEffectType.INCREASE_DAMAGE);
                    // see Rampage.java
                }
            }
        }
    }
}
