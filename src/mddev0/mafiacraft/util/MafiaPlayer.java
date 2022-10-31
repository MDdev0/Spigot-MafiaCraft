package mddev0.mafiacraft.util;

import mddev0.mafiacraft.MafiaCraft;
import mddev0.mafiacraft.roles.Role;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.UUID;

public class MafiaPlayer {

    private final MafiaCraft plugin;
    private UUID uuid;
    private Role role;
    private Role originalRole;
    private int attackerTicks;
    private final PlayerTicker onTick;

    public MafiaPlayer(MafiaCraft plugin) {
        this.plugin = plugin;
        onTick = new PlayerTicker();
        onTick.runTaskTimer(plugin,0,1); // runs every tick
    }

    public Role getRole() {
        return role;
    }

    public Role getOriginalRole() {
        return originalRole;
    }

    public void changeRole(Role newRole) {
        role = newRole;
    }

    public void changeOriginalRole(Role newRole) {
        originalRole = newRole;
    }

    public void setAttacker() {
        attackerTicks = plugin.getConfig().getInt("attackDuration");
    }

    public void tickAttacker() {
        attackerTicks = (attackerTicks>0) ? attackerTicks-1 : 0;
    }

    public boolean isAttacker() {
        return attackerTicks > 0;
    }

    private class PlayerTicker extends BukkitRunnable {
        @Override
        public void run() {
            attackerTicks--;
            getRole().tickCooldowns();
        }
    }

    public void cancelTasks() {
        onTick.cancel();
    }
}
