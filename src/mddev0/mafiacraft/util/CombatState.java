package mddev0.mafiacraft.util;

import mddev0.mafiacraft.MafiaCraft;

import mddev0.mafiacraft.player.MafiaPlayer;
import mddev0.mafiacraft.player.StatusData;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import javax.annotation.Nullable;

public class CombatState implements Listener {

    private final MafiaCraft plugin;

    public CombatState(MafiaCraft plugin) {
        this.plugin = plugin;
    }

    @SuppressWarnings("unused")
    @EventHandler (priority = EventPriority.HIGH) // execute AFTER normal damage handlers
    public void onAttackPlayer(EntityDamageByEntityEvent damage) {
        if (damage.getEntityType() == EntityType.PLAYER) {
            // Know the damaged entity is a player
            Player attacker = findAttackingPlayer(damage);
            if (attacker != null) {
                MafiaPlayer attackerMP = plugin.getPlayerList().get(attacker.getUniqueId());
                if (attackerMP != null)
                    attackerMP.getStatus().startStatus(StatusData.Status.IN_COMBAT, plugin.getWorldFullTime() + 600L);
            }
            // If null do nothing
        }
    }

    /**
     * Finds which Player attacked using the definition of Attacked for MafiaCraft
     * @param damage - event which caused damage
     * @return player who made the Attack, null if not an Attack by a Player
     */
    @Nullable
    public static Player findAttackingPlayer(EntityDamageByEntityEvent damage) {
        if (damage.getDamager().equals(damage.getEntity())) return null;
        // Return null if attacker and attacked are the same
        return switch (damage.getDamager().getType()) {
            case PLAYER:
                // Know the attacking entity is a player
                // Nested switching, fun! Thank god for enhanced switch.
                // Is this a horrible way of doing this? Probably. But, it's the best I can think of for now.
                switch (((Player) damage.getDamager()).getInventory().getItemInMainHand().getType()) { // Get main hand of attacker
                    // These are the melee types that trigger an attack
                    case WOODEN_SWORD, WOODEN_AXE, STONE_SWORD, STONE_AXE, IRON_SWORD, IRON_AXE,
                            GOLDEN_SWORD, GOLDEN_AXE, DIAMOND_SWORD, DIAMOND_AXE, NETHERITE_SWORD, NETHERITE_AXE, TRIDENT -> {
                        yield (Player) damage.getDamager();
                    }
                    default -> {
                        yield null;
                    }
                }
            case ARROW, TRIDENT:
                // This is a lot easier, just see who shot it
                if (((AbstractArrow) damage.getDamager()).getShooter() instanceof Player) {
                    yield (Player) ((AbstractArrow) damage.getDamager()).getShooter();
                } else yield null;
            default:
                yield null;
        };
    }
}
