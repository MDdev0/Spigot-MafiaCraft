package mddev0.mafiacraft;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import mddev0.mafiacraft.abilities.*;
import mddev0.mafiacraft.commands.MafiaCraftAdminCMD;
import mddev0.mafiacraft.commands.MafiaCraftCMD;
import mddev0.mafiacraft.player.MafiaPlayer;
import mddev0.mafiacraft.util.*;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class MafiaCraft extends JavaPlugin {

    private final ConcurrentMap<UUID, MafiaPlayer> players = new ConcurrentHashMap<>();

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

    private final GameRandomizer randomizer = new GameRandomizer(this);

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
        Bukkit.getPluginManager().registerEvents(new Assassinate(this), this);
        Bukkit.getPluginManager().registerEvents(new Revive(this), this);
        Bukkit.getPluginManager().registerEvents(new Retaliate(this), this);
        abilityHighNoon.runTaskTimer(this, 0L, 1L); // checks every tick
        Bukkit.getPluginManager().registerEvents(new Marksman(this), this);
        Bukkit.getPluginManager().registerEvents(new Investigate(this), this);
        Bukkit.getPluginManager().registerEvents(new Watch(this, manager), this);
        Bukkit.getPluginManager().registerEvents(new Peripherals(this, manager), this);
        Bukkit.getPluginManager().registerEvents(new ClearSight(this), this);
        Bukkit.getPluginManager().registerEvents(new Rescue(this), this);
        abilityAmbrosia.runTaskTimer(this, 0L, 100L); // checks every 5 seconds
        Bukkit.getPluginManager().registerEvents(abilityAmbrosia, this);
        abilityInquisition.runTaskTimer(this, 0L, 200L); // show particles every 10 seconds
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
        Bukkit.getPluginManager().registerEvents(new Nemesis(this), this);
        Bukkit.getPluginManager().registerEvents(new Convert(this), this);
        abilityNightOwl.runTaskTimer(this, 0L, 100L); // Check Day for Vampires every 5 seconds
        Bukkit.getPluginManager().registerEvents(new Staked(this), this);
        Bukkit.getPluginManager().registerEvents(new JustAPrank(this), this);

        // Register combat state manager. This will trigger after all abilities. (Priority = High, whereas others are Normal)
        Bukkit.getPluginManager().registerEvents(new CombatState(this), this);

        // GUI events are handled every time a GUI is instantiated

        // COMMANDS
        //noinspection DataFlowIssue
        getCommand("mafiacraftadmin").setExecutor(new MafiaCraftAdminCMD(this));
        //noinspection DataFlowIssue
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
            p.getValue().getSpyglass().cancel();
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
    public ConcurrentMap<UUID, MafiaPlayer> getPlayerList() {
        return players;
    }
    public ConcurrentMap<UUID, MafiaPlayer> getLivingPlayers() {
        ConcurrentMap<UUID, MafiaPlayer> output = new ConcurrentHashMap<>();
        for (Map.Entry<UUID, MafiaPlayer> p : players.entrySet())
            if (p.getValue().isLiving())
                output.put(p.getKey(), p.getValue());
        return output;
    }
    public ConcurrentMap<UUID, MafiaPlayer> getDeadPlayers() {
        ConcurrentMap<UUID, MafiaPlayer> output = new ConcurrentHashMap<>();
        for (Map.Entry<UUID, MafiaPlayer> p : players.entrySet())
            if (!p.getValue().isLiving())
                output.put(p.getKey(), p.getValue());
        return output;
    }
    public GameRandomizer getRandomizer() {
        return randomizer;
    }
    public Long getWorldFullTime() {
        return getServer().getWorlds().get(0).getFullTime();
    }
}
