package mddev0.mafiacraft.commands;

import mddev0.mafiacraft.MafiaCraft;
import mddev0.mafiacraft.roles.*;
import mddev0.mafiacraft.util.GameRandomizer;
import mddev0.mafiacraft.util.MafiaPlayer;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.logging.Level;

public class MafiaCraftAdminCMD implements CommandExecutor {

    private final MafiaCraft plugin;

    public MafiaCraftAdminCMD(MafiaCraft plugin) {
        this.plugin = plugin;
    }

    // Please ignore the @SuppressWarnings. If you ignore them they don't exist
    @Override
    public boolean onCommand(CommandSender sender, @SuppressWarnings("NullableProblems") Command cmd, @SuppressWarnings("NullableProblems") String label, @SuppressWarnings("NullableProblems") String[] args) {
        if (!sender.isOp()) {
            sender.sendMessage(ChatColor.RED + "Sorry, but you do not have permission to configure MafiaCraft.");
            return true;
        }

        if (args.length < 1) {
            sender.sendMessage(ChatColor.RED + "Missing argument: <setrole | removeplayer | randomize | start | stop | revive | list>");
            return false;
        }

        switch (args[0].toLowerCase()) {
            case "setrole" -> {
                if (args.length < 3) {
                    sender.sendMessage(ChatColor.RED + "Missing arguments: <player> <(Role Name)>");
                    return false;
                }
                Player p = Bukkit.getPlayer(args[1]);
                if (p == null) {
                    sender.sendMessage(ChatColor.RED + args[1] + " is not online.");
                    return true;
                }
                if (plugin.getPlayerList().containsKey(p.getUniqueId())) {
                    MafiaPlayer play = plugin.getPlayerList().get(p.getUniqueId());
                    play.changeRole(switch (args[2].toLowerCase()) {
                        case "godfather" -> new Godfather();
                        case "mafioso" -> new Mafioso();
                        case "forger" -> new Forger();
                        case "assassin" -> new Assassin();
                        case "reanimator" -> new Reanimator();
                        case "veteran" -> new Veteran();
                        case "deputy" -> new Deputy();
                        case "investigator" -> new Investigator();
                        case "lookout" -> new Lookout();
                        case "doctor" -> new Doctor();
                        case "apothecary" -> new Apothecary();
                        case "deacon" -> new Deacon();
                        case "serialkiller" -> new SerialKiller();
                        case "trapmaker" -> new Trapmaker();
                        case "hunter" -> new Hunter(plugin, p.getUniqueId());
                        case "sorcerer" -> new Sorcerer();
                        case "werewolf" -> new Werewolf();
                        case "vampire" -> new Vampire();
                        case "jester" -> new Jester();
                        default -> null;
                    });
                    if (play.getRole() == null) {
                        sender.sendMessage(ChatColor.RED + "Invalid role name provided.");
                        return true;
                    }
                    sender.sendMessage(ChatColor.YELLOW + "Changed " + args[1] + "'s role to " + play.getRole().toString() + ".");
                } else {
                    MafiaPlayer play = new MafiaPlayer(plugin, p.getUniqueId(), switch (args[2].toLowerCase()) {
                        case "godfather" -> new Godfather();
                        case "mafioso" -> new Mafioso();
                        case "forger" -> new Forger();
                        case "assassin" -> new Assassin();
                        case "reanimator" -> new Reanimator();
                        case "veteran" -> new Veteran();
                        case "deputy" -> new Deputy();
                        case "investigator" -> new Investigator();
                        case "lookout" -> new Lookout();
                        case "doctor" -> new Doctor();
                        case "apothecary" -> new Apothecary();
                        case "deacon" -> new Deacon();
                        case "serialkiller" -> new SerialKiller();
                        case "trapmaker" -> new Trapmaker();
                        case "hunter" -> new Hunter(plugin, p.getUniqueId());
                        case "sorcerer" -> new Sorcerer();
                        case "werewolf" -> new Werewolf();
                        case "vampire" -> new Vampire();
                        case "jester" -> new Jester();
                        default -> null;
                    });
                    if (play.getRole() == null) {
                        sender.sendMessage(ChatColor.RED + "Invalid role name provided.");
                        return true;
                    }
                    plugin.getPlayerList().put(play.getID(), play);
                    sender.sendMessage(ChatColor.GREEN + "Added " + args[1] + " to the game with the role " + play.getRole().toString() + ".");
                }
            }
            case "removeplayer" -> {
                if (args.length < 2) {
                    sender.sendMessage(ChatColor.RED + "Missing arguments: <player>");
                    return false;
                }
                Player p = Bukkit.getPlayer(args[1]);
                if (p == null) {
                    sender.sendMessage(ChatColor.RED + args[1] + " is not online.");
                    return true;
                }
                plugin.getPlayerList().remove(p.getUniqueId());
                sender.sendMessage(ChatColor.GREEN + args[1] + " has been removed from the game.");
                return true;
            }
            case "randomize" -> {
                if (args.length < 2) {
                    sender.sendMessage(ChatColor.RED + "Missing arguments: <add | addall | remove | removeall | list | start>");
                    return false;
                }
                switch (args[1].toLowerCase()) {
                    case "add" -> {
                        if (args.length < 3) {
                            sender.sendMessage(ChatColor.RED + "Missing arguments: <player>");
                            return false;
                        }
                        Player player = Bukkit.getPlayer(args[2]);
                        if (player == null) {
                            sender.sendMessage(ChatColor.RED + args[2] + " is not online.");
                            return true;
                        }
                        if (plugin.getRandomizer().addPlayer(player.getName())) {
                            sender.sendMessage(ChatColor.GREEN + args[2] + " has been added to the list of players to randomize.");
                        } else {
                            sender.sendMessage(ChatColor.YELLOW + args[2] + " was already marked to be randomized.");
                        }
                        return true;
                    }
                    case "addall" -> {
                        sender.sendMessage(ChatColor.GREEN + "Added all " + plugin.getRandomizer().addAllOffline() + " players who have ever joined to the list of players to randomize.");
                        return true;
                    }
                    case "remove" -> {
                        if (args.length < 3) {
                            sender.sendMessage(ChatColor.RED + "Missing arguments: <player>");
                            return false;
                        }
                        OfflinePlayer player = Bukkit.getPlayer(args[2]);
                        if (player == null) {
                            try {
                                player = Bukkit.getOfflinePlayer(UUID.fromString(args[2]));
                            } catch (IllegalArgumentException e) {
                                sender.sendMessage(ChatColor.RED + args[2] + " is not online.");
                                return true;
                            }
                        }
                        if (plugin.getRandomizer().removePlayer(player.getName())) {
                            sender.sendMessage(ChatColor.GREEN + args[2] + " has been removed from the list of players to randomize.");
                        } else {
                            sender.sendMessage(ChatColor.YELLOW + args[2] + " was not marked to be randomized.");
                        }
                        return true;
                    }
                    case "removeall" -> {
                        plugin.getRandomizer().removeAll();
                        sender.sendMessage(ChatColor.GREEN + "The list of players to be randomized was cleared.");
                        return true;
                    }
                    case "list" -> {
                        List<OfflinePlayer> playerList = plugin.getRandomizer().getPlayers();
                        sender.sendMessage(ChatColor.AQUA + "Listing " + playerList.size() + " players who are designated to be randomized:");
                        for (OfflinePlayer p : playerList) {
                            TextComponent message = new TextComponent(ChatColor.GRAY + p.getName() + " (" + ChatColor.UNDERLINE + p.getUniqueId() + ChatColor.RESET + ChatColor.GRAY + ")");
                            message.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(ChatColor.YELLOW + "Copy to Clipboard")));
                            message.setClickEvent(new ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, p.getUniqueId().toString()));
                            sender.spigot().sendMessage(message);
                        }
                        return true;
                    }
                    case "start" -> {
                        sender.sendMessage(ChatColor.DARK_PURPLE + "Starting Randomization... " + ChatColor.LIGHT_PURPLE + ChatColor.ITALIC + "(this may take a moment)");
                        Bukkit.getLogger().log(Level.INFO, "[MafiaCraft] Randomization task starting");
                        final Player caller = Bukkit.getPlayer(sender.getName());
                        final GameRandomizer randomizer = plugin.getRandomizer();
                        AtomicBoolean status = new AtomicBoolean(true);
                        AtomicReference<String> statusMsg = new AtomicReference<>();
                        // NO SYNCHRONOUS BUKKIT METHOD CALLS ALLOWED -----------------
                        Bukkit.getScheduler().runTaskAsynchronously(this.plugin, () -> {
                            // Asynchronous randomization method
                            try {
                                randomizer.randomizeGame();
                            } catch (GameRandomizer.RandomizationException problem) {
                                status.set(false);
                                statusMsg.set(problem.getMessage());
                            }
                            // Return to synchronous domain
                            Bukkit.getScheduler().runTask(plugin, () -> {
                                // SYNCHRONOUS BUKKIT METHOD CALLS ALLOWED AGAIN ------
                                if (caller == null) return; // offline check
                                if (status.get()) {
                                    caller.sendMessage(ChatColor.DARK_GREEN + "Randomization Complete! " + ChatColor.GRAY + "Add players to the randomizer again to re-roll their roles.");
                                    caller.sendMessage(ChatColor.DARK_AQUA + "Be careful not to check roles if you are playing to avoid being spoiled!");

                                    // Reveal roles to players
                                    for (OfflinePlayer offp : randomizer.getPrevPlayers()) {
                                        if (offp.isOnline()) {
                                            ((Player) offp).playSound(((Player) offp).getLocation(), Sound.BLOCK_PORTAL_TRIGGER, 1.0f, 1.0f);
                                            ((Player) offp).sendTitle(ChatColor.BOLD + "Your Role Is...", "", 20, 60, 20);
                                        }
                                    }
                                    Bukkit.getScheduler().runTaskLater(plugin, () -> {
                                        // Final role announcement
                                        for (OfflinePlayer offp : randomizer.getPrevPlayers()) {
                                            MafiaPlayer p = plugin.getPlayerList().get(offp.getUniqueId());
                                            if (offp.isOnline() && p != null) {
                                                ((Player) offp).playSound(((Player) offp).getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.0f);
                                                String roleTitle = switch (p.getRole().getWinCond()) {
                                                    case MAFIA -> ChatColor.RED;
                                                    case VILLAGE -> ChatColor.DARK_GREEN;
                                                    case ALONE -> {
                                                        if (p.getRole().toString().equals("Serial Killer"))
                                                            yield ChatColor.BLUE;
                                                        else if (p.getRole().toString().equals("Trapmaker"))
                                                            yield ChatColor.DARK_BLUE;
                                                        else yield ChatColor.DARK_GRAY; // Should never be used
                                                    }
                                                    case SURVIVING -> {
                                                        if (p.getRole().toString().equals("Jester"))
                                                            yield ChatColor.LIGHT_PURPLE;
                                                        else yield ChatColor.YELLOW; // Will be used
                                                    }
                                                    case ROLE -> {
                                                        if (p.getRole().toString().equals("Werewolf"))
                                                            yield ChatColor.DARK_AQUA;
                                                        else if (p.getRole().toString().equals("Vampire"))
                                                            yield ChatColor.DARK_PURPLE;
                                                        else yield ChatColor.AQUA; // Should never be used
                                                    }
                                                } + p.getRole().toString();
                                                String subtitle = ChatColor.GRAY + "Use " + ChatColor.GOLD + "/mafiacraft" + ChatColor.GRAY + " or " + ChatColor.GOLD + "/mafia" + ChatColor.GRAY + " for more info.";
                                                ((Player) offp).sendTitle(roleTitle, subtitle, 20, 120, 20);
                                            }
                                        }
                                        Bukkit.getLogger().log(Level.INFO, "[MafiaCraft] Role announcement complete");
                                    }, 100L);
                                    Bukkit.getLogger().log(Level.INFO, "[MafiaCraft] Starting role announcement");
                                } else { // Command failed
                                    caller.sendMessage(ChatColor.DARK_RED + "Failed to randomize: " + ChatColor.RED + statusMsg.get());
                                    Bukkit.getLogger().log(Level.WARNING, "[MafiaCraft] Randomization Failure: " + statusMsg);
                                }
                                Bukkit.getLogger().log(Level.INFO, "[MafiaCraft] Randomization has returned to main thread");
                            });
                        });
                        return true;
                    }
                }
                return false;
            }
            case "start" -> {
                plugin.setActive(true);
                sender.sendMessage(ChatColor.GREEN + "The game has been started.");
                return true;
            }
            case "stop" -> {
                plugin.setActive(false);
                sender.sendMessage(ChatColor.YELLOW + "The game has been stopped.");
                return true;
            }
            case "revive" -> { // TODO: FIX?
                if (args.length < 2) {
                    sender.sendMessage(ChatColor.RED + "Missing arguments: <player>");
                    return false;
                }
                Player p = Bukkit.getPlayer(args[1]);
                if (p == null) {
                    sender.sendMessage(ChatColor.RED + args[1] + " is not online.");
                    return true;
                }
                plugin.getPlayerList().get(p.getUniqueId()).makeAlive();
                // Code to revive and unhide online players
                if (p.getGameMode() == GameMode.SPECTATOR) {
                    // player needs to be respawned
                    Location toSpawn = (p.getBedSpawnLocation() == null) ? plugin.getServer().getWorlds().get(0).getSpawnLocation() : p.getBedSpawnLocation();
                    p.teleport(toSpawn);
                }
                p.setGameMode(GameMode.SURVIVAL);
                // all dead players should be hidden, p should be unhidden
                for (Player other : plugin.getServer().getOnlinePlayers()) {
                    other.showPlayer(plugin, p);
                    MafiaPlayer spec = plugin.getPlayerList().get(other.getUniqueId());
                    if (spec == null || !spec.isLiving()) {
                        p.hidePlayer(plugin, other);
                    }
                }
                Bukkit.broadcastMessage(ChatColor.YELLOW + p.getName() + " joined the game");
                sender.sendMessage(ChatColor.GREEN + args[1] + " has been revived.");
                return true;
            }
            case "list" -> {
                sender.sendMessage(ChatColor.AQUA + "Listing " + plugin.getPlayerList().size() + " MafiaCraft players:");
                for (MafiaPlayer p : plugin.getPlayerList().values()) {
                    sender.sendMessage(ChatColor.GRAY + Bukkit.getOfflinePlayer(p.getID()).getName() + " | " + p.getRole().toString() + " | Living: " + p.isLiving());
                }
            }
            default -> {
                sender.sendMessage(ChatColor.RED + "Incorrect argument: <setrole | removeplayer | randomize | start | stop | revive | list>");
                return false;
            }
        }
        return false;
    }
}
