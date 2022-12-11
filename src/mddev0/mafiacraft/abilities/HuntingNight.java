package mddev0.mafiacraft.abilities;

import mddev0.mafiacraft.MafiaCraft;
import mddev0.mafiacraft.util.MafiaPlayer;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public final class HuntingNight implements Listener {

    private final MafiaCraft plugin;

    public HuntingNight(MafiaCraft plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerKill(PlayerDeathEvent death) {
        if (death.getEntity().getKiller() != null) {
            // if the player doing the killing
            MafiaPlayer killer = plugin.getLivingPlayers().get(death.getEntity().getKiller().getUniqueId());
            if (killer.getRole().hasAbility(Ability.HUNTING_NIGHT)) {
                long dayTime = plugin.getServer().getWorlds().get(0).getTime();
                long fullTime = plugin.getServer().getWorlds().get(0).getFullTime();
                int phase = (int) (fullTime/24000)%8;
                if (dayTime >= 19000 && dayTime < 23000 && phase == 4) { // New Moon Night
                    // player already marked as dead, make them stay that way
                    death.getEntity().getKiller().sendMessage(ChatColor.DARK_RED + "The new moon is out tonight. The player you killed will not respawn.");
                    killer.setUnholy();
                }
            }
        }
    }
}
