package mddev0.mafiacraft.util;

import mddev0.mafiacraft.MafiaCraft;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

// SCUFFED: THIS WHOLE DAMN CLASS

/**
 * This class handles all aspects of checking whether a player
 * is scoped in on another player with a spyglass for 10 seconds.
 */
public class SpyglassUtil extends BukkitRunnable {
    private final MafiaCraft plugin;
    private int ticks = 0;
    private final Player holder;
    private Player targeted;
    private int secsTargeted = 0;
    private boolean running = false;
    BukkitRunnable timer = new BukkitRunnable() {
        @Override
        public void run() {
            Player found = null;
            for (Player t : holder.getWorld().getPlayers()) {
                if (lookingAtPlayer(plugin, holder, t)) {
                    found = t;
                }
            }
            if (found == null)
                secsTargeted = 0;
            else if (found.equals(targeted)) {
                holder.playSound(holder.getLocation(), Sound.BLOCK_NOTE_BLOCK_HAT, 1.0f, 2.0f);
                secsTargeted++;
            }
            else {
                secsTargeted = 0;
                targeted = found;
            }
        }
    };

    public SpyglassUtil(MafiaCraft plugin, Player p) {
        this.plugin = plugin;
        this.holder = p;
    }

    @Override
    public void run() {
        ticks = (ticks>0) ? ticks-1 : 0;
        if (ticks == 0) {
            secsTargeted = 0;
            if (running) {
                timer.cancel();
            }
        }
    }

    public void refresh() {
        ticks = 5; // event is fired every 4 ticks or fewer when right click is held
    }

    public boolean isSpyglassActive() {
        return ticks > 0;
    }

    public void startTimer() {
        running = true;
        timer.runTaskTimer(plugin, 0L, 20L); // checks every second
    }

    public boolean finished() {
        if (secsTargeted >= plugin.getConfig().getInt("spyglassTargetTime")) {
            // timer is done, close up shop
            timer.cancel();
            return true;
        } else
            return false; // not done, keep going
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
