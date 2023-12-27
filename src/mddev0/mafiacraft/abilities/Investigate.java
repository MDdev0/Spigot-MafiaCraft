package mddev0.mafiacraft.abilities;

import mddev0.mafiacraft.MafiaCraft;
import mddev0.mafiacraft.player.MafiaPlayer;
import mddev0.mafiacraft.player.Role;
import mddev0.mafiacraft.player.StatusData;
import mddev0.mafiacraft.util.SpyglassUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.scheduler.BukkitRunnable;

public final class Investigate implements Listener {

    private final MafiaCraft plugin;

    public Investigate(MafiaCraft plugin) {
        this.plugin = plugin;
    }

    @SuppressWarnings("unused")
    @EventHandler
    public void onSpyglassLook(PlayerInteractEvent click) {
        if (!plugin.getActive()) return; // DO NOTHING IF NOT ACTIVE!
        if (click.getItem() != null && click.getItem().getType() == Material.SPYGLASS) {
            // Material is spyglass, check player
            MafiaPlayer clicker = plugin.getLivingPlayers().get(click.getPlayer().getUniqueId());
            if (clicker != null && clicker.getRole().getAbilities().contains(Ability.INVESTIGATE)) {
                // Delay running checks on spyglass for one tick to make sure it has time to be set properly
                (new AbilityActivateTimer(plugin, plugin.getLivingPlayers().get(click.getPlayer().getUniqueId()))).runTaskTimer(plugin, 1L, 1L);
            }
        }
    }

    private final static class AbilityActivateTimer extends BukkitRunnable {
        private final MafiaCraft plugin;
        private final double targetTime;
        private final MafiaPlayer user;
        private long ticksActive;

        private AbilityActivateTimer(MafiaCraft plugin, MafiaPlayer user) {
            this.plugin = plugin;
            targetTime = plugin.getConfig().getDouble("spyglassTargetTime");
            this.user = user;
            ticksActive = 0;
        }

        // This will be run every tick to see if a user is tracking their target
        @Override
        public void run() {
            SpyglassUtil spyglass = user.getSpyglass();
            if (spyglass.isActive()) {
                // spyglass is still active, all is well
                ticksActive += 1;
                Player holder = Bukkit.getPlayer(user.getID());
                if (holder == null) this.cancel(); // cancel if player doesn't exist (somehow)
                assert holder != null;
                if (ticksActive % 20 == 0) { // play sound once per second
                    holder.playSound(holder.getLocation(), Sound.BLOCK_NOTE_BLOCK_HAT, 1.0f, 2.0f);
                }
                if (ticksActive >= (20 * targetTime)) { // 20 ticks per sec.
                    // Finished!
                    MafiaPlayer target = plugin.getLivingPlayers().get(spyglass.getTargeted().getUniqueId());
                    // Check if player is suspicious
                    boolean sus = target.getRole().getAlignment() == Role.Team.MAFIA;
                    sus = sus || (target.getStatus().hasStatus(StatusData.Status.FRAMED)); // Sus if they are framed
                    sus = sus && (!target.getRole().getAbilities().contains(Ability.CHARISMA)); // Clean if they have charisma
                    if (sus) {
                        holder.sendMessage(ChatColor.GRAY + "This player seems to be "
                                + ChatColor.RED + "" + ChatColor.BOLD + "suspicious" +
                                ChatColor.RESET + "" + ChatColor.GRAY + ".");
                    } else {
                        holder.sendMessage(ChatColor.GRAY + "This player seems to be "
                                + ChatColor.DARK_GREEN + "" + ChatColor.BOLD + "not suspicious" +
                                ChatColor.RESET + "" + ChatColor.GRAY + ".");
                    }
                    this.cancel();
                }
            } else {
                // Spyglass is not active, cancel this
                this.cancel();
            }
        }
    }
}
