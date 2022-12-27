package mddev0.mafiacraft.util;

import mddev0.mafiacraft.MafiaCraft;
import mddev0.mafiacraft.roles.*;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;
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

            List<MafiaPlayer> winners = new ArrayList<>();
            boolean endGame = false;

            String title = "" + ChatColor.GOLD + ChatColor.BOLD + "Game Over!";
            String subtitle = "";

            // Should check if the game should end by counting each win condition
            // Count remaining players
            int mafiaRemaining, villageRemaining, aloneRemaining, roleRemaining, survivingRemaining;
            mafiaRemaining = villageRemaining = aloneRemaining = roleRemaining = survivingRemaining = 0;
            for (MafiaPlayer mp : plugin.getLivingPlayers().values()) {
                switch (mp.getRole().getWinCond()) {
                    case VILLAGE -> villageRemaining++;
                    case MAFIA -> mafiaRemaining++;
                    case ALONE -> aloneRemaining++;
                    case ROLE -> roleRemaining++;
                    case SURVIVING -> survivingRemaining++;
                }
            }

            // If there are no opposing groups remaining
            if (mafiaRemaining + villageRemaining + aloneRemaining + roleRemaining == Math.max(Math.max(mafiaRemaining, villageRemaining), Math.max(aloneRemaining, roleRemaining))) {
                // if that super complicated expression clears then the game might be ready to end
                // Essentially checks if sum of all groups alive is equal to the number of the largest group alive (only true if all but one of them are 0)
                // Now check which group should win
                if (mafiaRemaining > 0) {
                    // Mafia are only group left, they win
                    endGame = true;
                    for (MafiaPlayer winner : plugin.getPlayerList().values()) {
                        if (winner.getRole().getWinCond() == Role.WinCondition.MAFIA) {
                            winners.add(winner);
                        }
                    }
                    subtitle = ChatColor.GRAY + "The " + ChatColor.RED + ChatColor.BOLD + "Mafia" + ChatColor.RESET + ChatColor.GRAY + " has won!";
                } else if (villageRemaining > 0) {
                    // Village is only group left, they win
                    endGame = true;
                    for (MafiaPlayer winner : plugin.getPlayerList().values()) { // dead and living players can win
                        if (winner.getRole().getWinCond() == Role.WinCondition.VILLAGE) {
                            winners.add(winner);
                        }
                    }
                    subtitle = ChatColor.GRAY + "The " + ChatColor.DARK_GREEN + ChatColor.BOLD + "Village" + ChatColor.RESET + ChatColor.GRAY + " has won!";
                } else if (aloneRemaining == 1) {
                    // Only one "wins alone" role can win the game, so specifically one must be found here to win
                    for (MafiaPlayer winner : plugin.getPlayerList().values()) { // dead and living players can win
                        endGame = true;
                        if (winner.getRole().getWinCond() == Role.WinCondition.ALONE) {
                            winners.add(winner);
                            if (winner.getRole() instanceof SerialKiller)
                                subtitle = ChatColor.GRAY + "The " + ChatColor.BLUE + ChatColor.BOLD + "Serial Killer" + ChatColor.RESET + ChatColor.GRAY + " has won!";
                            else if (winner.getRole() instanceof Trapmaker)
                                subtitle = ChatColor.GRAY + "The " + ChatColor.DARK_BLUE + ChatColor.BOLD + "Trapmaker" + ChatColor.RESET + ChatColor.GRAY + " has won!";
                            break; // since we only take 1
                        }
                    }
                } else if (roleRemaining > 0) {
                    // Will need to check each role
                    int werewolvesRemaining, vampiresRemaining;
                    werewolvesRemaining = vampiresRemaining = 0;
                    for (MafiaPlayer mp : plugin.getLivingPlayers().values()) { // count living players, same idea as above
                        if (mp.getRole() instanceof Werewolf) werewolvesRemaining++;
                        else if (mp.getRole() instanceof Vampire) vampiresRemaining++;
                    }
                    if (werewolvesRemaining + vampiresRemaining == Math.max(werewolvesRemaining, vampiresRemaining)) {
                        // Only Werewolves or Vampires remain, same deal as above
                        if (werewolvesRemaining > 0) {
                            // Werewolves win
                            endGame = true;
                            for (MafiaPlayer winner : plugin.getPlayerList().values()) { // dead and living players can win
                                if (winner.getRole() instanceof Werewolf) {
                                    winners.add(winner);
                                }
                                subtitle = ChatColor.GRAY + "The " + ChatColor.DARK_AQUA + ChatColor.BOLD + "Werewolves" + ChatColor.RESET + ChatColor.GRAY + " have won!";
                            }
                        } else if (vampiresRemaining > 0) {
                            // Vampires win
                            endGame = true;
                            for (MafiaPlayer winner : plugin.getPlayerList().values()) { // dead and living players can win
                                if (winner.getRole() instanceof Vampire) {
                                    winners.add(winner);
                                }
                                subtitle = ChatColor.GRAY + "The " + ChatColor.DARK_PURPLE + ChatColor.BOLD + "Vampires" + ChatColor.RESET + ChatColor.GRAY + " have won!";
                            }
                        }
                    }
                } else if (mafiaRemaining + villageRemaining + aloneRemaining + roleRemaining == 0) { // only survivors left somehow
                    subtitle = ChatColor.GRAY + "The " + ChatColor.YELLOW + ChatColor.BOLD + "Survivors" + ChatColor.RESET + ChatColor.GRAY + " have won!";
                }

                // Survivor roles
                if (survivingRemaining > 0) {
                    // Now check if any survivor roles have reached their win conditions and are alive
                    for (MafiaPlayer mp : plugin.getLivingPlayers().values()) {
                        if (mp.getRole() instanceof Hunter && ((Hunter) mp.getRole()).targetsKilled()) { // survivors ONLY win if alive
                            winners.add(mp);
                        } else if (mp.getRole() instanceof Jester && ((Jester) mp.getRole()).getAbilityActivated())  {
                            winners.add(mp);
                        } else { // Other surviving roles have no special win conditions
                            winners.add(mp);
                        }
                    }
                }

                // Do end the game if people have won
                if (endGame) {
                    plugin.setActive(false);
                    Bukkit.getLogger().log(Level.INFO, "[MafiaCraft] The game has been finished!");
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
                    String playerListHeader = ChatColor.GRAY +  "All of the players who played in this game (with final and original roles):";
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
                        addToList.append(switch (p.getRole().getWinCond()) {
                            case MAFIA -> ChatColor.RED;
                            case VILLAGE -> ChatColor.DARK_GREEN;
                            case ALONE -> {
                                if (p.getRole().toString().equals("Serial Killer")) yield ChatColor.BLUE;
                                else if (p.getRole().toString().equals("Trapmaker")) yield ChatColor.DARK_BLUE;
                                else yield ChatColor.DARK_GRAY; // Should never be used
                            }
                            case SURVIVING -> {
                                if (p.getRole().toString().equals("Jester")) yield ChatColor.LIGHT_PURPLE;
                                else yield ChatColor.YELLOW; // Will be used
                            }
                            case ROLE -> {
                                if (p.getRole().toString().equals("Werewolf")) yield ChatColor.DARK_AQUA;
                                else if (p.getRole().toString().equals("Vampire")) yield ChatColor.DARK_PURPLE;
                                else yield ChatColor.AQUA; // Should never be used
                            }
                        }).append(p.getRole().toString());
                        // Original Role
                        if (!p.getRole().toString().equals(p.getOriginalRole().toString())) {
                            addToList.append(ChatColor.GRAY).append(" (originally ");
                            addToList.append(switch (p.getOriginalRole().getWinCond()) {
                                case MAFIA -> ChatColor.RED;
                                case VILLAGE -> ChatColor.DARK_GREEN;
                                case ALONE -> {
                                    if (p.getOriginalRole().toString().equals("Serial Killer")) yield ChatColor.BLUE;
                                    else if (p.getOriginalRole().toString().equals("Trapmaker")) yield ChatColor.DARK_BLUE;
                                    else yield ChatColor.DARK_GRAY; // Should never be used
                                }
                                case SURVIVING -> {
                                    if (p.getOriginalRole().toString().equals("Jester")) yield ChatColor.LIGHT_PURPLE;
                                    else yield ChatColor.YELLOW; // Will be used
                                }
                                case ROLE -> {
                                    if (p.getOriginalRole().toString().equals("Werewolf")) yield ChatColor.DARK_AQUA;
                                    else if (p.getOriginalRole().toString().equals("Vampire")) yield ChatColor.DARK_PURPLE;
                                    else yield ChatColor.AQUA; // Should never be used
                                }
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
