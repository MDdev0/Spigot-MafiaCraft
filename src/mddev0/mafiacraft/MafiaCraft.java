package mddev0.mafiacraft;

import mddev0.mafiacraft.abilities.*;
import mddev0.mafiacraft.util.CombatState;
import mddev0.mafiacraft.util.MafiaPlayer;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class MafiaCraft extends JavaPlugin {
    /*
     * TODO: CONFIG ITEMS TO ADD:
     * - forgeItemName
     * - attackDuration
     * - reanimateSacrifice
     */

    private final Map<UUID, MafiaPlayer> players = new HashMap<>();

    // Timed abilities
    private final HighNoon abilityHighNoon = new HighNoon(this);

    public void onEnable() {
        // Register abilities
        this.getServer().getPluginManager().registerEvents(new Protection(this), this);
        this.getServer().getPluginManager().registerEvents(new Succession(this), this);
        this.getServer().getPluginManager().registerEvents(new Forgery(this), this);
        this.getServer().getPluginManager().registerEvents(new Assassination(this), this);
        this.getServer().getPluginManager().registerEvents(new Reanimation(this), this);
        this.getServer().getPluginManager().registerEvents(new Retaliation(this), this);
        abilityHighNoon.runTaskTimer(this, 0L, 1L);
        this.getServer().getPluginManager().registerEvents(new Marksman(this), this);
        // Register combat state manager. This will trigger after all abilities. (Priority = High, whereas others are Normal)
        this.getServer().getPluginManager().registerEvents(new CombatState(this), this);
        // GUI events are handled every time a GUI is instantiated
    }

    public void onDisable() {
        for (Map.Entry<UUID,MafiaPlayer> p : players.entrySet())
            p.getValue().cancelTasks();
    }

    public Map<UUID, MafiaPlayer> getPlayerList() {
        return players;
    }
    public Map<UUID, MafiaPlayer> getLivingPlayers() {
        Map<UUID, MafiaPlayer> output = new HashMap<>();
        for (Map.Entry<UUID, MafiaPlayer> p : players.entrySet())
            if (p.getValue().isLiving())
                output.put(p.getKey(), p.getValue());
        return output;
    }
    public Map<UUID, MafiaPlayer> getDeadPlayers() {
        Map<UUID, MafiaPlayer> output = new HashMap<>();
        for (Map.Entry<UUID, MafiaPlayer> p : players.entrySet())
            if (!p.getValue().isLiving())
                output.put(p.getKey(), p.getValue());
        return output;
    }
}
