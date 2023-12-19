package mddev0.mafiacraft.abilities;

import mddev0.mafiacraft.MafiaCraft;
import mddev0.mafiacraft.gui.ReanimationGUI;
import mddev0.mafiacraft.player.MafiaPlayer;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityCombustByBlockEvent;

import java.util.Objects;

// XXX: There is probably a bug somewhere in the Reanimation ability. We'll see during testing
public final class Reanimation implements Listener {

    private final MafiaCraft plugin;

    public Reanimation(MafiaCraft plugin) {
        this.plugin = plugin;
    }

    @SuppressWarnings("unused")
    @EventHandler
    public void onItemDestroy(EntityCombustByBlockEvent damage) {
        if (!plugin.getActive()) return; // DO NOTHING IF NOT ACTIVE!
        if (damage.getEntityType() == EntityType.DROPPED_ITEM && Objects.requireNonNull(damage.getCombuster()).getType() == Material.SOUL_FIRE) {
            Item item = (Item) damage.getEntity();
            if (item.getItemStack().getType() == Material.getMaterial(Objects.requireNonNull(plugin.getConfig().getString("reanimateSacrifice")))) {
                // Valid item, check player and cooldown
                if (item.getThrower() == null) return;
                MafiaPlayer thrower = plugin.getLivingPlayers().get(item.getThrower());
                if (thrower.getRole().getAbilities().contains(Ability.REANIMATION) && !thrower.getCooldowns().isOnCooldown(Ability.REANIMATION)) {
                    item.remove();
                    ReanimationGUI gui = new ReanimationGUI(plugin);
                    gui.open(Objects.requireNonNull(Bukkit.getPlayer(item.getThrower())));
                }
            }
        }
    }
}
