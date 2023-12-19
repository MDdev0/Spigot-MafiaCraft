package mddev0.mafiacraft.util;

import mddev0.mafiacraft.MafiaCraft;
import mddev0.mafiacraft.abilities.Ability;
import mddev0.mafiacraft.player.*;
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
            Bukkit.getLogger().log(Level.SEVERE, "[MafiaCraft] Cannot save player data! GameSaver has not been initialized! If you see this, file a bug report!");
            return;
        }

        // Save each player
        Map<UUID, MafiaPlayer> players = plugin.getPlayerList();
        for (Map.Entry<UUID, MafiaPlayer> p : players.entrySet()) {
            MafiaPlayer player = p.getValue();
            // Create file if needed and set up variables
            OfflinePlayer offp = Bukkit.getOfflinePlayer(p.getKey());
            String dataName = (offp.getName() != null) ? offp.getName() : p.getKey().toString();
            File dataFile = new File(playerDataFolder, dataName + ".yml");
            if (!dataFile.exists()) {
                try {
                    if (dataFile.createNewFile())
                        Bukkit.getLogger().log(Level.INFO, "[MafiaCraft] Created player data file: " + dataName);
                    else {
                        Bukkit.getLogger().log(Level.WARNING, "[MafiaCraft] Unable to create player data file: " + dataName + ", skipping dataMap for this player");
                        continue;
                    }
                } catch (IOException e) {
                    Bukkit.getLogger().log(Level.SEVERE, "[MafiaCraft] Error while attempting to dataMap " + dataName + ", skipping dataMap for this player: ", e);
                    continue;
                }
            }
            FileConfiguration data = YamlConfiguration.loadConfiguration(dataFile);

            /*
             * Write player data to file
             */
            MafiaPlayer.PlayerSaveData playerData = player.getSaveData();
            data.set("uuid", playerData.uuid().toString());
            data.set("living", playerData.living());
            // Role and Original Role data
            data.set("role", playerData.role().name());
            for (Map.Entry<RoleData.DataType, Object> roleDataEntry : playerData.roleData().dataMap().entrySet())
                data.set("roleData." + roleDataEntry.getKey().name(), roleDataEntry.getValue());
            data.set("originalRole", playerData.originalRole().name());
            for (Map.Entry<RoleData.DataType, Object> originalRoleDataEntry : playerData.roleData().dataMap().entrySet())
                data.set("originalRoleData." + originalRoleDataEntry.getKey().name(), originalRoleDataEntry.getValue());
            // Cooldown Data
            for (Map.Entry<Ability, Long> cooldownDataEntry : playerData.cooldowns().cooldownMap().entrySet())
                data.set("cooldowns." + cooldownDataEntry.getKey().name(), cooldownDataEntry.getValue());
            // Status Data
            for (Map.Entry<StatusData.Status, Long> statusDataEntry : playerData.status().statusMap().entrySet())
                data.set("status." + statusDataEntry.getKey().name(), statusDataEntry.getValue());

            // Save file
            try {
                data.save(dataFile);
            } catch (IOException e) {
                Bukkit.getLogger().log(Level.WARNING, "Unable to dataMap player file " + dataFile.getName() + ":", e);
            }
        }
    }

    public static void loadGame() {
        if (plugin == null || playerDataFolder == null) {
            Bukkit.getLogger().log(Level.SEVERE, "[MafiaCraft] Cannot load player data! GameSaver has not been initialized!");
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

            // Status Data
            HashMap<StatusData.Status, Long> statusMap = new HashMap<>();
            ConfigurationSection statusSection = data.getConfigurationSection("status");
            if (statusSection != null) {
                Set<String> statuses = statusSection.getKeys(false);
                for (String s : statuses) {
                    statusMap.put(StatusData.Status.valueOf(s), statusSection.getLong(s));
                }
            }
            StatusData.StatusDataSave statusData = new StatusData.StatusDataSave(statusMap);

            // Cooldown Data
            HashMap<Ability, Long> cooldownMap = new HashMap<>();
            ConfigurationSection cooldownSection = data.getConfigurationSection("cooldowns");
            if (cooldownSection != null) {
                Set<String> cooldowns = cooldownSection.getKeys(false);
                for (String c : cooldowns) {
                    cooldownMap.put(Ability.valueOf(c), cooldownSection.getLong(c));
                }
            }
            CooldownData.CooldownDataSave cooldownData = new CooldownData.CooldownDataSave(cooldownMap);

            // Role Data
            HashMap<RoleData.DataType, Object> roleDataMap = new HashMap<>();
            ConfigurationSection roleDataSection = data.getConfigurationSection("roleData");
            if (roleDataSection != null) {
                Set<String> rdEntries = roleDataSection.getKeys(false);
                for (String rd : rdEntries) {
                    roleDataMap.put(RoleData.DataType.valueOf(rd), roleDataSection.get(rd));
                }
            }
            RoleData.RoleDataSave roleData = new RoleData.RoleDataSave(roleDataMap);

            // Original Role Data
            HashMap<RoleData.DataType, Object> originalRoleDataMap = new HashMap<>();
            ConfigurationSection originalRoleDataSection = data.getConfigurationSection("originalRoleData");
            if (originalRoleDataSection != null) {
                Set<String> rdEntries = originalRoleDataSection.getKeys(false);
                for (String rd : rdEntries) {
                    originalRoleDataMap.put(RoleData.DataType.valueOf(rd), originalRoleDataSection.get(rd));
                }
            }
            RoleData.RoleDataSave originalRoleData = new RoleData.RoleDataSave(originalRoleDataMap);

            // Add player to game
            plugin.getPlayerList().put(UUID.fromString(Objects.requireNonNull(data.getString("uuid"))), new MafiaPlayer(plugin, new MafiaPlayer.PlayerSaveData(
                    UUID.fromString(Objects.requireNonNull(data.getString("uuid"))),
                    data.getBoolean("living"),
                    Role.valueOf(data.getString("role")),
                    roleData,
                    Role.valueOf(data.getString("originalRole")),
                    originalRoleData,
                    cooldownData,
                    statusData
            )));

            Bukkit.getLogger().log(Level.INFO, "Added player from dataMap file: " + dataFile.getName());
        }

        Bukkit.getLogger().log(Level.INFO, "[MafiaCraft] Completed player data loading procedure");
    }

    public static class WorldSaveListener implements Listener {
        @SuppressWarnings("unused")
        @EventHandler
        public void onWorldSave(WorldSaveEvent save) {
            saveGame();
        }
    }
}
