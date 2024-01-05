package mddev0.mafiacraft.abilities;

import mddev0.mafiacraft.MafiaCraft;
import mddev0.mafiacraft.player.MafiaPlayer;
import org.bukkit.ChatColor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.List;

public final class Rescue implements Listener {

    private final MafiaCraft plugin;

    public Rescue(MafiaCraft plugin) {
        this.plugin = plugin;
    }

    @SuppressWarnings("unused")
    @EventHandler
    public void onPlayerDamaged(EntityDamageEvent damage) {
        if (!plugin.getActive()) return; // DO NOTHING IF NOT ACTIVE!
        if (damage.getEntityType() == EntityType.PLAYER) {
            // Damaged entity is a player
            Player damaged = (Player) damage.getEntity();
            if ((damaged.getHealth() - damage.getFinalDamage()) < 1.0) {
                // Player would otherwise die
                double range = plugin.getConfig().getDouble("rescueRange");
                List<Entity> nearbyEntities = damaged.getNearbyEntities(range,range,range);
                for (Entity e : nearbyEntities) {
                    MafiaPlayer rescuer = plugin.getLivingPlayers().get(e.getUniqueId());
                    if (e.getType() == EntityType.PLAYER &&
                            rescuer.getRole().getAbilities().contains(Ability.RESCUE) &&
                            !rescuer.getCooldowns().isOnCooldown(Ability.RESCUE)) {
                        // a player nearby has the rescue ability
                        if (e.getUniqueId() != damaged.getUniqueId()) { // Can't dataMap yourself
                            // the player is able to be saved
                            // Do the saving
                            damage.setCancelled(true);
                            damaged.setHealth(1.0);
                            damaged.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 300,2,false,true,true));
                            damaged.sendMessage(ChatColor.AQUA + "You were saved from death!");
                            // Handle the rescuer
                            e.sendMessage(ChatColor.AQUA + "You saved " + ChatColor.GREEN + damaged.getName() + ChatColor.AQUA + " from death!");
                            long waitUntil = plugin.getWorldFullTime() + 24000L * 3;
                            waitUntil = waitUntil - (waitUntil % 24000); // Cooldown ends at dawn
                            rescuer.getCooldowns().startCooldown(Ability.RESCUE, waitUntil);
                        }
                    }
                }
            }
        }
    }
}
