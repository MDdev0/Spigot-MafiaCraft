package mddev0.mafiacraft.abilities;

import mddev0.mafiacraft.MafiaCraft;
import mddev0.mafiacraft.util.MafiaPlayer;
import mddev0.mafiacraft.util.SpyglassUtil;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

public final class Investigate implements Listener {

    private final MafiaCraft plugin;

    public Investigate(MafiaCraft plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onSpyglassLook(PlayerInteractEvent click) {
        if (click.getItem() != null && click.getItem().getType() == Material.SPYGLASS) {
            // Material is spyglass, check player
            MafiaPlayer clicker = plugin.getPlayerList().get(click.getPlayer().getUniqueId());
            if (clicker.getRole().hasAbility(Ability.INVESTIGATE)) {
                // Can investigate
                SpyglassUtil spyglass = clicker.getSpyglass();
                // Always refresh the spyglass
                spyglass.refresh();
                // Must check if spyglass is being opened or if it was already active
                if (!spyglass.isSpyglassActive()) {
                    // Spyglass was not active, start timer
                    spyglass.startTimer();
                } else {
                    // Spyglass was already active, check timer
                    if (spyglass.finished()) {
                        // TELL PLAYER!
                        Player toTell = click.getPlayer();
                        if (plugin.getLivingPlayers().get(spyglass.getTargeted().getUniqueId()).isMafiaSuspect())
                            toTell.sendMessage(ChatColor.GRAY + "This player seems to be "
                                + ChatColor.RED + "" + ChatColor.BOLD + "suspicious" +
                                    ChatColor.RESET + "" + ChatColor.GRAY + ".");
                        else
                            toTell.sendMessage(ChatColor.GRAY + "This player seems to be "
                                    + ChatColor.DARK_GREEN + "" + ChatColor.BOLD + "not suspicious" +
                                    ChatColor.RESET + "" + ChatColor.GRAY + ".");
                    }
                }
            }
        }
    }
}
