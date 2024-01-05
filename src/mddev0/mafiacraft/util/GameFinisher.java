package mddev0.mafiacraft.util;

import mddev0.mafiacraft.MafiaCraft;
import mddev0.mafiacraft.player.*;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;
import java.util.logging.Level;

public class GameFinisher extends BukkitRunnable {

    private final MafiaCraft plugin;

    boolean checkedTonight = false;

    public GameFinisher(MafiaCraft plugin) {
        this.plugin = plugin;
    }


    @Override
    public void run() {
        if (!plugin.getActive()) return;
        // Only check if game is finished at dusk
        // Sunset starts at 12000, but sun and moon even on horizon at 12786.
        long dayTime = Bukkit.getWorlds().get(0).getTime();
        if (dayTime > 13000 && checkedTonight) checkedTonight = false;
        else if (dayTime > 12786 && dayTime <= 13000 && !checkedTonight) {
            checkedTonight = true;
            Bukkit.getLogger().log(Level.INFO, "[MafiaCraft] Checking to see if there is a winner");

            Set<MafiaPlayer> winnerSet = new HashSet<>();
            boolean endGame = false;

            String title = "" + ChatColor.GOLD + ChatColor.BOLD + "Game Over!";
            String subtitle = "";

            // Should check if the game should end by counting each win condition
            // Count remaining players
            int mafiaRemaining, villageRemaining, soloRemaining, vampRemaining;
            mafiaRemaining = villageRemaining = soloRemaining = vampRemaining = 0;
            for (MafiaPlayer mp : plugin.getLivingPlayers().values()) {
                switch (mp.getRole().getAlignment()) {
                    case VILLAGE -> villageRemaining++;
                    case MAFIA -> mafiaRemaining++;
                    case SOLO -> soloRemaining++;
                    case VAMPIRES -> vampRemaining++;
                }
            }

            // If there are no opposing groups remaining the game might be ready to end
            if (mafiaRemaining + villageRemaining + soloRemaining + vampRemaining == Math.max(Math.max(mafiaRemaining, villageRemaining), Math.max(soloRemaining, vampRemaining))) {
                // if that super complicated expression clears then the game might be ready to end
                // Essentially checks if sum of all groups alive is equal to the number of the largest group alive (only true if all but one of them are 0)
                // Now check which group should win
                if (mafiaRemaining > 0) {
                    // Mafia are only group left, they win
                    endGame = true;
                    for (MafiaPlayer winner : plugin.getPlayerList().values()) {
                        if (winner.getRole().getAlignment() == Role.Team.MAFIA) {
                            winnerSet.add(winner);
                        }
                    }
                    subtitle = ChatColor.GRAY + "The " + ChatColor.RED + ChatColor.BOLD + "Mafia" + ChatColor.RESET + ChatColor.GRAY + " has won!";
                } else if (villageRemaining > 0) {
                    // Village is only group left, they win
                    endGame = true;
                    for (MafiaPlayer winner : plugin.getPlayerList().values()) { // dead and living players can win
                        if (winner.getRole().getAlignment() == Role.Team.VILLAGE) {
                            winnerSet.add(winner);
                        }
                    }
                    subtitle = ChatColor.GRAY + "The " + ChatColor.DARK_GREEN + ChatColor.BOLD + "Village" + ChatColor.RESET + ChatColor.GRAY + " has won!";
                } else if (soloRemaining == 1) {
                    // Only one "wins alone" role can win the game, so specifically one must be found here to win
                    for (MafiaPlayer winner : plugin.getLivingPlayers().values()) { // ONLY living players can win
                        endGame = true;
                        if (winner.getRole().getAlignment() == Role.Team.SOLO) {
                            winnerSet.add(winner);
                            if (winner.getRole() == Role.SERIAL_KILLER)
                                subtitle = ChatColor.GRAY + "The " + ChatColor.BLUE + ChatColor.BOLD + "Serial Killer" + ChatColor.RESET + ChatColor.GRAY + " has won!";
                            else if (winner.getRole() == Role.BODYGUARD)
                                subtitle = ChatColor.GRAY + "The " + ChatColor.DARK_AQUA + ChatColor.BOLD + "Trapper" + ChatColor.RESET + ChatColor.GRAY + " has won!";
                            else if (winner.getRole() == Role.WEREWOLF)
                                subtitle = ChatColor.GRAY + "The " + ChatColor.DARK_RED + ChatColor.BOLD + "Werewolf" + ChatColor.RESET + ChatColor.GRAY + " has won!";
                            break; // since we only take 1
                        }
                    }
                } else if (vampRemaining > 0) {
                    // Vampires win
                    endGame = true;
                    for (MafiaPlayer winner : plugin.getPlayerList().values()) { // dead and living players can win
                        if (winner.getRole() == Role.VAMPIRE) {
                            winnerSet.add(winner);
                        }
                        subtitle = ChatColor.GRAY + "The " + ChatColor.DARK_PURPLE + ChatColor.BOLD + "Vampires" + ChatColor.RESET + ChatColor.GRAY + " have won!";
                    }
                } else if (mafiaRemaining + villageRemaining + soloRemaining + vampRemaining == 0) { // only unaligned left somehow
                    endGame = true;
                    subtitle = ChatColor.GRAY + "The " + ChatColor.YELLOW + ChatColor.BOLD + "Survivors" + ChatColor.RESET + ChatColor.GRAY + " have won!";
                } else if (plugin.getLivingPlayers().isEmpty()) {
                    endGame = true;
                    subtitle = ChatColor.GRAY + "Everyone died!";
                }

                // Unaligned roles
                for (MafiaPlayer mp : plugin.getLivingPlayers().values()) { // survivors ONLY win if alive
                    if (mp.getRole() == Role.HUNTER) {
                        // Check if targets are killed
                        boolean hunterWin = true;
                        // SCUFFED: Yes, this next line is an unchecked cast. Guess I just have to be careful.
                        for (String id : (Set<String>)mp.getRoleData().getData(RoleData.DataType.HUNTER_TARGETS)) {
                            hunterWin = hunterWin && !plugin.getPlayerList().get(UUID.fromString(id)).isLiving();
                        }
                        if (hunterWin) winnerSet.add(mp);
                    } else if (mp.getRole() == Role.JESTER)
                        if ((Boolean)mp.getRoleData().getData(RoleData.DataType.JESTER_ABILITY_USED))  {
                            winnerSet.add(mp);
                    } else if (mp.getRole() == Role.BODYGUARD)
                        if (plugin.getLivingPlayers().containsKey(UUID.fromString((String)mp.getRoleData().getData(RoleData.DataType.BODYGUARD_PROTECTEE)))) {
                            winnerSet.add(mp);
                    } else if (mp.isLiving()) { // Other surviving roles have no special win conditions, win if alive
                        winnerSet.add(mp);
                    }
                }
                
                // End the game if people have won
                List<MafiaPlayer> winners = winnerSet.stream().toList();
                if (endGame) {
                    plugin.setActive(false);
                    Bukkit.getLogger().log(Level.INFO, "[MafiaCraft] The game is over!");
                    // Sound and title sent (title prepped above)
                    for (Player player : Bukkit.getOnlinePlayers()) {
                        player.playSound(player.getLocation(), Sound.ENTITY_WITHER_SPAWN, SoundCategory.MASTER, 1.0f, 1.0f);
                        player.sendTitle(title, subtitle, 20, 200, 100);
                    }
                    // Winner list
                    String winnersMessage = ChatColor.GREEN + "";
                    if (winners.size() == 1) {
                        winnersMessage = winnersMessage.concat(Bukkit.getOfflinePlayer(winners.get(0).getID()).getName() + " is the winner!");
                    } else if (winners.size() == 2) {
                        winnersMessage = winnersMessage.concat(Bukkit.getOfflinePlayer(winners.get(0).getID()).getName() + " and " +
                                Bukkit.getOfflinePlayer(winners.get(1).getID()).getName() + " are the winners!");
                    } else {
                        for (int w = 0; w < winners.size()-1; w++) {
                            winnersMessage = winnersMessage.concat(Bukkit.getOfflinePlayer(winners.get(w).getID()).getName() + ", ");
                        }
                        winnersMessage = winnersMessage.concat(" and " + winners.get(winners.size()-1) + " are the winners!");
                    }
                    Bukkit.broadcastMessage(winnersMessage);

                    // List of all players
                    List<String> playerList = new ArrayList<>();
                    String playerListHeader = ChatColor.GOLD +  "All of the players who played in this game:";
                    playerList.add(playerListHeader);
                    for (MafiaPlayer p : plugin.getPlayerList().values()) {
                        StringBuilder addToList = new StringBuilder();
                        // Name and living
                        if (p.isLiving()) {
                            addToList.append(Bukkit.getOfflinePlayer(p.getID()).getName()).append(" ");
                        } else {
                            addToList.append(ChatColor.DARK_GRAY).append(Bukkit.getOfflinePlayer(p.getID()).getName()).append(" (dead) ");
                        }
                        // Separator
                        addToList.append(ChatColor.GRAY).append("- ");
                        // Role
                        addToList.append(switch (p.getRole().getAlignment()) {
                            case MAFIA -> ChatColor.RED;
                            case VILLAGE -> ChatColor.DARK_GREEN;
                            case SOLO-> {
                                if (p.getRole() == Role.SERIAL_KILLER) yield ChatColor.BLUE;
                                else if (p.getRole() == Role.BODYGUARD) yield ChatColor.DARK_AQUA;
                                else yield ChatColor.DARK_GRAY; // Should never be used
                            }
                            case NONE -> {
                                if (p.getRole() == Role.JESTER) yield ChatColor.LIGHT_PURPLE;
                                else yield ChatColor.YELLOW; // Will be used
                            }
                            case VAMPIRES -> {
                                if (p.getRole() == Role.VAMPIRE) yield ChatColor.DARK_PURPLE;
                                else yield ChatColor.AQUA; // Should never be used
                            }
                        }).append(p.getRole().toString());
                        // Original Role
                        if (!p.getRole().toString().equals(p.getOriginalRole().toString())) {
                            addToList.append(ChatColor.GRAY).append(" (originally ");
                            addToList.append(switch (p.getOriginalRole().getAlignment()) {
                                case MAFIA -> ChatColor.RED;
                                case VILLAGE -> ChatColor.DARK_GREEN;
                                case SOLO -> {
                                    if (p.getOriginalRole() == Role.SERIAL_KILLER) yield ChatColor.BLUE;
                                    else if (p.getOriginalRole() == Role.BODYGUARD) yield ChatColor.DARK_AQUA;
                                    else yield ChatColor.DARK_GRAY; // Should never be used
                                }
                                case NONE -> {
                                    if (p.getOriginalRole() == Role.JESTER) yield ChatColor.LIGHT_PURPLE;
                                    else yield ChatColor.YELLOW; // Will be used
                                }
                                case VAMPIRES -> ChatColor.DARK_PURPLE;
                            }).append(p.getOriginalRole().toString()).append(ChatColor.GRAY).append(")");
                        }
                        playerList.add(addToList.toString());
                    }

                    // Send player list to all players
                    for (String player : playerList) {
                        Bukkit.broadcastMessage(player);
                    }
                }
            }
            // Or, nobody has won yet
            else {
                Bukkit.getLogger().log(Level.INFO, "[MafiaCraft] No winners found");
            }
        }
    }
}
