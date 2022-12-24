package mddev0.mafiacraft.commands;

import mddev0.mafiacraft.MafiaCraft;
import mddev0.mafiacraft.roles.*;
import mddev0.mafiacraft.util.MafiaPlayer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

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

        switch (args[0]) {
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
                sender.sendMessage(ChatColor.RED + "Not implemented yet.");
                return true;
            }
            case "start" -> {
                plugin.setActive(true);
                sender.sendMessage(ChatColor.GREEN + "The game has been started.");
                return true;
            }
            case "stop" -> {
                plugin.setActive(false);
                sender.sendMessage("The game has been stopped.");
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
                for (MafiaPlayer p : plugin.getPlayerList().values()) {
                    sender.sendMessage(ChatColor.GRAY + Bukkit.getOfflinePlayer(p.getID()).getName() + " | " + p.getRole().toString() + " | " + p.isLiving());
                }
            }
        }
        return false;
    }
}
