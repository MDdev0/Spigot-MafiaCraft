package mddev0.mafiacraft.abilities;

import mddev0.mafiacraft.MafiaCraft;
import mddev0.mafiacraft.player.MafiaPlayer;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPotionEffectEvent;
import org.bukkit.potion.PotionEffectType;

import java.util.Objects;

public final class ClearSight implements Listener {

    private final MafiaCraft plugin;

    public ClearSight(MafiaCraft plugin) {
        this.plugin = plugin;
    }

    @SuppressWarnings("unused")
    @EventHandler
    public void onEffectApplied(EntityPotionEffectEvent potion) {
        if (!plugin.getActive()) return; // DO NOTHING IF NOT ACTIVE!
        MafiaPlayer affected = plugin.getPlayerList().get(potion.getEntity().getUniqueId());
        if (potion.getEntity().getType() == EntityType.PLAYER && affected != null &&
                affected.getRole().getAbilities().contains(Ability.CLEAR_SIGHT) && (
                        Objects.requireNonNull(potion.getNewEffect()).getType() == PotionEffectType.BLINDNESS ||
                        Objects.requireNonNull(potion.getNewEffect()).getType() == PotionEffectType.DARKNESS
                ))
            potion.setCancelled(true); // Cancel potion effect if eligible player is being blinded
    }
}
