package mddev0.mafiacraft.abilities;

import mddev0.mafiacraft.MafiaCraft;
import mddev0.mafiacraft.roles.Godfather;
import mddev0.mafiacraft.util.MafiaPlayer;
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

    // XXX: If godfather dies and becomes vampire or something, should they be replaced?
    // XXX: Can godfathers be resurrected?
    @EventHandler
    public void onGodfatherDeath(PlayerDeathEvent death) {
        if (plugin.getPlayerList().get(death.getEntity().getUniqueId()).getRole() instanceof Godfather) {
            // Godfather has died, select a new one.
            List<MafiaPlayer> eligible = new ArrayList<>();
            for (Map.Entry<UUID, MafiaPlayer> p : plugin.getLivingPlayers().entrySet()) {
                if (p.getValue().getRole().hasAbility(Ability.SUCCESSION))
                    eligible.add(p.getValue());
            }
            if (eligible.size() != 0) {
                // Only act if more than one eligible player is in the game
                int selection = new Random().nextInt(eligible.size());
                eligible.get(selection).changeRole(new Godfather());
                Objects.requireNonNull(plugin.getServer().getPlayer(eligible.get(selection).getID())).sendMessage(ChatColor.RED + "The Godfather is dead. You have taken their place.");
            }
        }
    }
}
