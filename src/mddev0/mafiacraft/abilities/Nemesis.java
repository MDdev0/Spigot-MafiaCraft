package mddev0.mafiacraft.abilities;

import mddev0.mafiacraft.MafiaCraft;


import mddev0.mafiacraft.player.MafiaPlayer;
import mddev0.mafiacraft.player.RoleData;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

public final class Nemesis implements Listener {

    private final MafiaCraft plugin;

    public Nemesis(MafiaCraft plugin) {
        this.plugin = plugin;
    }

    @SuppressWarnings("unused")
    @EventHandler
    public void onDamageTaken(EntityDamageByEntityEvent hit) {
        if (!plugin.getActive()) return; // DO NOTHING IF NOT ACTIVE!
        MafiaPlayer damaged = plugin.getLivingPlayers().get(hit.getEntity().getUniqueId());
        if (damaged != null && damaged.getRole().getAbilities().contains(Ability.NEMESIS) &&
                (Boolean)damaged.getRoleData().getData(RoleData.DataType.WEREWOLF_TRANSFORM) && hit.getDamager() instanceof LivingEntity ) {
            // Has nemesis ability and is transformed
            // null check
            if (((LivingEntity) hit.getDamager()).getEquipment() == null) return;
            // get hand item
            ItemStack item = ((LivingEntity) hit.getDamager()).getEquipment().getItemInMainHand();
            // do nothing if hand item is empty
            if (switch (item.getType()) {
                // Damage source
                case IRON_AXE,IRON_HOE,IRON_PICKAXE,IRON_SHOVEL,IRON_SWORD -> true;
                default -> false;
            }) {
                // add one heart to damage dealt
                hit.setDamage(hit.getDamage() + 2.0);
            }
        }
    }
}
