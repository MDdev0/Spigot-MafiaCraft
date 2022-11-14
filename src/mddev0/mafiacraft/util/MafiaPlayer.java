package mddev0.mafiacraft.util;

import mddev0.mafiacraft.MafiaCraft;
import mddev0.mafiacraft.abilities.Ability;
import mddev0.mafiacraft.roles.Role;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class MafiaPlayer {

    private final MafiaCraft plugin;
    // Player Identification
    private final UUID uuid;
    private boolean living;
    private Role role;
    private final Role originalRole;
    // Ticked Flags
    private final Map<Ability, CooldownLength> cooldowns = new HashMap<>();
    private int attackerTicks;
    private boolean framed;
    private final PlayerTicker onTick;

    public MafiaPlayer(MafiaCraft plugin, UUID id, Role startingRole) {
        this.plugin = plugin;
        onTick = new PlayerTicker();
        onTick.runTaskTimer(plugin,0,1); // runs every tick
        uuid = id;
        living = true;
        role = originalRole = startingRole;
    }

    public void cancelTasks() {
        onTick.cancel();
    }

    // Living Info
    public boolean isLiving() {
        return living;
    }

    public void makeAlive() {
        living = true;
        // TODO: Logic to reanimate players
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
        return false; // TODO: Implement
    }

    // Attack and Framed effect cooldowns
    public void setAttacker() {
        attackerTicks = plugin.getConfig().getInt("attackDuration");
    }

    public boolean isAttacker() {
        return attackerTicks > 0;
    }

    public void setFramed() {
        framed = true;
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

    /** New cooldown for specified days, starting at current day time
     *
     * @param a Ability to put on cooldown
     * @param days Number of days cooldown will last
     */
    public void startCooldown(Ability a, int days) {
        cooldowns.put(a, new CooldownLength(plugin.getServer().getWorlds().get(0).getTime(), days));
    }

    public boolean onCooldown(Ability a) {
        return cooldowns.containsKey(a);
    }

    // Support Classes
    private class PlayerTicker extends BukkitRunnable {
        @Override
        public void run() {
            attackerTicks = (attackerTicks>0) ? attackerTicks-1 : 0;
            // Framed players reset at sunrise
            if (framed && plugin.getServer().getWorlds().get(0).getTime() == 0L)
                framed = false;
            // Cooldowns
            for (Map.Entry<Ability, CooldownLength> a : cooldowns.entrySet()) {
                a.getValue().decrement();
                if (a.getValue().expired()) cooldowns.remove(a.getKey());
            }
        }
    }

    private class CooldownLength {
        long dayTime;
        int days;
        CooldownLength(long dayTime, int days) {
            this.dayTime = dayTime;
            this.days = days;
        }
        CooldownLength(long dayTime) {
            this.dayTime = dayTime;
            this.days = 0;
        }
        void decrement() {
            if (days == 0 && dayTime == 0) return; // Do nothing
            dayTime--;
            if (dayTime < 0) {
                days--;
                dayTime += 24000;
            }
        }
        boolean expired() {
            return (days == 0 && dayTime == 0);
        }
    }
}
