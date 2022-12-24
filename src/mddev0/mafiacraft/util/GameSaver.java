package mddev0.mafiacraft.util;

import mddev0.mafiacraft.MafiaCraft;
import mddev0.mafiacraft.abilities.Ability;
import mddev0.mafiacraft.roles.Hunter;
import mddev0.mafiacraft.roles.Jester;
import mddev0.mafiacraft.roles.Role;
import mddev0.mafiacraft.roles.Werewolf;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.WorldSaveEvent;

import java.io.File;

import java.io.IOException;
import java.util.*;
import java.util.logging.Level;

public class GameSaver {

    private static MafiaCraft plugin = null;
    private static File playerDataFolder = null;

    public static void init(MafiaCraft plugin) {
        GameSaver.plugin = plugin;
        GameSaver.playerDataFolder = new File(plugin.getDataFolder().getPath() + File.separator + "players");
        if (!playerDataFolder.isDirectory()) {
            if (playerDataFolder.mkdir())
                Bukkit.getLogger().log(Level.INFO, "[MafiaCraft] Created player data folder");
            else
                Bukkit.getLogger().log(Level.WARNING, "[MafiaCraft] Unable to create or find player data folder");
        }
    }

    public static void saveGame() {
        if (plugin == null || playerDataFolder == null) {
            Bukkit.getLogger().log(Level.SEVERE, "[MafiaCraft] Cannot save player data! GameSaver has not been initialized!");
            return;
        }

        Bukkit.getLogger().log(Level.INFO, "[MafiaCraft] Saving player data");

        // Save each player
        Map<UUID, MafiaPlayer> players = plugin.getPlayerList();
        for (Map.Entry<UUID, MafiaPlayer> p : players.entrySet()) {
            MafiaPlayer player = p.getValue();
            // Create file if needed and set up variables
            OfflinePlayer offp = Bukkit.getPlayer(p.getKey());
            String dataName = (offp != null) ? offp.getName() : p.getKey().toString();
            File dataFile = new File(playerDataFolder, dataName + ".yml");
            if (!dataFile.exists()) {
                try {
                    if (dataFile.createNewFile())
                        Bukkit.getLogger().log(Level.INFO, "[MafiaCraft] Created player data file: " + dataName);
                    else {
                        Bukkit.getLogger().log(Level.WARNING, "[MafiaCraft] Unable to create player data file: " + dataName + ", skipping save for this player");
                        continue;
                    }
                } catch (IOException e) {
                    Bukkit.getLogger().log(Level.SEVERE, "[MafiaCraft] Error while attempting to save " + dataName + ", skipping save for this player: ", e);
                    continue;
                }
            }
            FileConfiguration data = YamlConfiguration.loadConfiguration(dataFile);

            // Write player data to data file
            MafiaPlayer.PlayerData playerData = player.getDataRecord();
            data.set("uuid", playerData.uuid().toString());
            data.set("living", playerData.living());
            data.set("role", playerData.role().getClass().getSimpleName());
            data.set("originalRole", playerData.originalRole().getClass().getSimpleName());
            for (Map.Entry<Ability, MafiaPlayer.CooldownData> cooldown : playerData.cooldowns().entrySet()) {
                data.set("cooldowns." + cooldown.getKey().name().toLowerCase() +
                        ".dayTime", cooldown.getValue().dayTime());
                data.set("cooldowns." + cooldown.getKey().name().toLowerCase() +
                        ".days", cooldown.getValue().days());
            }
            data.set("framed", playerData.framed());
            data.set("unholyTicks", playerData.unholyTicks());
            data.set("attackerTicks", playerData.attackerTicks());

            // Custom data if the role has any
            Class<?> roleCls = player.getRole().getClass();
            switch (roleCls.getSimpleName()) {
                case "Hunter" -> {
                    List<String> targetsStr = new ArrayList<>();
                    for (UUID target : ((Hunter) player.getRole()).getTargets()) {
                        targetsStr.add(target.toString());
                        data.set("roleData.targets", targetsStr);
                    }
                }
                case "Jester" -> data.set("roleData.abilityTriggered", ((Jester) player.getRole()).getAbilityActivated());
                case "Werewolf" -> {
                    data.set("roleData.transformed", ((Werewolf) player.getRole()).getTransformed());
                    data.set("roleData.killsWhileTransformed", ((Werewolf) player.getRole()).getKills());
                }
            }

            // Custom data if the original role has any
            Class<?> originalRoleCls = player.getOriginalRole().getClass();
            switch (originalRoleCls.getSimpleName()) {
                case "Hunter" -> {
                    List<String> targetsStr = new ArrayList<>();
                    for (UUID target : ((Hunter) player.getOriginalRole()).getTargets()) {
                        targetsStr.add(target.toString());
                        data.set("originalRoleData.targets", targetsStr);
                    }
                }
                case "Jester" -> data.set("originalRoleData.abilityTriggered", ((Jester) player.getOriginalRole()).getAbilityActivated());
                case "Werewolf" -> {
                    data.set("originalRoleData.transformed", ((Werewolf) player.getOriginalRole()).getTransformed());
                    data.set("originalRoleData.killsWhileTransformed", ((Werewolf) player.getOriginalRole()).getKills());
                }
            }

            // Save file
            try {
                data.save(dataFile);
            } catch (IOException e) {
                Bukkit.getLogger().log(Level.WARNING, "Unable to save player file " + dataFile.getName() + ":", e);
            }
        }
    }

