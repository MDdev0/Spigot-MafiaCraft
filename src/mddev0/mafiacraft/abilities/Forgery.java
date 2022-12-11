package mddev0.mafiacraft.abilities;

import mddev0.mafiacraft.MafiaCraft;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;

import java.util.Objects;

public final class Forgery implements Listener {

    private final MafiaCraft plugin;

    public Forgery(MafiaCraft plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onItemPickup(EntityPickupItemEvent pickup) {
        if (!plugin.getActive()) return; // DO NOTHING IF NOT ACTIVE!
        if (pickup.getEntityType() == EntityType.PLAYER &&
                Objects.equals(pickup.getItem().getCustomName(), plugin.getConfig().getString("forgeItemName"))) {
            // Check that thrower has ability
            if (plugin.getPlayerList().get(pickup.getItem().getThrower()).getRole().hasAbility(Ability.FORGERY))
                plugin.getPlayerList().get(pickup.getEntity().getUniqueId()).setFramed(); // Frame player
        }
    }
}
