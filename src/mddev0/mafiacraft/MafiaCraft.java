package mddev0.mafiacraft;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import mddev0.mafiacraft.abilities.*;
import mddev0.mafiacraft.util.CombatState;
import mddev0.mafiacraft.util.DeathManager;
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

    // TODO: find all uses of getPlayers(), should it be replaced with getLivingPlayers() or getDeadPlayers()?

    private final Map<UUID, MafiaPlayer> players = new HashMap<>();

    // Timed abilities
    private final HighNoon abilityHighNoon = new HighNoon(this);
    private final Ambrosia abilityAmbrosia = new Ambrosia(this);
    private final Inquisition abilityInquisition = new Inquisition(this);
    private final Transform abilityTransform = new Transform(this);
    private final Rampage abilityRampage = new Rampage(this);

    public void onEnable() {
        // ProtocolLib
        ProtocolManager manager = ProtocolLibrary.getProtocolManager();

        // Register death state manager. This will trigger before all abilities. (Priority = Low, whereas others are Normal)
        this.getServer().getPluginManager().registerEvents(new DeathManager(this), this);

        // Register abilities
        this.getServer().getPluginManager().registerEvents(new Protection(this), this);
        this.getServer().getPluginManager().registerEvents(new Succession(this), this);
        this.getServer().getPluginManager().registerEvents(new Forgery(this), this);
        this.getServer().getPluginManager().registerEvents(new Assassination(this), this);
        this.getServer().getPluginManager().registerEvents(new Reanimation(this), this);
        this.getServer().getPluginManager().registerEvents(new Retaliation(this), this);
        abilityHighNoon.runTaskTimer(this, 0L, 1L); // checks every tick
        this.getServer().getPluginManager().registerEvents(new Marksman(this), this);
        this.getServer().getPluginManager().registerEvents(new Investigate(this), this);
        this.getServer().getPluginManager().registerEvents(new Watch(this, manager), this);
        this.getServer().getPluginManager().registerEvents(new Peripherals(this, manager), this);
        this.getServer().getPluginManager().registerEvents(new ClearSight(this), this);
        this.getServer().getPluginManager().registerEvents(new Rescue(this), this);
        abilityAmbrosia.runTaskTimer(this, 0L, 100L); // checks every 5 seconds
        this.getServer().getPluginManager().registerEvents(abilityAmbrosia, this);
        abilityInquisition.runTaskTimer(this, 0L, 200L); // show particles every 10 seconds
        this.getServer().getPluginManager().registerEvents(new Ambush(this), this);
        this.getServer().getPluginManager().registerEvents(new ThisIsFine(this), this);
        this.getServer().getPluginManager().registerEvents(new DodgeRoll(this), this);
        this.getServer().getPluginManager().registerEvents(new SpellBook(this), this);
        this.getServer().getPluginManager().registerEvents(new Scatter(this), this);
        this.getServer().getPluginManager().registerEvents(new Toadify(this), this);
        this.getServer().getPluginManager().registerEvents(new FogOfWar(this), this);
        this.getServer().getPluginManager().registerEvents(new Vanish(this), this);
        abilityTransform.runTaskTimer(this, 0L, 100L); // Check night for Werewolves every 5 seconds
        this.getServer().getPluginManager().registerEvents(abilityRampage, this);
        abilityRampage.runTaskTimer(this, 0L, 100L); // Apply strength every 5 seconds
        this.getServer().getPluginManager().registerEvents(new Bite(this), this);
        this.getServer().getPluginManager().registerEvents(new Nemesis(this), this);
        this.getServer().getPluginManager().registerEvents(new Convert(this), this);
        this.getServer().getPluginManager().registerEvents(new HuntingNight(this), this);

        // Register combat state manager. This will trigger after all abilities. (Priority = High, whereas others are Normal)
        this.getServer().getPluginManager().registerEvents(new CombatState(this), this);

        // GUI events are handled every time a GUI is instantiated
    }

    public void onDisable() {
        for (Map.Entry<UUID,MafiaPlayer> p : players.entrySet())
            p.getValue().cancelTasks();
        abilityHighNoon.cancel();
        abilityAmbrosia.cancel();
        abilityInquisition.cancel();
        abilityTransform.cancel();
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
