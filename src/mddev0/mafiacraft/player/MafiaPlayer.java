package mddev0.mafiacraft.player;

import mddev0.mafiacraft.MafiaCraft;
import mddev0.mafiacraft.abilities.Ability;
import mddev0.mafiacraft.util.SpyglassUtil;
import org.bukkit.Bukkit;

import java.util.*;

public class MafiaPlayer {
    private final MafiaCraft plugin;

    // Player Identification
    private final UUID uuid;
    private boolean living;
    private Role role;
    private RoleData roleData;
    private final Role originalRole;
    private final RoleData originalRoleData;
    // Flags
    private final SpyglassUtil spyglass;
    private final CooldownData cooldowns;
    private final StatusData statuses;

    // For creating during gameplay
    public MafiaPlayer(MafiaCraft plugin, UUID id, Role startingRole) {
        this.plugin = plugin;
        this.spyglass = new SpyglassUtil(plugin, Bukkit.getOfflinePlayer(id));
        this.spyglass.runTaskTimer(plugin,0,1); // runs every tick
        this.uuid = id;
        this.living = true;
        this.role = this.originalRole = startingRole;
        this.roleData = this.originalRoleData = new RoleData();
        this.statuses = new StatusData(plugin);
        this.cooldowns = new CooldownData(plugin);

        // If this role has added attributes, set defaults if needed
        switch (role)  {
            case JESTER -> roleData.setData(RoleData.DataType.JESTER_ABILITY_USED, false);
            case SORCERER -> roleData.setData(RoleData.DataType.SORCERER_SELECTED, Ability.SPELL_BOOK);
            case WEREWOLF -> {
                roleData.setData(RoleData.DataType.WEREWOLF_TRANSFORM, false);
                roleData.setData(RoleData.DataType.WEREWOLF_KILLS, 0);
            }
        }
    }

    // XXX: BROKEN
    // For creating from offline dataMap
    public MafiaPlayer(MafiaCraft plugin, PlayerSaveData dataRecord) {
        this.plugin = plugin;
        spyglass = new SpyglassUtil(plugin, Bukkit.getOfflinePlayer(dataRecord.uuid()));
        spyglass.runTaskTimer(plugin,0,1); // runs every tick
        this.uuid = dataRecord.uuid();
        this.living = dataRecord.living();
        this.role = dataRecord.role();
        this.roleData = new RoleData(dataRecord.roleData());
        this.originalRole = dataRecord.originalRole();
        this.originalRoleData = new RoleData(dataRecord.originalRoleData());
        this.statuses = new StatusData(plugin, dataRecord.status());
        this.cooldowns = new CooldownData(plugin, dataRecord.cooldowns());
    }

    public UUID getID() {
        return uuid;
    }

    // Living Info
    public boolean isLiving() {
        return living;
    }

    public void makeDead() {
        living = false;
    }

    public void makeAlive() {
        living = true;
    }

    // Role Info
    public Role getRole() {
        return role;
    }

    public RoleData getRoleData() {
        return roleData;
    }

    public Role getOriginalRole() {
        return originalRole;
    }

    public RoleData getOriginalRoleData() {
        return originalRoleData;
    }

    /**
     * For changing roles mid-game
     */
    public void changeRole(Role newRole) {
        role = newRole;
        roleData = new RoleData(); // Does not need to be initialized currently, as this will only be called for Vampires
    }

    public void resetRole() {
        role = originalRole;
        roleData = originalRoleData;
    }

    public CooldownData getCooldowns() {
        return cooldowns;
    }

    public StatusData getStatus() {
        return statuses;
    }

    public SpyglassUtil getSpyglass() {
        return spyglass;
    }

    /**
     * Used for saving while server is offline
     */
    public record PlayerSaveData(UUID uuid, boolean living, Role role, RoleData.RoleDataSave roleData,
                                 Role originalRole, RoleData.RoleDataSave originalRoleData,
                                 CooldownData.CooldownDataSave cooldowns, StatusData.StatusDataSave status) {}
    public PlayerSaveData getSaveData() {
        return new PlayerSaveData(uuid, living, role, roleData.getDataSave(), originalRole,
                originalRoleData.getDataSave(), cooldowns.getDataSave(), statuses.getDataSave());
    }
}
