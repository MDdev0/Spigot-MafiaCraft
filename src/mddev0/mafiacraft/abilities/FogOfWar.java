package mddev0.mafiacraft.abilities;

import mddev0.mafiacraft.MafiaCraft;
import mddev0.mafiacraft.player.*;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Objects;

public final class FogOfWar implements Listener {

    private final MafiaCraft plugin;

    public FogOfWar(MafiaCraft plugin) {
        this.plugin = plugin;
    }

    @SuppressWarnings("unused")
    @EventHandler
    public void onItemSwing(PlayerInteractEvent click) {
        if (click.getAction().equals(Action.LEFT_CLICK_AIR)) {
            if (!plugin.getActive()) return; // DO NOTHING IF NOT ACTIVE!
            if (click.getItem() != null && click.getItem().getType() == Material.ENCHANTED_BOOK) {
                ItemStack book = click.getItem();
                if (Objects.requireNonNull(book.getItemMeta()).isUnbreakable()) {
                    MafiaPlayer sorcerer = plugin.getLivingPlayers().get(click.getPlayer().getUniqueId());
                    if (sorcerer.getRole() == Role.SORCERER && sorcerer.getRoleData().getData(RoleData.DataType.SORCERER_SELECTED) == Ability.FOG_OF_WAR) {
                        if (click.getPlayer().getLevel() < plugin.getConfig().getInt("fogOfWarCost")) {
                            click.getPlayer().sendMessage(ChatColor.RED + "You don't have enough levels to use this spell!");
                        } else {
                            // Activate Spell
                            int range = plugin.getConfig().getInt("fogOfWarRange");
                            for (Entity target : click.getPlayer().getNearbyEntities(range,range,range)) {
                                if (!(target instanceof Player)) continue;
                                if (target == click.getPlayer()) continue;
                                ((Player) target).addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 600, 0, false, false, true));
                                plugin.getServer().getWorlds().get(0).spawnParticle(Particle.SQUID_INK, target.getLocation().add(0,1,0), 10, 1, 1, 1);
                            }
                            // apply effect
                            click.getPlayer().setLevel(click.getPlayer().getLevel() - plugin.getConfig().getInt("fogOfWarCost"));
                            click.getPlayer().sendMessage(ChatColor.GREEN + "You used " + ChatColor.LIGHT_PURPLE + "Fog of War");
                            sorcerer.getStatus().startStatus(StatusData.Status.UNHOLY, 48000L); // Two days of unholy
                            plugin.getServer().getWorlds().get(0).spawnParticle(Particle.SPELL_WITCH, click.getPlayer().getLocation().add(0,1,0), 10, 1, 1, 1);
                        }
                    }
                }
            }
        }
    }
}
