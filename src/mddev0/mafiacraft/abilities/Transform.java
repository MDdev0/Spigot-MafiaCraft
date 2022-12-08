package mddev0.mafiacraft.abilities;

import mddev0.mafiacraft.MafiaCraft;
import mddev0.mafiacraft.roles.Werewolf;
import mddev0.mafiacraft.util.MafiaPlayer;
import org.bukkit.ChatColor;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Objects;

public final class Transform extends BukkitRunnable {

    private final MafiaCraft plugin;

    public Transform(MafiaCraft plugin) {
        this.plugin = plugin;
    }

    @Override
    public void run() {
        // Do nothing if it is not night
        long dayTime = plugin.getServer().getWorlds().get(0).getTime();
        if (dayTime >= 19000 && dayTime < 23000) {
            long fullTime = plugin.getServer().getWorlds().get(0).getFullTime();
            int phase = (int) (fullTime/24000)%8;
            if (phase == 0) { // Full Moon
                for (MafiaPlayer play : plugin.getLivingPlayers().values()) {
                    if (play.getRole().hasAbility(Ability.TRANSFORM)) {
                        // player can transform
                        if (!((Werewolf) play.getRole()).getTransformed()) {
                            Objects.requireNonNull(plugin.getServer().getPlayer(play.getID())).sendMessage(ChatColor.DARK_RED + "You feel hungry. It is a full moon!");
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
                    Objects.requireNonNull(plugin.getServer().getPlayer(play.getID())).sendMessage(ChatColor.DARK_GRAY + "You feel normal again.");
                    ((Werewolf) play.getRole()).setTransformed(false);
                    play.setUnholy();
                }
            }
        }
    }
}
