package mddev0.mafiacraft;

import mddev0.mafiacraft.abilities.Protection;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class MafiaCraft extends JavaPlugin {
    /*
     * CONFIG ITEMS TO ADD:
     * - Attack duration time
     */

    private Map<UUID, MafiaPlayer> players = new HashMap<>();

    public void onEnable() {
        this.getServer().getPluginManager().registerEvents(new Protection(this), this);
    }

    public void onDisable() {
        for (UUID p : players.keySet())
            players.get(p).cancelTasks();
    }

    public Map<UUID, MafiaPlayer> getPlayerList() {
        return players;
    }
}
