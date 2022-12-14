package mddev0.mafiacraft.commands;

import mddev0.mafiacraft.MafiaCraft;
import mddev0.mafiacraft.gui.InfoGUI;
import org.bukkit.Bukkit;
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
        InfoGUI gui = new InfoGUI(plugin, plugin.getPlayerList().get(Bukkit.getPlayer(sender.getName()).getUniqueId()));
        gui.open(Bukkit.getPlayer(sender.getName()));
        return true;
    }
}
