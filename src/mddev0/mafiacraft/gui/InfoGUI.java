package mddev0.mafiacraft.gui;

import mddev0.mafiacraft.MafiaCraft;
import mddev0.mafiacraft.abilities.Ability;
import mddev0.mafiacraft.roles.Role;
import mddev0.mafiacraft.util.MafiaPlayer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryEvent;
import org.bukkit.event.inventory.InventoryInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.ArrayList;

public final class InfoGUI implements Listener {

    private final MafiaCraft plugin;
    private final MafiaPlayer caller;
    private final Inventory inv;

    // TODO: SHOW HUNTER TARGETS SOMEWHERE IN GUI!

    public InfoGUI(MafiaCraft plugin, MafiaPlayer player) {
        this.plugin = plugin;
        this.caller = player;

        String invTitle = switch (caller.getRole().getWinCond()) {
            case MAFIA -> ChatColor.RED;
            case VILLAGE -> ChatColor.DARK_GREEN;
            case ALONE -> ChatColor.BLUE;
            case SURVIVING -> ChatColor.YELLOW;
            case ROLE -> ChatColor.DARK_AQUA;
        } + Bukkit.getPlayer(caller.getID()).getName() + " | "
                + caller.getRole().toString();

        inv = Bukkit.createInventory(null, 54, invTitle);

        fillInventory();
    }

