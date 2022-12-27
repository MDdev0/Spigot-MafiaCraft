package mddev0.mafiacraft;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import mddev0.mafiacraft.abilities.*;
import mddev0.mafiacraft.commands.MafiaCraftAdminCMD;
import mddev0.mafiacraft.commands.MafiaCraftCMD;
import mddev0.mafiacraft.util.*;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class MafiaCraft extends JavaPlugin {

    // TODO: find all uses of getPlayers(), should it be replaced with getLivingPlayers() or getDeadPlayers()?

    private final Map<UUID, MafiaPlayer> players = new HashMap<>();

    // Timed abilities
    private final HighNoon abilityHighNoon = new HighNoon(this);
    private final Ambrosia abilityAmbrosia = new Ambrosia(this);
    private final Inquisition abilityInquisition = new Inquisition(this);
    private final Transform abilityTransform = new Transform(this);
    private final Rampage abilityRampage = new Rampage(this);
    private final NightOwl abilityNightOwl = new NightOwl(this);

    private boolean active;

    // Game Finisher
    private final GameFinisher gameFinisher = new GameFinisher(this);

    public void onEnable() {
        // Config
        saveDefaultConfig();

        // GAME SAVE MANAGER
        GameSaver.init(this);
        GameSaver.loadGame();
        Bukkit.getPluginManager().registerEvents(new GameSaver.WorldSaveListener(), this);

        // ProtocolLib
        ProtocolManager manager = ProtocolLibrary.getProtocolManager();
        manager.addPacketListener(new HardcoreHearts(this));

        // Register death state manager. This has two different triggers.
        // FIRST TRIGGER: (Priority = Low, whereas others are Normal) recognizes death and flags player as dead.
        // SECOND TRIGGER: (Priority = High, whereas others are Normal) puts player in spectator and hides them from living players if they are still flagged.
        // ALSO TRIGGERS ON RESPAWN TO SET GAMEMODE
        Bukkit.getPluginManager().registerEvents(new DeathManager(this), this);

        // Manager for joining and leaving
        Bukkit.getPluginManager().registerEvents(new JoinLeaveManager(this), this);

        // Register abilities
        Bukkit.getPluginManager().registerEvents(new Protection(this), this);
        Bukkit.getPluginManager().registerEvents(new Succession(this), this);
        Bukkit.getPluginManager().registerEvents(new Forgery(this), this);
        Bukkit.getPluginManager().registerEvents(new Assassination(this), this);
        Bukkit.getPluginManager().registerEvents(new Reanimation(this), this);
        Bukkit.getPluginManager().registerEvents(new Retaliation(this), this);
        abilityHighNoon.runTaskTimer(this, 0L, 1L); // checks every tick
        Bukkit.getPluginManager().registerEvents(new Marksman(this), this);
        Bukkit.getPluginManager().registerEvents(new Investigate(this), this);
        Bukkit.getPluginManager().registerEvents(new Watch(this, manager), this);
        Bukkit.getPluginManager().registerEvents(new Peripherals(this, manager), this);
        Bukkit.getPluginManager().registerEvents(new ClearSight(this), this);
        Bukkit.getPluginManager().registerEvents(new Rescue(this), this);
        abilityAmbrosia.runTaskTimer(this, 0L, 100L); // checks every 5 seconds
        Bukkit.getPluginManager().registerEvents(abilityAmbrosia, this);
        abilityInquisition.runTaskTimer(this, 0L, 600L); // show particles every 10 seconds
        Bukkit.getPluginManager().registerEvents(new Ambush(this), this);
        Bukkit.getPluginManager().registerEvents(new ThisIsFine(this), this);
        Bukkit.getPluginManager().registerEvents(new DodgeRoll(this), this);
        Bukkit.getPluginManager().registerEvents(new SpellBook(this), this);
        Bukkit.getPluginManager().registerEvents(new Scatter(this), this);
        Bukkit.getPluginManager().registerEvents(new Toadify(this), this);
        Bukkit.getPluginManager().registerEvents(new FogOfWar(this), this);
        Bukkit.getPluginManager().registerEvents(new Vanish(this), this);
        abilityTransform.runTaskTimer(this, 0L, 100L); // Check night for Werewolves every 5 seconds
        Bukkit.getPluginManager().registerEvents(abilityRampage, this);
        abilityRampage.runTaskTimer(this, 0L, 100L); // Apply strength every 5 seconds
        Bukkit.getPluginManager().registerEvents(new Bite(this), this);
        Bukkit.getPluginManager().registerEvents(new Nemesis(this), this);
        Bukkit.getPluginManager().registerEvents(new Convert(this), this);
        Bukkit.getPluginManager().registerEvents(new HuntingNight(this), this);
        abilityNightOwl.runTaskTimer(this, 0L, 100L); // Check Day for Vampires every 5 seconds
        Bukkit.getPluginManager().registerEvents(new Staked(this), this);
        Bukkit.getPluginManager().registerEvents(new JustAPrank(this), this);

        // Register combat state manager. This will trigger after all abilities. (Priority = High, whereas others are Normal)
        Bukkit.getPluginManager().registerEvents(new CombatState(this), this);

        // GUI events are handled every time a GUI is instantiated

        // COMMANDS
        getCommand("mafiacraftadmin").setExecutor(new MafiaCraftAdminCMD(this));
        getCommand("mafiacraft").setExecutor(new MafiaCraftCMD(this));

        // Chat
        Bukkit.getPluginManager().registerEvents(new ChatBlocking(this), this);

        // Active
        active = getConfig().getBoolean("active");

        // Listener for finishing the game
        gameFinisher.runTaskTimer(this, 0L, 1L); // Check if game should end every tick
    }

    public void onDisable() {
        GameSaver.saveGame();
        for (Map.Entry<UUID,MafiaPlayer> p : players.entrySet())
            p.getValue().cancelTasks();
        abilityHighNoon.cancel();
        abilityAmbrosia.cancel();
        abilityInquisition.cancel();
        abilityTransform.cancel();
        abilityRampage.cancel();
        abilityNightOwl.cancel();
    }

    public boolean getActive() {
        return active;
    }
    public void setActive(boolean state) {
        this.active = state;
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

    // TODO: CLEANUP AND REMOVE
//    private class EventTester implements Listener {
//        @EventHandler
//        public void onEvent1(InventoryClickEvent inv) {
//            Bukkit.broadcastMessage("EVENT:" + inv.toString());
//        }
//    }
}
