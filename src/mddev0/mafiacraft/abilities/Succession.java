package mddev0.mafiacraft.abilities;

import mddev0.mafiacraft.MafiaCraft;
import mddev0.mafiacraft.roles.Godfather;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

public class Succession implements Listener {

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
            List<UUID> eligible = new ArrayList<>();
            for (UUID key : plugin.getPlayerList().keySet()) {
                if (plugin.getPlayerList().get(key).getRole().hasAbility(Ability.SUCCESSION))
                    eligible.add(key);
            }
            if (eligible.size() != 0) {
                // Only act if more than one eligible player is in the game
                int selection = new Random().nextInt(eligible.size());
                plugin.getPlayerList().get(eligible.get(selection)).changeRole(new Godfather());
            }
        }
    }
}