    public static void loadGame() {
        if (plugin == null || playerDataFolder == null) {
            Bukkit.getLogger().log(Level.SEVERE, "[MafiaCraft] Cannot save player data! GameSaver has not been initialized!");
            return;
        }
        File[] dataFileList = playerDataFolder.listFiles();
        if (dataFileList == null || dataFileList.length == 0) {
            Bukkit.getLogger().log(Level.INFO, "[MafiaCraft] Found no player data to load");
            return;
        }

        Bukkit.getLogger().log(Level.INFO, "[MafiaCraft] Loading player data from " + dataFileList.length + " files");

        // For all players in the files
        for (File dataFile : dataFileList) {
            FileConfiguration data = YamlConfiguration.loadConfiguration(dataFile);

            // cooldowns
            Map<Ability, MafiaPlayer.CooldownData> cooldowns = new HashMap<>();
            ConfigurationSection cooldownSection = data.getConfigurationSection("cooldowns");
            if (cooldownSection != null) {
                Set<String> cools = cooldownSection.getKeys(false);
                for (String ability : cools) {
                    cooldowns.put(Ability.valueOf(ability.toUpperCase()), new MafiaPlayer.CooldownData(
                            cooldownSection.getLong(ability + ".dayTime"),  cooldownSection.getInt(ability + ".days"))
                    );
                }
            }

            // Role
            Role role;
            try {
                Class<?> roleCls = Class.forName("mddev0.mafiacraft.roles." + data.getString("role"));
                switch (roleCls.getSimpleName()) {
                    case "Hunter" -> {
                        List<UUID> targets = new ArrayList<>();
                        for (String uuidStr : data.getStringList("roleData.targets"))
                            targets.add(UUID.fromString(uuidStr));
                        role = (Hunter) roleCls.getDeclaredConstructor(MafiaCraft.class, UUID.class, List.class).newInstance(plugin, UUID.fromString(data.getString("uuid")), targets);
                    }
                    case "Jester" -> {
                        role = (Jester) roleCls.getDeclaredConstructor().newInstance();
                        if (data.getBoolean("roleData.abilityTriggered")) ((Jester) role).activate();
                    }
                    case "Werewolf" -> {
                        role = (Werewolf) roleCls.getDeclaredConstructor().newInstance();
                        ((Werewolf) role).setTransformed(data.getBoolean("roleData.transformed"));
                        for (int k = 0; k < data.getInt("roleData.killsWhileTransformed"); k++)
                            ((Werewolf) role).incrementKills();
                    }
                    default -> role = (Role) roleCls.getDeclaredConstructor().newInstance();
                }
            } catch (ReflectiveOperationException | IllegalArgumentException e) {
                Bukkit.getLogger().log(Level.SEVERE, "[MafiaCraft] Unable to create player from file " + dataFile.getName() + ". Encountered an error during Role creation: ", e);
                continue; // Skip this iteration of the loop!
            }

            // Original Role
            Role originalRole;
            try {
                Class<?> originalRoleCls = Class.forName("mddev0.mafiacraft.roles." + data.getString("originalRole"));
                switch (originalRoleCls.getSimpleName()) {
                    case "Hunter" -> {
                        List<UUID> targets = new ArrayList<>();
                        for (String uuidStr : data.getStringList("originalRoleData.targets"))
                            targets.add(UUID.fromString(uuidStr));
                        originalRole = (Hunter) originalRoleCls.getDeclaredConstructor(MafiaCraft.class, UUID.class, List.class).newInstance(plugin, UUID.fromString(data.getString("uuid")), targets);
                    }
                    case "Jester" -> {
                        originalRole = (Jester) originalRoleCls.getDeclaredConstructor().newInstance();
                        if (data.getBoolean("originalRoleData.abilityTriggered")) ((Jester) originalRole).activate();
                    }
                    case "Werewolf" -> {
                        originalRole = (Werewolf) originalRoleCls.getDeclaredConstructor().newInstance();
                        ((Werewolf) originalRole).setTransformed(data.getBoolean("originalRoleData.transformed"));
                        for (int k = 0; k < data.getInt("originalRoleData.killsWhileTransformed"); k++)
                            ((Werewolf) originalRole).incrementKills();
                    }
                    default -> originalRole = (Role) originalRoleCls.getDeclaredConstructor().newInstance();
                }
            } catch (ReflectiveOperationException | IllegalArgumentException e) {
                Bukkit.getLogger().log(Level.SEVERE, "[MafiaCraft] Unable to create player from file " + dataFile.getName() + ". Encountered an error during Original Role creation: ", e);
                continue; // Skip this iteration of the loop!
            }

            // Add player to game
            plugin.getPlayerList().put(UUID.fromString(data.getString("uuid")), new MafiaPlayer(plugin, new MafiaPlayer.PlayerData(
                    UUID.fromString(data.getString("uuid")),
                    data.getBoolean("living"),
                    role,
                    originalRole,
                    cooldowns,
                    data.getBoolean("framed"),
                    data.getLong("unholyTicks"),
                    data.getInt("attackerTicks"))));

            Bukkit.getLogger().log(Level.INFO, "Added player from save file: " + dataFile.getName());
        }

        Bukkit.getLogger().log(Level.INFO, "[MafiaCraft] Completed player data loading procedure");
    }

    public static class WorldSaveListener implements Listener {
        @EventHandler
        public void onWorldSave(WorldSaveEvent save) {
            saveGame();
        }
    }
}
