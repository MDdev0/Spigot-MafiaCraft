package mddev0.mafiacraft.abilities;

import mddev0.mafiacraft.MafiaCraft;
import mddev0.mafiacraft.util.MafiaPlayer;
import org.bukkit.ChatColor;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.inventory.meta.ItemMeta;

public final class Forgery implements Listener {

    private final MafiaCraft plugin;

    public Forgery(MafiaCraft plugin) {
        this.plugin = plugin;
    }

    @SuppressWarnings("unused")
    @EventHandler
    public void onItemPickup(EntityPickupItemEvent pickup) {
        if (!plugin.getActive()) return; // DO NOTHING IF NOT ACTIVE!
        ItemMeta meta = pickup.getItem().getItemStack().getItemMeta();
        String itemName = (meta != null) ? ChatColor.stripColor(meta.getDisplayName()) : null;
        if (pickup.getEntityType() == EntityType.PLAYER && itemName != null &&
                itemName.trim().equalsIgnoreCase(plugin.getConfig().getString("forgeItemName"))) {
            // Check that thrower has ability
            MafiaPlayer thrower = plugin.getLivingPlayers().get(pickup.getItem().getThrower());
            MafiaPlayer toFrame = plugin.getLivingPlayers().get(pickup.getEntity().getUniqueId());
            if (thrower != null && thrower.getRole().hasAbility(Ability.FORGERY) && toFrame != null)
                toFrame.setFramed(); // Frame player
        }
    }
}
