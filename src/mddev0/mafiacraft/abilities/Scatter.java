package mddev0.mafiacraft.abilities;

import mddev0.mafiacraft.MafiaCraft;
import mddev0.mafiacraft.roles.Sorcerer;
import mddev0.mafiacraft.util.MafiaPlayer;
import mddev0.mafiacraft.util.SpyglassUtil;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Objects;

public class Scatter implements Listener {

    private final MafiaCraft plugin;

    public Scatter(MafiaCraft plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onItemSwing(PlayerInteractEvent click) {
        if (click.getAction().equals(Action.LEFT_CLICK_AIR)) {
            if (click.getItem() != null && click.getItem().getType() == Material.ENCHANTED_BOOK) {
                ItemStack book = click.getItem();
                if (Objects.requireNonNull(book.getItemMeta()).isUnbreakable()) {
                    MafiaPlayer sorcerer = plugin.getLivingPlayers().get(click.getPlayer().getUniqueId());
                    if (sorcerer.getRole() instanceof Sorcerer && ((Sorcerer) sorcerer.getRole()).getSelected() == Ability.SCATTER) { // dirty check but it works
                        if (click.getPlayer().getLevel() < 5) { //TODO: CONFIG
                            click.getPlayer().sendMessage(ChatColor.RED + "You don't have enough levels to use this spell!");
                        } else {
                            // Activate Spell
                            Player p = null;
                            for (Player target : plugin.getServer().getOnlinePlayers()) {
                                if (target == click.getPlayer()) continue;
                                if (SpyglassUtil.lookingAtPlayer(click.getPlayer(), target)) {
                                    p = target;
                                    break;
                                }
                            }
                            if (p == null) p = click.getPlayer();
                            // apply effect
                            p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 400, 3, false, false, true));
                            click.getPlayer().setLevel(click.getPlayer().getLevel() - 5); // TODO: Config
                            click.getPlayer().sendMessage(ChatColor.GREEN + "You used " + ChatColor.LIGHT_PURPLE + "Scatter" +
                                    ChatColor.GREEN + " on " + ChatColor.AQUA + p.getName());
                            sorcerer.setUnholy();
                            plugin.getServer().getWorlds().get(0).spawnParticle(Particle.SPELL_WITCH, click.getPlayer().getLocation().add(0,1,0), 10, 1, 1, 1);
                            plugin.getServer().getWorlds().get(0).spawnParticle(Particle.CLOUD, p.getLocation().add(0,1,0), 10, 1, 1, 1);
                        }
                    }
                }
            }
        }
    }
}