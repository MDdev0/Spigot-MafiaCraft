package mddev0.mafiacraft.abilities;

import mddev0.mafiacraft.MafiaCraft;
import mddev0.mafiacraft.player.Role;
import mddev0.mafiacraft.player.StatusData;
import mddev0.mafiacraft.player.MafiaPlayer;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.data.Levelled;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PotionSplashEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@SuppressWarnings("unused")
public final class Ambrosia extends BukkitRunnable implements Listener {

    private final MafiaCraft plugin;

    public Ambrosia(MafiaCraft plugin) {
        this.plugin = plugin;
    }

    // To create potion
    @Override
    public void run() {
        if (!plugin.getActive()) return; // DO NOTHING IF NOT ACTIVE!
        for (World w : plugin.getServer().getWorlds()) {
            for (Item i : w.getEntitiesByClass(Item.class)) {
                // Test for one of the four required items in the ambrosia recipe
                // TODO: maybe refactor this to make it configurable
                if (i.getItemStack().getType() == Material.GOLDEN_APPLE) {
                    // Create list of other items needed
                    List<Material> required = new ArrayList<>();
                    required.add(Material.GOLDEN_APPLE);
                    required.add(Material.HONEY_BOTTLE);
                    required.add(Material.MILK_BUCKET);
                    // check for cauldron
                    Block b = i.getLocation().getBlock();
                    if (b.getType() == Material.WATER_CAULDRON) {
                        // check block below
                        if (i.getLocation().add(0, -1, 0).getBlock().getType() == Material.FIRE ||
                                i.getLocation().add(0, -1, 0).getBlock().getType() == Material.SOUL_FIRE) {
                            // Check cauldron level
                            Levelled bdata = (Levelled) b.getBlockData();
                            if (bdata.getLevel() == bdata.getMaximumLevel()) {
                                // cauldron is full
                                // Since the Golden Apple is the trigger item, make sure that it was thrown
                                // by a player with the correct ability
                                MafiaPlayer thrower = plugin.getLivingPlayers().get(i.getThrower());
                                if (thrower != null && thrower.getRole().getAbilities().contains(Ability.AMBROSIA) && !thrower.getCooldowns().isOnCooldown(Ability.AMBROSIA)) {
                                    // then get list of all items in cauldron
                                    List<Item> items = new ArrayList<>();
                                    for (Entity ent : b.getWorld().getNearbyEntities(b.getBoundingBox()))
                                        if (ent.getType() == EntityType.DROPPED_ITEM)
                                            items.add((Item) ent);
                                    // Mark the required items as satisfied, if not required then remove so it doesn't get deleted
                                    items.removeIf(found -> !required.remove(found.getItemStack().getType()));
                                    if (required.isEmpty()) {
                                        // all required items have been found
                                        // delete the required items
                                        for (Item toDelete : items) toDelete.remove();
                                        // make empty cauldron
                                        b.setType(Material.CAULDRON);
                                        b.getWorld().playEffect(b.getLocation(), Effect.BREWING_STAND_BREW,0);
                                        b.getWorld().spawnParticle(Particle.BUBBLE_POP, b.getLocation(),20,0.5,0.5,0.5);
                                        // create item
                                        ItemStack ambrosiaItem = new ItemStack(Material.SPLASH_POTION);
                                        PotionMeta ambrosiaPotion = (PotionMeta) ambrosiaItem.getItemMeta();
                                        assert ambrosiaPotion != null;
                                        ambrosiaPotion.setColor(Color.fromRGB(255,200,0));
                                        ambrosiaPotion.setDisplayName(ChatColor.GOLD + "Ambrosia");
                                        List<String> ambrosiaLore = new ArrayList<>();
                                        ambrosiaLore.add("Splash on any " + ChatColor.DARK_PURPLE + "Vampire");
                                        ambrosiaLore.add("to convert them back to their original role.");
                                        ambrosiaPotion.setLore(ambrosiaLore);
                                        ambrosiaItem.setItemMeta(ambrosiaPotion);
                                        Item spawned = (Item) i.getWorld().spawnEntity(b.getLocation(), EntityType.DROPPED_ITEM);
                                        spawned.setItemStack(ambrosiaItem);
                                        // Add cooldown to thrower
                                        long waitUntil = plugin.getWorldFullTime() + (24000L * 7); // 7 days later
                                        waitUntil = waitUntil - (waitUntil % 24000); // Round to earliest morning
                                        thrower.getCooldowns().startCooldown(Ability.AMBROSIA, waitUntil);
                                        thrower.getStatus().startStatus(StatusData.Status.UNHOLY, plugin.getWorldFullTime() + 48000L); // Two days of unholy
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    // When potion lands
    @EventHandler
    public void onPotionLand(PotionSplashEvent splash) {
        if (!plugin.getActive()) return; // DO NOTHING IF NOT ACTIVE!
        ItemStack original = splash.getPotion().getItem();
        if (Objects.requireNonNull(original.getItemMeta()).getDisplayName().equals(ChatColor.GOLD + "Ambrosia")) {
            // Is an ambrosia potion
            for (Entity ent : splash.getAffectedEntities())
                if (ent.getType() == EntityType.PLAYER) {
                    MafiaPlayer player = plugin.getLivingPlayers().get(ent.getUniqueId());
                    if (player.getRole() == Role.VAMPIRE) {
                        // affected entity is player who is a vampire
                        player.resetRole();
                        // role is changed back
                        ent.getWorld().playSound(ent.getLocation(), Sound.ENTITY_ZOMBIE_VILLAGER_CURE, SoundCategory.PLAYERS, 1, 1);
                        ent.getWorld().spawnParticle(Particle.ELECTRIC_SPARK, ent.getLocation().add(0,1,0),50,1,1,2,1);
                        ent.sendMessage(ChatColor.AQUA + "You have been returned to your original role!");
                    }
                }
        }
    }
}
