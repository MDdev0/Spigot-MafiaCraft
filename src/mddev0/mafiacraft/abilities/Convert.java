package mddev0.mafiacraft.abilities;

import mddev0.mafiacraft.MafiaCraft;
import mddev0.mafiacraft.roles.Vampire;
import mddev0.mafiacraft.util.MafiaPlayer;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public final class Convert implements Listener {

    private final MafiaCraft plugin;

    public Convert(MafiaCraft plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerKill(PlayerDeathEvent death) {
        if (death.getEntity().getKiller() != null) {
            // if the player doing the killing
            MafiaPlayer killer = plugin.getLivingPlayers().get(death.getEntity().getKiller().getUniqueId());
            if (killer.getRole().hasAbility(Ability.CONVERT)) {
                long dayTime = plugin.getServer().getWorlds().get(0).getTime();
                long fullTime = plugin.getServer().getWorlds().get(0).getFullTime();
                int phase = (int) (fullTime/24000)%8;
                if (dayTime < 19000 || dayTime >= 23000 || phase != 4) { // NOT New Moon Night
                    MafiaPlayer killed = plugin.getPlayerList().get(death.getEntity().getUniqueId());
                    killed.makeAlive();
                    killed.changeRole(new Vampire());
                    death.getEntity().sendMessage(ChatColor.DARK_PURPLE + "You are now a " + ChatColor.DARK_GRAY + "Vampire" + ChatColor.DARK_PURPLE + ".");
                    killer.setUnholy();
                }
            }
        }
    }
}
