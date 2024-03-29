package mddev0.mafiacraft.util;

import mddev0.mafiacraft.MafiaCraft;
import mddev0.mafiacraft.player.MafiaPlayer;
import mddev0.mafiacraft.player.RoleData;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.Map;
import java.util.UUID;

public class JoinLeaveManager implements Listener {

    private final MafiaCraft plugin;

    public JoinLeaveManager(MafiaCraft plugin) {
        this.plugin = plugin;
    }

    @SuppressWarnings("unused")
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent join) {
        if (!plugin.getActive()) return;
        MafiaPlayer joined = plugin.getPlayerList().get(join.getPlayer().getUniqueId());
        if (joined != null && joined.isLiving()) {
            if (join.getPlayer().getGameMode() == GameMode.SPECTATOR) {
                // player needs to be respawned
                Location toSpawn = (join.getPlayer().getBedSpawnLocation() == null) ? plugin.getServer().getWorlds().get(0).getSpawnLocation() : join.getPlayer().getBedSpawnLocation();
                join.getPlayer().teleport(toSpawn);
            }
            join.getPlayer().setGameMode(GameMode.SURVIVAL);
            if (plugin.getConfig().getBoolean("hideDeadPlayers")) {
                // if true, dead players should be hidden
                for (Player p : plugin.getServer().getOnlinePlayers()) {
                    MafiaPlayer spec = plugin.getPlayerList().get(p.getUniqueId());
                    if (spec == null || !spec.isLiving()) {
                        join.getPlayer().hidePlayer(plugin, p);
                    }
                }
            }
        } else { // player is not in game or is dead
            // TODO: set spectator? Potential for dead players to remain in world?
            join.getPlayer().setGameMode(GameMode.SPECTATOR);
            if (plugin.getConfig().getBoolean("hideDeadPlayers")) {
                // if true, hide player from all living players
                join.setJoinMessage(null);
                for (Map.Entry<UUID, MafiaPlayer> living : plugin.getLivingPlayers().entrySet()) {
                    OfflinePlayer offp = Bukkit.getOfflinePlayer(living.getKey());
                    if (offp.isOnline()) {
                        Player p = offp.getPlayer();
                        assert p != null;
                        p.hidePlayer(plugin, join.getPlayer());
                    }
                }
            }
        }
        // Handle Jester
        if (joined != null) {
            Boolean jestStatus = (Boolean) joined.getRoleData().getData(RoleData.DataType.JESTER_ABILITY_USED);
            if (jestStatus != null && jestStatus) {
                join.getPlayer().setDisplayName("[" + ChatColor.LIGHT_PURPLE + "Jester" + ChatColor.RESET + "] " + join.getPlayer().getDisplayName());
                join.getPlayer().setPlayerListName("[" + ChatColor.LIGHT_PURPLE + "Jester" + ChatColor.RESET + "] " + join.getPlayer().getPlayerListName());
            }
        }
    }

    @SuppressWarnings("unused")
    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent leave) {
        if (!plugin.getActive()) return;
        if (plugin.getConfig().getBoolean("hideDeadPlayers")) {
            MafiaPlayer left = plugin.getPlayerList().get(leave.getPlayer().getUniqueId());
            if (left == null || !left.isLiving()) {
                leave.setQuitMessage(null);
            }
        }
    }
}
