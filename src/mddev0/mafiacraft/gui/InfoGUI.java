package mddev0.mafiacraft.gui;

import mddev0.mafiacraft.MafiaCraft;
import mddev0.mafiacraft.abilities.Ability;
import mddev0.mafiacraft.player.MafiaPlayer;
import mddev0.mafiacraft.player.Role;
import mddev0.mafiacraft.player.RoleData;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

public final class InfoGUI implements Listener {

    private final MafiaCraft plugin;
    private final MafiaPlayer caller;
    private final Inventory inv;

    public InfoGUI(MafiaCraft plugin, MafiaPlayer player) {
        this.plugin = plugin;
        this.caller = player;

        String invTitle = switch (caller.getRole().getAlignment()) {
            case MAFIA -> ChatColor.RED;
            case VILLAGE -> ChatColor.DARK_GREEN;
            case SOLO -> ChatColor.BLUE;
            case NONE -> ChatColor.YELLOW;
            case VAMPIRES -> ChatColor.DARK_PURPLE;
        } + Objects.requireNonNull(Bukkit.getPlayer(caller.getID())).getName() + " | "
                + caller.getRole().toString();

        inv = Bukkit.createInventory(null, 54, invTitle);

        fillInventory();
    }

    private void fillInventory() {
        // Top row black
        for (int pos = 0; pos < 9; pos++) {
            inv.setItem(pos, new ItemStack(Material.BLACK_STAINED_GLASS_PANE));
        }

        // Create head for top bar
        ItemStack head = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta headMeta = (SkullMeta) head.getItemMeta();
        assert headMeta != null;
        headMeta.setOwningPlayer(Bukkit.getOfflinePlayer(caller.getID()));
        // Title
        String headTitle = switch (caller.getRole().getAlignment()) {
            case MAFIA -> ChatColor.RED;
            case VILLAGE -> ChatColor.DARK_GREEN;
            case SOLO -> ChatColor.BLUE;
            case NONE -> ChatColor.YELLOW;
            case VAMPIRES -> ChatColor.DARK_PURPLE;
        } + Objects.requireNonNull(Bukkit.getPlayer(caller.getID())).getName() + " | "
                + caller.getRole().toString();
        headMeta.setDisplayName(headTitle);
        // Lore
        ArrayList<String> headLore = new ArrayList<>();
        headLore.add(ChatColor.RESET + "" + ChatColor.GRAY + "Wins " +
                switch (caller.getRole().getAlignment()) {
                    case MAFIA -> "with the " + ChatColor.RED + "Mafia";
                    case VILLAGE -> "with the " + ChatColor.DARK_GREEN + "Village";
                    case SOLO -> "by " + ChatColor.BLUE + "killing everyone" +
                            ChatColor.GRAY + " who would oppose you";
                    case NONE -> {
                        if (caller.getRole() == Role.JESTER)
                            yield "if " + ChatColor.LIGHT_PURPLE + "killed" +
                                    ChatColor.GRAY + " by a member of the " + ChatColor.DARK_GREEN + "Village";
                        else
                            yield ChatColor.YELLOW + "by surviving";
                    }
                    case VAMPIRES -> "by " + ChatColor.DARK_AQUA + "Converting all players to Vampires";
                });
        headLore.add(ChatColor.GRAY + "See all your abilities below.");
        if (caller.getRole().getAlignment() == Role.Team.MAFIA || caller.getRole().getAlignment() == Role.Team.VAMPIRES) {
            headLore.add(ChatColor.GRAY + "The names and roles of your");
            headLore.add(ChatColor.GRAY + "teammates are listed below, if they are alive.");
        } else if (caller.getRole() == Role.HUNTER) {
            headLore.add(ChatColor.GRAY + "The names and status of your");
            headLore.add(ChatColor.GRAY + "targets are listed below.");
        }
        headMeta.setLore(headLore);
        head.setItemMeta(headMeta);
        inv.setItem(4,head);

        // Game status indicator in top
        ItemStack active;
        ItemMeta activeMeta;
        if (plugin.getActive()) {
            active = new ItemStack(Material.LIME_CONCRETE_POWDER);
            activeMeta = active.getItemMeta();
            assert activeMeta != null;
            activeMeta.setDisplayName(ChatColor.GREEN + "The game is active!");
        } else {
            active = new ItemStack(Material.RED_CONCRETE_POWDER);
            activeMeta = active.getItemMeta();
            assert activeMeta != null;
            activeMeta.setDisplayName(ChatColor.RED + "The game is paused.");
        }
        active.setItemMeta(activeMeta);
        inv.setItem(3,active);
        inv.setItem(5,active);

        // TODO: Show abilities that are active for a set time with light blue concrete
        // List of Abilities
        int abilityNumber = 0;
        for (Ability abil : caller.getRole().getAbilities()) {
            ItemStack abilItem;
            ItemMeta abilMeta;
            if (abil == Ability.JUST_A_PRANK && (Boolean)caller.getRoleData().getData(RoleData.DataType.JESTER_ABILITY_USED)) {
                // One-time use (this ability only)
                abilItem = new ItemStack(Material.GRAY_CONCRETE);
                abilMeta = abilItem.getItemMeta();
                assert abilMeta != null;
                abilMeta.setDisplayName(ChatColor.DARK_GRAY + abil.toString());
                ArrayList<String> abilLore = new ArrayList<>();
                abilLore.add(ChatColor.RESET + "" + ChatColor.GRAY + "This ability may only be used once.");
                abilLore.add(ChatColor.DARK_AQUA + "" + ChatColor.ITALIC + abil.getDesc());
                abilMeta.setLore(abilLore);
                abilItem.setItemMeta(abilMeta);
            } else if (caller.getCooldowns().isOnCooldown(abil)) { // On Cooldown
                abilItem = new ItemStack(Material.YELLOW_CONCRETE);
                abilMeta = abilItem.getItemMeta();
                assert abilMeta != null;
                abilMeta.setDisplayName(ChatColor.GOLD + abil.toString());
                ArrayList<String> abilLore = new ArrayList<>();
                abilLore.add(ChatColor.RESET + "" + ChatColor.YELLOW + "This ability is on cooldown.");
                abilLore.add(ChatColor.DARK_AQUA + "" + ChatColor.ITALIC + abil.getDesc());
                abilMeta.setLore(abilLore);
                abilItem.setItemMeta(abilMeta);
            } else { // Not On Cooldown
                abilItem = new ItemStack(Material.WHITE_CONCRETE);
                abilMeta = abilItem.getItemMeta();
                assert abilMeta != null;
                abilMeta.setDisplayName(ChatColor.AQUA + abil.toString());
                ArrayList<String> abilLore = new ArrayList<>();
                abilLore.add(ChatColor.RESET + "" + ChatColor.WHITE + "This ability is available!");
                abilLore.add(ChatColor.DARK_AQUA + "" + ChatColor.ITALIC + abil.getDesc());
                abilMeta.setLore(abilLore);
                abilItem.setItemMeta(abilMeta);
            }
            // Set Item
            int abilPos = 9 + (9 * (abilityNumber % 5)) + (abilityNumber / 5);
            inv.setItem(abilPos, abilItem);
            abilityNumber++;
        }

        // List of Teammates
        if (caller.getRole().getAlignment() == Role.Team.MAFIA) { // If Mafia
            int teammateNumber = 0;
            for (MafiaPlayer other : plugin.getLivingPlayers().values()) {
                ItemStack teamHead;
                SkullMeta teamMeta;
                String teamTitle;
                if (other.getRole().getAlignment() == Role.Team.MAFIA) { // Is Other Mafia
                    teamHead = new ItemStack(Material.PLAYER_HEAD);
                    teamMeta = (SkullMeta) teamHead.getItemMeta();
                    assert teamMeta != null;
                    teamMeta.setOwningPlayer(Bukkit.getOfflinePlayer(other.getID()));
                    teamTitle = ChatColor.RED + Bukkit.getOfflinePlayer(other.getID()).getName() + " | " + other.getRole().toString();
                    teamMeta.setDisplayName(teamTitle);
                    teamHead.setItemMeta(teamMeta);
                    // Set Item
                    int teamPos = 17 + (9 * (teammateNumber % 5)) - (teammateNumber / 5);
                    inv.setItem(teamPos, teamHead);
                    teammateNumber++;
                }
            }
        } else if (caller.getRole().getAlignment() == Role.Team.VAMPIRES) { // If Vampire
            int teammateNumber = 0;
            for (MafiaPlayer other : plugin.getLivingPlayers().values()) {
                ItemStack teamHead;
                SkullMeta teamMeta;
                String teamTitle;
                if (other.getRole().toString().equals(caller.getRole().toString())) { // Is Other Teammate
                    teamHead = new ItemStack(Material.PLAYER_HEAD);
                    teamMeta = (SkullMeta) teamHead.getItemMeta();
                    assert teamMeta != null;
                    teamMeta.setOwningPlayer(Bukkit.getOfflinePlayer(other.getID()));
                    teamTitle = ChatColor.DARK_AQUA + Bukkit.getOfflinePlayer(other.getID()).getName() + " | " + other.getRole().toString();
                    teamMeta.setDisplayName(teamTitle);
                    teamHead.setItemMeta(teamMeta);
                    // Set Item
                    int teamPos = 17 + (9 * (teammateNumber % 5)) - (teammateNumber / 5);
                    inv.setItem(teamPos, teamHead);
                    teammateNumber++;
                }
            }
        } else if (caller.getRole() == Role.HUNTER) { // If is a list of targets instead
            int targetNumber = 0;
            for (String uuid : (Set<String>)caller.getRoleData().getData(RoleData.DataType.HUNTER_TARGETS)) {
                MafiaPlayer target = plugin.getPlayerList().get(UUID.fromString(uuid));
                if (target == null) {
                    // skip target if that player is not in the game
                    continue;
                }
                ItemStack targetHead = new ItemStack(Material.PLAYER_HEAD);
                SkullMeta targetMeta = (SkullMeta) targetHead.getItemMeta();
                assert targetMeta != null;
                targetMeta.setOwningPlayer(Bukkit.getOfflinePlayer(UUID.fromString(uuid)));
                String targetTitle;
                if (target.isLiving())
                    targetTitle = ChatColor.YELLOW + Bukkit.getOfflinePlayer(UUID.fromString(uuid)).getName() + " | Alive";
                else
                    targetTitle = ChatColor.DARK_RED + Bukkit.getOfflinePlayer(UUID.fromString(uuid)).getName() + " | Dead";
                targetMeta.setDisplayName(targetTitle);
                targetHead.setItemMeta(targetMeta);
                // set item
                int targetPos = 17 + (9 * (targetNumber % 5)) - (targetNumber / 5);
                inv.setItem(targetPos, targetHead);
                targetNumber++;
            }
        }
        // TODO: Add indicators for Jester and Werewolves
    }

    public void open(final HumanEntity e) {
        e.openInventory(inv);
    }

    @SuppressWarnings("unused")
    @EventHandler
    public void onInteract(InventoryClickEvent e) {
        if (e.getInventory().equals(inv))
            e.setCancelled(true);
    }
}