    private void fillInventory() {
        // Top row black
        for (int pos = 0; pos < 9; pos++) {
            inv.setItem(pos, new ItemStack(Material.BLACK_STAINED_GLASS_PANE));
        }

        // Create head for top center
        ItemStack head = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta headMeta = (SkullMeta) head.getItemMeta();
        assert headMeta != null;
        headMeta.setOwningPlayer(Bukkit.getOfflinePlayer(caller.getID()));
        // Title
        String headTitle = switch (caller.getRole().getWinCond()) {
            case MAFIA -> ChatColor.RED;
            case VILLAGE -> ChatColor.DARK_GREEN;
            case ALONE -> ChatColor.BLUE;
            case SURVIVING -> ChatColor.YELLOW;
            case ROLE -> ChatColor.DARK_AQUA;
        } + Bukkit.getPlayer(caller.getID()).getName() + " | "
                + caller.getRole().toString();
        headMeta.setDisplayName(headTitle);
        // Lore
        ArrayList<String> headLore = new ArrayList<>();
        headLore.add(ChatColor.RESET + "" + ChatColor.GRAY + "Wins " +
                switch (caller.getRole().getWinCond()) {
                    case MAFIA -> "with the " + ChatColor.RED + "Mafia";
                    case VILLAGE -> "with the " + ChatColor.DARK_GREEN + "Village";
                    case ALONE -> "by " + ChatColor.BLUE + "killing everyone" +
                            ChatColor.GRAY + " (except those who win by surviving)";
                    case SURVIVING -> ChatColor.YELLOW + "by surviving";
                    case ROLE -> ChatColor.DARK_AQUA + "with players of the same role";
                });
        headLore.add(ChatColor.GRAY + "See all your abilities below.");
        if (caller.getRole().getWinCond() == Role.WinCondition.MAFIA || caller.getRole().getWinCond() == Role.WinCondition.ROLE)
            headLore.add(ChatColor.GRAY + "The names and roles of your teammates are listed, if they are alive.");
        headMeta.setLore(headLore);
        head.setItemMeta(headMeta);
        inv.setItem(4,head);

        // List of Abilities
        int abilityNumber = 0;
        for (Ability abil : caller.getRole().getAbilities()) {
            ItemStack abilItem;
            ItemMeta abilMeta;
            if (caller.onCooldown(abil)) { // On Cooldown
                abilItem = new ItemStack(Material.YELLOW_CONCRETE);
                abilMeta = abilItem.getItemMeta();
                assert abilMeta != null;
                abilMeta.setDisplayName(ChatColor.GOLD + abil.fullName());
                ArrayList<String> abilLore = new ArrayList<>();
                abilLore.add(ChatColor.RESET + "" + ChatColor.YELLOW + "This ability is on cooldown.");
                abilMeta.setLore(abilLore);
                abilItem.setItemMeta(abilMeta);
            } else { // Not On Cooldown
                abilItem = new ItemStack(Material.WHITE_CONCRETE);
                abilMeta = abilItem.getItemMeta();
                assert abilMeta != null;
                abilMeta.setDisplayName(ChatColor.AQUA + abil.fullName());
                ArrayList<String> abilLore = new ArrayList<>();
                abilLore.add(ChatColor.RESET + "" + ChatColor.WHITE + "This ability is available!");
                abilMeta.setLore(abilLore);
                abilItem.setItemMeta(abilMeta);
            }
            // Set Item
            int abilPos = 9 + (9 * (abilityNumber % 5)) + (abilityNumber / 9);
            inv.setItem(abilPos, abilItem);
            abilityNumber++;
        }

        // List of Teammates
        int teammateNumber = 0;
        if (caller.getRole().getWinCond() == Role.WinCondition.MAFIA) { // If Mafia
            for (MafiaPlayer other : plugin.getLivingPlayers().values()) {
                ItemStack teamHead;
                SkullMeta teamMeta;
                String teamTitle;
                if (other.getRole().getWinCond() == Role.WinCondition.MAFIA) { // Is Other Mafia
                    teamHead = new ItemStack(Material.PLAYER_HEAD);
                    teamMeta = (SkullMeta) teamHead.getItemMeta();
                    assert teamMeta != null;
                    teamMeta.setOwningPlayer(Bukkit.getOfflinePlayer(other.getID()));
                    try {
                        teamTitle = ChatColor.RED + Bukkit.getPlayer(other.getID()).getName() + " | " + other.getRole().toString();
                    } catch (NullPointerException npex) {
                        teamTitle = ChatColor.RED + "??? | " + other.getRole().toString();
                    }
                    teamMeta.setDisplayName(teamTitle);
                    teamHead.setItemMeta(teamMeta);
                    // Set Item
                    int teamPos = 17 + (9 * (teammateNumber % 5)) - (teammateNumber / 9);
                    inv.setItem(teamPos, teamHead);
                    teammateNumber++;
                }
            }
        } else if (caller.getRole().getWinCond() == Role.WinCondition.ROLE) { // If Other Teammate
            for (MafiaPlayer other : plugin.getLivingPlayers().values()) {
                ItemStack teamHead;
                SkullMeta teamMeta;
                String teamTitle;
                if (other.getRole().toString().equals(caller.getRole().toString())) { // Is Other Teammate
                    teamHead = new ItemStack(Material.PLAYER_HEAD);
                    teamMeta = (SkullMeta) teamHead.getItemMeta();
                    assert teamMeta != null;
                    teamMeta.setOwningPlayer(Bukkit.getOfflinePlayer(other.getID()));
                    try {
                        teamTitle = ChatColor.DARK_AQUA + Bukkit.getPlayer(other.getID()).getName() + " | " + other.getRole().toString();
                    } catch (NullPointerException npex) {
                        teamTitle = ChatColor.DARK_AQUA + "??? | " + other.getRole().toString();
                    }
                    teamMeta.setDisplayName(teamTitle);
                    teamHead.setItemMeta(teamMeta);
                    // Set Item
                    int teamPos = 17 + (9 * (teammateNumber % 5)) - (teammateNumber / 9);
                    inv.setItem(teamPos, teamHead);
                    teammateNumber++;
                }
            }
        }
    }

    public void open(final HumanEntity e) {
        e.openInventory(inv);
    }

    @EventHandler
    public void onInteract(InventoryClickEvent e) {
        if (e.getInventory().equals(inv))
            e.setCancelled(true);
    }
}
