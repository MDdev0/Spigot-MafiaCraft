package mddev0.mafiacraft.abilities;

import mddev0.mafiacraft.MafiaCraft;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPotionEffectEvent;
import org.bukkit.potion.PotionEffectType;

import java.util.Objects;

public final class ClearSight implements Listener {

    private MafiaCraft plugin;

    public ClearSight(MafiaCraft plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onBlindnessApplied(EntityPotionEffectEvent potion) {
        if (potion.getEntity().getType() == EntityType.PLAYER &&
                plugin.getPlayerList().get(potion.getEntity().getUniqueId()).getRole().hasAbility(Ability.CLEAR_SIGHT) &&
                Objects.requireNonNull(potion.getNewEffect()).getType() == PotionEffectType.BLINDNESS)
            potion.setCancelled(true); // Cancel potion effect if eligible player is being blinded
    }
}
