package mddev0.mafiacraft.abilities;

import mddev0.mafiacraft.MafiaCraft;
import mddev0.mafiacraft.roles.Sorcerer;
import mddev0.mafiacraft.util.MafiaPlayer;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByBlockEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class SpellBook implements Listener {

    private final MafiaCraft plugin;

    public SpellBook(MafiaCraft plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onItemDamage(EntityDamageByBlockEvent damage) {
        if (!plugin.getActive()) return; // DO NOTHING IF NOT ACTIVE!
        if (damage.getEntityType() == EntityType.DROPPED_ITEM) {
            Item item = (Item) damage.getEntity();
            if (item.getItemStack().getType() == Material.ENCHANTED_BOOK) {
                // Valid item, check player
                MafiaPlayer thrower = plugin.getLivingPlayers().get(item.getThrower());
                if (thrower.getRole().hasAbility(Ability.SPELL_BOOK)) {
                    item.remove();
                    ItemStack toGive = new ItemStack(Material.ENCHANTED_BOOK);
                    ItemMeta meta = toGive.getItemMeta();
                    assert meta != null;
                    meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
                    meta.setUnbreakable(true);
                    meta.setDisplayName(ChatColor.LIGHT_PURPLE + "Spell Book");
                    List<String> lore = new ArrayList<>();
                    lore.add("Only usable by Sorcerers.");
                    lore.add("Right click to switch to different spells.");
                    lore.add("Left click to activate. Look at a player to cast the spell on them, or anywhere else to cast on yourself.");
                    meta.setLore(lore);
                    thrower.setUnholy();
                }
            }
        }
    }

    @EventHandler
    public void onBookClick(PlayerInteractEvent click) {
        if (!plugin.getActive()) return; // DO NOTHING IF NOT ACTIVE!
        if (click.getAction().equals(Action.RIGHT_CLICK_AIR)) {
            if (click.getItem() != null && click.getItem().getType() == Material.ENCHANTED_BOOK) {
                ItemStack book = click.getItem();
                if (Objects.requireNonNull(book.getItemMeta()).isUnbreakable()) {
                    MafiaPlayer sorcerer = plugin.getLivingPlayers().get(click.getPlayer().getUniqueId());
                    if (sorcerer.getRole() instanceof Sorcerer) { // dirty check but it works
                        ((Sorcerer) sorcerer.getRole()).nextAbility();
                        click.getPlayer().sendMessage(ChatColor.LIGHT_PURPLE +
                                "Changed spells to " + ChatColor.GRAY +
                                ((Sorcerer) sorcerer.getRole()).getSelected().fullName());
                    }
                }
            }
        }
    }
}
