package mddev0.mafiacraft.util;

import mddev0.mafiacraft.MafiaCraft;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

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
    public void onPlayerDeathEarly(PlayerDeathEvent death) {
        if (!plugin.getActive()) return;
        plugin.getPlayerList().get(death.getEntity().getUniqueId()).makeDead();
    }

    @EventHandler (priority = EventPriority.HIGH)
    public void onPlayerDeathLate(PlayerDeathEvent death) {
        if (!plugin.getActive()) return;
        MafiaPlayer dead = plugin.getPlayerList().get(death.getEntity().getUniqueId());
        if (dead != null && !dead.isLiving() && death.getEntity().getGameMode() != GameMode.SPECTATOR) {
            Player died = death.getEntity();
            Bukkit.broadcastMessage(ChatColor.YELLOW + died.getName() + " left the game");
            for (Player p : plugin.getServer().getOnlinePlayers()) {
                MafiaPlayer live = plugin.getLivingPlayers().get(p.getUniqueId());
                if (live != null) {
                    // player is online and alive
                    p.hidePlayer(plugin, died);
                }
            }
        }
    }

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent res) {
        if (!plugin.getActive()) return;
        MafiaPlayer dead = plugin.getPlayerList().get(res.getPlayer().getUniqueId());
        if (dead != null && !dead.isLiving()) {
            res.getPlayer().setGameMode(GameMode.SPECTATOR);
        }
    }
}
