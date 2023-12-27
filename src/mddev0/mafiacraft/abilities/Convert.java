package mddev0.mafiacraft.abilities;

import mddev0.mafiacraft.MafiaCraft;
import mddev0.mafiacraft.player.Role;
import mddev0.mafiacraft.player.StatusData;
import mddev0.mafiacraft.player.MafiaPlayer;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public final class Convert implements Listener {

    private final MafiaCraft plugin;

    public Convert(MafiaCraft plugin) {
        this.plugin = plugin;
    }

    @SuppressWarnings("unused")
    @EventHandler
    public void onPlayerKill(PlayerDeathEvent death) {
        if (!plugin.getActive()) return; // DO NOTHING IF NOT ACTIVE!
        if (death.getEntity().getKiller() != null) {
            // if the player doing the killing
            MafiaPlayer killer = plugin.getLivingPlayers().get(death.getEntity().getKiller().getUniqueId());
            if (killer.getRole().getAbilities().contains(Ability.CONVERT)) {
                MafiaPlayer killed = plugin.getPlayerList().get(death.getEntity().getUniqueId());
                if (killed == null) return;
                killed.makeAlive();
                killed.changeRole(Role.VAMPIRE);
                death.getEntity().sendMessage(ChatColor.DARK_PURPLE + "You are now a " + ChatColor.DARK_GRAY + "Vampire" + ChatColor.DARK_PURPLE + ".");
                killer.getStatus().startStatus(StatusData.Status.UNHOLY, plugin.getWorldFullTime() + 48000L); // Two days of unholy;
            }
        }
    }
}
