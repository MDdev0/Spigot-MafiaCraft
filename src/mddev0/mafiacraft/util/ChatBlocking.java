package mddev0.mafiacraft.util;

import mddev0.mafiacraft.MafiaCraft;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ChatBlocking implements Listener {

    private final MafiaCraft plugin;

    public ChatBlocking(MafiaCraft plugin) {
        this.plugin = plugin;
    }

    @SuppressWarnings("unused")
    @EventHandler
    public void onChatSend(AsyncPlayerChatEvent chat) {
        if (plugin.getConfig().getBoolean("blockSpectatorChat") && plugin.getActive()) {
            if (chat.getPlayer().getGameMode() == GameMode.SPECTATOR && !chat.getPlayer().isOp()) {
                chat.setCancelled(true);
                chat.getPlayer().sendMessage(ChatColor.GRAY + "You are unable to send chat while spectating.");
            }
        }
    }
}
