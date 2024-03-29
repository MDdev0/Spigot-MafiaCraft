package mddev0.mafiacraft.abilities;

import mddev0.mafiacraft.MafiaCraft;
import mddev0.mafiacraft.player.MafiaPlayer;
import mddev0.mafiacraft.player.Role;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import java.util.*;

public final class Succession implements Listener {

    private final MafiaCraft plugin;

    public Succession(MafiaCraft plugin) {
        this.plugin = plugin;
    }
    @SuppressWarnings("unused")
    @EventHandler
    public void onGodfatherDeath(PlayerDeathEvent death) {
        if (!plugin.getActive()) return; // DO NOTHING IF NOT ACTIVE!
        if (plugin.getPlayerList().get(death.getEntity().getUniqueId()).getRole() == Role.GODFATHER) {
            // Godfather has died, select a new one.
            List<MafiaPlayer> eligible = new ArrayList<>();
            for (Map.Entry<UUID, MafiaPlayer> p : plugin.getLivingPlayers().entrySet()) {
                if (p.getValue().getRole().getAbilities().contains(Ability.SUCCESSION))
                    eligible.add(p.getValue());
            }
            if (!eligible.isEmpty()) {
                // Only act if more than one eligible player is in the game
                int selection = new Random().nextInt(eligible.size());
                eligible.get(selection).changeRole(Role.GODFATHER);
                Objects.requireNonNull(Bukkit.getPlayer(eligible.get(selection).getID())).sendMessage(ChatColor.RED + "The Godfather is dead. You have taken their place.");
            }
        }
    }
}
