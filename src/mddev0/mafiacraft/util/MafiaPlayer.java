package mddev0.mafiacraft.util;

import mddev0.mafiacraft.MafiaCraft;
import mddev0.mafiacraft.abilities.Ability;
import mddev0.mafiacraft.roles.Role;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class MafiaPlayer {

    private final MafiaCraft plugin;
    // Player Identification
    private final UUID uuid;
    private boolean living;
    private Role role;
    private final Role originalRole;
    // Ticked Flags
    private final Map<Ability, CooldownLength> cooldowns = new HashMap<>();
    private int attackerTicks = 0;
    private boolean framed = false;
    private long unholyTicks = 0L;
    private final PlayerTicker onTick;
    private final SpyglassUtil spyglass;

    public MafiaPlayer(MafiaCraft plugin, UUID id, Role startingRole) {
        this.plugin = plugin;
        onTick = new PlayerTicker();
        onTick.runTaskTimer(plugin,0,1); // runs every tick
        spyglass = new SpyglassUtil(plugin, Bukkit.getOfflinePlayer(id));
        spyglass.runTaskTimer(plugin,0,1); // runs every tick
        uuid = id;
        living = true;
        role = originalRole = startingRole;
    }

    public MafiaPlayer(MafiaCraft plugin, PlayerData dataRecord) {
        this.plugin = plugin;
        onTick = new PlayerTicker();
        onTick.runTaskTimer(plugin,0,1); // runs every tick
        spyglass = new SpyglassUtil(plugin, Bukkit.getOfflinePlayer(dataRecord.uuid()));
        spyglass.runTaskTimer(plugin,0,1); // runs every tick
        this.uuid = dataRecord.uuid();
        this.living = dataRecord.living();
        this.role = dataRecord.role();
        this.originalRole = dataRecord.originalRole();
        for (Map.Entry<Ability, CooldownData> cooldown : dataRecord.cooldowns().entrySet())
            this.cooldowns.put(cooldown.getKey(), new CooldownLength(cooldown.getValue().dayTime(), cooldown.getValue().days()));
        this.framed = dataRecord.framed();
        this.unholyTicks = dataRecord.unholyTicks();
        this.attackerTicks = dataRecord.attackerTicks();
    }

    public UUID getID() {
        return uuid;
    }

    public void cancelTasks() {
        onTick.cancel();
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

    public Role getOriginalRole() {
        return originalRole;
    }

    public void changeRole(Role newRole) {
        role = newRole;
    }

    // Investigatory Helpers
    public boolean isMafiaSuspect() {
        if (role.getWinCond() == Role.WinCondition.MAFIA) // Mafia case
            return !(role.getAbilities().contains(Ability.CHARISMA));
        else return framed; // Not mafia case
    }

    public boolean isUnholySuspect() {
        return (unholyTicks > 0);
    }

    // Attack and Framed effect cooldowns
    public void setAttacker() {
        attackerTicks = plugin.getConfig().getInt("attackDuration");
    }

    public boolean isNotAttacker() {
        return attackerTicks <= 0;
    }

    public void setFramed() {
        framed = true;
    }

    public void setUnholy() {
        unholyTicks = 24000L * 2;
    }

    // Ability cooldowns
    /** New cooldown for specified days, ending at specified day time
     *
     * @param a Ability to put on cooldown
     * @param dayTime time of day cooldown will end
     * @param days Number of days cooldown will last
     */
    public void startCooldown(Ability a, long dayTime, int days) {
        cooldowns.put(a, new CooldownLength(dayTime, days));
    }
    /** New cooldown ending at specified day time
     *
     * @param a Ability to put on cooldown
     * @param dayTime time of day cooldown will end
     */
    public void startCooldown(Ability a, long dayTime) {
        cooldowns.put(a, new CooldownLength(dayTime));
    }

    public boolean onCooldown(Ability a) {
        return cooldowns.containsKey(a);
    }

    public SpyglassUtil getSpyglass() {
        return spyglass;
    }

    // Game Save Utility
    public PlayerData getDataRecord() {
        Map<Ability, CooldownData> cools = new HashMap<>();
        for (Map.Entry<Ability, CooldownLength> cooldown : cooldowns.entrySet())
            cools.put(cooldown.getKey(), cooldown.getValue().getDataRecord());
        return new PlayerData(uuid, living, role, originalRole, cools, framed, unholyTicks, attackerTicks);
    }

    /**
     * Used for saving while server is offline
     * @param uuid
     * @param living
     * @param role
     * @param originalRole
     * @param cooldowns
     * @param framed
     * @param unholyTicks
     * @param attackerTicks
     */
    public record PlayerData (UUID uuid, boolean living, Role role, Role originalRole, Map<Ability, CooldownData> cooldowns, boolean framed, long unholyTicks, int attackerTicks) {}
    /**
     * Used for saving while server is offline
     * @param dayTime
     * @param days
     */
    record CooldownData (long dayTime, int days) {}

    // Support Classes
    private class PlayerTicker extends BukkitRunnable {
        @Override
        public void run() {
            // Decrease attacker ticks
            attackerTicks = (attackerTicks>0) ? attackerTicks-1 : 0;
            // Framed players reset at sunrise
            if (framed && plugin.getServer().getWorlds().get(0).getTime() == 0L)
                framed = false;
            // Decrease unholy marked ticks
            unholyTicks = (unholyTicks>0) ? unholyTicks-1 : 0;
            // Cooldowns
            for (Map.Entry<Ability, CooldownLength> a : cooldowns.entrySet()) {
                a.getValue().check();
                if (a.getValue().expired()) cooldowns.remove(a.getKey());
            }
        }
    }

    private static class CooldownLength {
        final long dayTime;
        int days;
        CooldownLength(long dayTime, int days) {
            this.dayTime = dayTime;
            this.days = days;
        }
        CooldownLength(long dayTime) {
            this.dayTime = dayTime;
            this.days = 0;
        }
        void check() {
            if (days < 0) return; // Do nothing
            if (Bukkit.getWorlds().get(0).getTime() == dayTime) {
                days--;
            }
        }
        boolean expired() {
            return (days < 0);
        }
        CooldownData getDataRecord() {
            return new CooldownData(dayTime, days);
        }
    }
}
