package mddev0.mafiacraft.abilities;

import mddev0.mafiacraft.MafiaCraft;


import mddev0.mafiacraft.player.MafiaPlayer;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

public final class Staked implements Listener {

    private final MafiaCraft plugin;

    public Staked(MafiaCraft plugin) {
        this.plugin = plugin;
    }

    @SuppressWarnings("unused")
    @EventHandler
    public void onDamageTaken(EntityDamageByEntityEvent hit) {
        if (!plugin.getActive()) return; // DO NOTHING IF NOT ACTIVE!
        MafiaPlayer damaged = plugin.getLivingPlayers().get(hit.getEntity().getUniqueId());
        if (damaged != null && damaged.getRole().getAbilities().contains(Ability.STAKED) && hit.getDamager() instanceof LivingEntity ) {
            // null check
            if (((LivingEntity) hit.getDamager()).getEquipment() == null) return;
            // get hand item
            ItemStack item = ((LivingEntity) hit.getDamager()).getEquipment().getItemInMainHand();
            // do nothing if hand item is empty
            if (switch (item.getType()) {
                // Damage source
                case WOODEN_AXE,WOODEN_HOE,WOODEN_PICKAXE,WOODEN_SHOVEL,WOODEN_SWORD,STICK -> true;
                default -> false;
            }) {
                // add one heart to damage dealt
                hit.setDamage(hit.getDamage() + 5.0);
            }
        }
    }
}
