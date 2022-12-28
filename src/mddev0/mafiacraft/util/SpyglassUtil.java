package mddev0.mafiacraft.util;

import mddev0.mafiacraft.MafiaCraft;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;

import java.util.Objects;
import java.util.UUID;

// SCUFFED: THIS WHOLE DAMN CLASS

/**
 * This class handles all aspects of checking whether a player
 * is scoped in on another player with a spyglass for 10 seconds.
 */
public class SpyglassUtil extends BukkitRunnable {
    private final MafiaCraft plugin;
    private final OfflinePlayer holder;
    private Player targeted;
    boolean active = false;

    public SpyglassUtil(MafiaCraft plugin, OfflinePlayer p) {
        this.plugin = plugin;
        this.holder = p;
        targeted = null;
    }

    @Override
    public void run() {
        if (holder == null || !holder.isOnline()) return; // holder is offline
        ItemStack using = Objects.requireNonNull(holder.getPlayer()).getItemInUse();
        active = using != null && using.getType() == Material.SPYGLASS;
        // only active if player is using spyglass
        if (active) {
            if (targeted == null || !lookingAtPlayer(plugin,holder.getPlayer(),targeted)) {
                // not looking at previously targeted player
                active = false;
                // find new player
                for (UUID toCheck : plugin.getLivingPlayers().keySet()) {
                    Player p = Bukkit.getPlayer(toCheck);
                    if (p != null && p.isOnline() && lookingAtPlayer(plugin,holder.getPlayer(),p)) {
                        // set new target
                        targeted = p;
                        break; // break once target is found
                    } else targeted = null;
                }
            }
        }
        if (active) {
            // If line of sight between player and target is interrupted, then false. otherwise true
            double lineDistance = holder.getPlayer().getEyeLocation().distance(targeted.getEyeLocation());
            RayTraceResult lineOfSight = holder.getPlayer().rayTraceBlocks(lineDistance, FluidCollisionMode.NEVER);
            active = (lineOfSight == null);
        }
    }

    public boolean isActive() {
        return active;
    }

    public Player getTargeted() {
        return targeted;
    }

    public static boolean lookingAtPlayer(Plugin plugin, Player p, Player target) {
        // "Inspired" by this thread: (I pretty much ripped it let's be honest)
        // https://www.spigotmc.org/threads/how-to-detect-an-entity-the-player-is-looking-at.139310/
        Location eye = p.getEyeLocation();
        Vector toEntity = target.getEyeLocation().toVector().subtract(eye.toVector());
        double dotProd = toEntity.normalize().dot(eye.getDirection());
        return dotProd > (1.00D - plugin.getConfig().getDouble("spyglassSensitivity"));
    }
}
