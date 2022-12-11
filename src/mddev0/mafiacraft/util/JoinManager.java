package mddev0.mafiacraft.util;

import mddev0.mafiacraft.MafiaCraft;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.Map;
import java.util.UUID;

public class JoinManager implements Listener {

    private final MafiaCraft plugin;

    public JoinManager(MafiaCraft plugin) {
        this.plugin = plugin;
    }

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
            // all dead players should be hidden
            for (Player p : plugin.getServer().getOnlinePlayers()) {
                MafiaPlayer spec = plugin.getPlayerList().get(p.getUniqueId());
                if (spec == null || !spec.isLiving()) {
                    join.getPlayer().hidePlayer(plugin, p);
                }
            }
        }
        else { // player is not in game or is dead
            // set spectator and hide join message
            join.getPlayer().setGameMode(GameMode.SPECTATOR);
            join.setJoinMessage(null);
            // hide player from all living players
            for (Map.Entry<UUID, MafiaPlayer> living : plugin.getLivingPlayers().entrySet()) {
                OfflinePlayer offp = plugin.getServer().getOfflinePlayer(living.getKey());
                if (offp.isOnline()) {
                    Player p = offp.getPlayer();
                    assert p != null;
                    p.hidePlayer(plugin, join.getPlayer());
                }
            }
        }
    }
}
