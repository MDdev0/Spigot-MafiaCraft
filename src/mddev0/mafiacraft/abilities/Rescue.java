package mddev0.mafiacraft.abilities;

import mddev0.mafiacraft.MafiaCraft;
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
                    if (e.getType() == EntityType.PLAYER &&
                            plugin.getLivingPlayers().get(e.getUniqueId()).getRole().getAbilities().contains(Ability.RESCUE)) {
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
                            long waitUntil = plugin.getServer().getWorlds().get(0).getFullTime() + 24000L;
                            waitUntil = waitUntil - (waitUntil % 24000); // Cooldown ends at dawn
                            plugin.getLivingPlayers().get(e.getUniqueId()).getCooldowns().startCooldown(Ability.RESCUE, waitUntil);
                        }
                    }
                }
            }
        }
    }
}
