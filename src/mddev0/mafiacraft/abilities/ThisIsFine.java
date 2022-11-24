package mddev0.mafiacraft.abilities;

import mddev0.mafiacraft.MafiaCraft;
import mddev0.mafiacraft.util.MafiaPlayer;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

public final class ThisIsFine implements Listener {

    private final MafiaCraft plugin;

    public ThisIsFine(MafiaCraft plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onFireDamage(EntityDamageEvent damage) {
        if (damage.getEntity().getType() == EntityType.PLAYER) {
            MafiaPlayer p = plugin.getPlayerList().get(damage.getEntity().getUniqueId());
            if (p.getRole().hasAbility(Ability.THIS_IS_FINE)) {
                if (switch (damage.getCause()) {
                    case FIRE,LAVA,FIRE_TICK,HOT_FLOOR -> true;
                    default -> false;
                }) {
                    // Has ability and damage type is right
                    // Damage reduced by 75%, so 25% is taken
                    damage.setDamage(damage.getDamage() * 0.25);
                }
            }
        }
    }
}
