package mddev0.mafiacraft.abilities;

import mddev0.mafiacraft.MafiaCraft;
import mddev0.mafiacraft.player.MafiaPlayer;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

public final class DodgeRoll implements Listener {

    private final MafiaCraft plugin;

    public DodgeRoll(MafiaCraft plugin) {
        this.plugin = plugin;
    }

    @SuppressWarnings("unused")
    @EventHandler
    public void onFallOrExplodeDamage(EntityDamageEvent damage) {
        if (!plugin.getActive()) return; // DO NOTHING IF NOT ACTIVE!
        if (damage.getEntity().getType() == EntityType.PLAYER) {
            MafiaPlayer p = plugin.getLivingPlayers().get(damage.getEntity().getUniqueId());
            if (p != null && p.getRole().getAbilities().contains(Ability.DODGE_ROLL)) {
                if (switch (damage.getCause()) {
                    case FALL,ENTITY_EXPLOSION,BLOCK_EXPLOSION -> true;
                    default -> false;
                }) {
                    // Has ability and damage type is right
                    // Damage is reduced by 75%, so 25% is taken
                    damage.setDamage(damage.getDamage() * 0.25);
                }
            }
        }
    }
}
