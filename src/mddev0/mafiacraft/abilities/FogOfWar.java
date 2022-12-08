package mddev0.mafiacraft.abilities;

import mddev0.mafiacraft.MafiaCraft;
import mddev0.mafiacraft.roles.Sorcerer;
import mddev0.mafiacraft.util.MafiaPlayer;
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

    @EventHandler
    public void onItemSwing(PlayerInteractEvent click) {
        if (click.getAction().equals(Action.LEFT_CLICK_AIR)) {
            if (click.getItem() != null && click.getItem().getType() == Material.ENCHANTED_BOOK) {
                ItemStack book = click.getItem();
                if (Objects.requireNonNull(book.getItemMeta()).isUnbreakable()) {
                    MafiaPlayer sorcerer = plugin.getLivingPlayers().get(click.getPlayer().getUniqueId());
                    if (sorcerer.getRole() instanceof Sorcerer && ((Sorcerer) sorcerer.getRole()).getSelected() == Ability.FOG_OF_WAR) { // dirty check but it works
                        if (click.getPlayer().getLevel() < 10) { //TODO: CONFIG
                            click.getPlayer().sendMessage(ChatColor.RED + "You don't have enough levels to use this spell!");
                        } else {
                            // Activate Spell
                            for (Entity target : click.getPlayer().getNearbyEntities(30,30,30)) { //TODO: CONFIG
                                if (!(target instanceof Player)) continue;
                                if (target == click.getPlayer()) continue;
                                ((Player) target).addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 600, 0, false, false, true));
                                plugin.getServer().getWorlds().get(0).spawnParticle(Particle.SQUID_INK, target.getLocation().add(0,1,0), 10, 1, 1, 1);
                            }
                            // apply effect
                            click.getPlayer().setLevel(click.getPlayer().getLevel() - 10); // TODO: Config
                            click.getPlayer().sendMessage(ChatColor.GREEN + "You used " + ChatColor.LIGHT_PURPLE + "Fog of War");
                            sorcerer.setUnholy();
                            plugin.getServer().getWorlds().get(0).spawnParticle(Particle.SPELL_WITCH, click.getPlayer().getLocation().add(0,1,0), 10, 1, 1, 1);
                        }
                    }
                }
            }
        }
    }
}
