package mddev0.mafiacraft.abilities;

import mddev0.mafiacraft.MafiaCraft;
import mddev0.mafiacraft.player.*;
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

public final class Vanish implements Listener {

    private final MafiaCraft plugin;

    public Vanish(MafiaCraft plugin) {
        this.plugin = plugin;
    }

    @SuppressWarnings("unused")
    @EventHandler
    public void onItemSwing(PlayerInteractEvent click) {
        if (!plugin.getActive()) return; // DO NOTHING IF NOT ACTIVE!
        if (click.getAction().equals(Action.LEFT_CLICK_AIR)) {
            if (click.getItem() != null && click.getItem().getType() == Material.ENCHANTED_BOOK) {
                ItemStack book = click.getItem();
                if (Objects.requireNonNull(book.getItemMeta()).isUnbreakable()) {
                    MafiaPlayer sorcerer = plugin.getLivingPlayers().get(click.getPlayer().getUniqueId());
                    if (sorcerer.getRole() == Role.SORCERER && sorcerer.getRoleData().getData(RoleData.DataType.SORCERER_SELECTED) == Ability.VANISH) { // dirty check but it works
                        if (click.getPlayer().getLevel() < plugin.getConfig().getInt("vanishCost")) {
                            click.getPlayer().sendMessage(ChatColor.RED + "You don't have enough levels to use this spell!");
                        } else {
                            // Activate Spell
                            Player p = null;
                            for (Player target : plugin.getServer().getOnlinePlayers()) {
                                if (target == click.getPlayer()) continue;
                                if (SpyglassUtil.lookingAtPlayer(plugin, click.getPlayer(), target)) {
                                    p = target;
                                    break;
                                }
                            }
                            if (p == null) p = click.getPlayer();
                            // apply effect
                            p.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 3600, 0, false, false, true));
                            click.getPlayer().setLevel(click.getPlayer().getLevel() - plugin.getConfig().getInt("vanishCost"));
                            click.getPlayer().sendMessage(ChatColor.GREEN + "You used " + ChatColor.LIGHT_PURPLE + "Vanish" +
                                    ChatColor.GREEN + " on " + ChatColor.AQUA + p.getName());
                            sorcerer.getStatus().startStatus(StatusData.Status.UNHOLY, 48000L); // Two days of unholy
                            plugin.getServer().getWorlds().get(0).spawnParticle(Particle.SPELL_WITCH, click.getPlayer().getLocation().add(0,1,0), 10, 1, 1, 1);
                            plugin.getServer().getWorlds().get(0).spawnParticle(Particle.ENCHANTMENT_TABLE, p.getLocation().add(0,1,0), 10, 1, 1, 1);
                        }
                    }
                }
            }
        }
    }
}
