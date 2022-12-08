package mddev0.mafiacraft.util;

import mddev0.mafiacraft.MafiaCraft;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class DeathManager implements Listener {

    private final MafiaCraft plugin;

    public DeathManager(MafiaCraft plugin) {
        this.plugin = plugin;
    }

    /**
     * Is called before any abilities. Will mark the player as dead.
     * Later events that prevent death can make them alive again.
     */
    @EventHandler (priority = EventPriority.LOW)
    public void onPlayerDeath(PlayerDeathEvent death) {
        plugin.getPlayerList().get(death.getEntity().getUniqueId()).makeDead();
    }
}
