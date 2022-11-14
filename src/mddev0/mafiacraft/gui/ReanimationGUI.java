package mddev0.mafiacraft.gui;

import mddev0.mafiacraft.MafiaCraft;
import mddev0.mafiacraft.abilities.Ability;
import mddev0.mafiacraft.util.MafiaPlayer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public final class ReanimationGUI implements Listener {

    private final MafiaCraft plugin;
    private final Inventory inv;

    public ReanimationGUI(MafiaCraft plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);

        Map<UUID, MafiaPlayer> deadPlayers = plugin.getDeadPlayers();
        inv = Bukkit.createInventory(null, (deadPlayers.size()/9) +1, ChatColor.DARK_GREEN + "Choose a player to reanimate");

        createHeads(deadPlayers);
    }

    private void createHeads(Map<UUID, MafiaPlayer> dead) {
        for (Map.Entry<UUID, MafiaPlayer> p : dead.entrySet()) {
            OfflinePlayer player = Bukkit.getOfflinePlayer(p.getKey());
            final ItemStack head = new ItemStack(Material.PLAYER_HEAD, 1);
            final SkullMeta meta = (SkullMeta) head.getItemMeta();
            assert meta != null;
            meta.setDisplayName(ChatColor.AQUA + player.getName());
            meta.setOwningPlayer(player);
            inv.addItem(head);
        }
    }

    public void open(final HumanEntity e) {
        e.openInventory(inv);
    }

    @EventHandler
    public void onHeadClick(final InventoryClickEvent click) {
        if (!Objects.equals(click.getClickedInventory(), inv)) return;
        // Correct inventory
        click.setCancelled(true);
        final ItemStack clicked = click.getCurrentItem();
        if (clicked == null || clicked.getType().isAir()) return;
        // SCUFFED: So many requireNonNull... am I doing this wrong?
        UUID toReanimate = Objects.requireNonNull(((SkullMeta) Objects.requireNonNull(clicked.getItemMeta())).getOwningPlayer()).getUniqueId();
        plugin.getPlayerList().get(click.getWhoClicked().getUniqueId()).startCooldown(Ability.REANIMATION, 0L, 6);
        plugin.getPlayerList().get(toReanimate).makeAlive();
    }

    @EventHandler
    public void onDragItem(final InventoryDragEvent e) {
        if (e.getInventory().equals(inv))
            e.setCancelled(true);
    }
}
