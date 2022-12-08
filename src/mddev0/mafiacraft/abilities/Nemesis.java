package mddev0.mafiacraft.abilities;

import mddev0.mafiacraft.MafiaCraft;


import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntity;

public final class Nemesis implements Listener {

    private final MafiaCraft plugin;

    public Marksman(MafiaCraft plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onDamageTaken(EntityDamageByEntity hit) {
      
        if (plugin.getPlayerList().get(hit.getEntity().getUniqueId()).getRole().hasAbility(Ability.NEMESIS) && hit.getDamager() instanceof LivingEntity ) {
          
          
          ItemStack item = ((LivingEntity) hit.getDamager()).getEquipment().getItemInMainHand();
          
          if (switch (item.getType()) {
            
            case IRON_AXE,IRON_HOE,IRON_PICKAXE,IRON_SHOVEL,IRON_SWORD -> true;
            default -> false;
            }) {
            hit.setDamage(damage.getDamage() + 2.0)
             
            }
          
            
          
        }
    }
}
