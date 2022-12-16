package mddev0.mafiacraft.commands;

import mddev0.mafiacraft.MafiaCraft;
import mddev0.mafiacraft.gui.InfoGUI;
import mddev0.mafiacraft.util.MafiaPlayer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class MafiaCraftCMD implements CommandExecutor {

    private final MafiaCraft plugin;

    public MafiaCraftCMD(MafiaCraft plugin) {
        this.plugin = plugin;
    }

    // Please ignore the @SuppressWarnings. If you ignore them they don't exist
    @Override
    public boolean onCommand(CommandSender sender, @SuppressWarnings("NullableProblems") Command cmd, @SuppressWarnings("NullableProblems") String label, @SuppressWarnings("NullableProblems") String[] args) {
        MafiaPlayer player = plugin.getPlayerList().get(Bukkit.getPlayer(sender.getName()).getUniqueId());
        if (player == null) {
            sender.sendMessage(ChatColor.GRAY + "You are not currently in the MafiaCraft game!");
            return true;
        }
        InfoGUI gui = new InfoGUI(plugin, player);
        gui.open(Bukkit.getPlayer(sender.getName()));
        Bukkit.getPluginManager().registerEvents(gui, plugin);
        return true;
    }
}
