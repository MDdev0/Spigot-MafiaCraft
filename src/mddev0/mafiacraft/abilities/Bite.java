package mddev0.mafiacraft.abilities;

import mddev0.mafiacraft.MafiaCraft;
import mddev0.mafiacraft.roles.Werewolf;
import mddev0.mafiacraft.util.MafiaPlayer;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public final class Bite implements Listener {

    private final MafiaCraft plugin;

    public Bite(MafiaCraft plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerKill(PlayerDeathEvent death) {
        if (death.getEntity().getKiller() != null) {
            // if the player doing the killing
            MafiaPlayer killer = plugin.getLivingPlayers().get(death.getEntity().getKiller().getUniqueId());
            if (killer.getRole().hasAbility(Ability.BITE) && killer.getRole() instanceof Werewolf) {
                if (((Werewolf) killer.getRole()).getTransformed()) {
                    MafiaPlayer killed = plugin.getPlayerList().get(death.getEntity().getUniqueId());
                    killed.makeAlive();
                    killed.changeRole(new Werewolf());
                    death.getEntity().sendMessage(ChatColor.DARK_PURPLE + "You are now a " + ChatColor.DARK_AQUA + "Werewolf" + ChatColor.DARK_PURPLE + ".");
                }
            }
        }
    }
}
