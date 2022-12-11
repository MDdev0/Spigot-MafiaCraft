package mddev0.mafiacraft.abilities;

import mddev0.mafiacraft.MafiaCraft;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityShootBowEvent;

public final class Marksman implements Listener {

    private final MafiaCraft plugin;

    public Marksman(MafiaCraft plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onArrowShoot(EntityShootBowEvent shot) {
        if (!plugin.getActive()) return; // DO NOTHING IF NOT ACTIVE!
        if (shot.getEntity().getType() != EntityType.ARROW) return;
        // Only shots from players allowed
        if (plugin.getPlayerList().get(shot.getEntity().getUniqueId()).getRole().hasAbility(Ability.MARKSMAN)
                && shot.getProjectile().getType() != EntityType.ARROW) {
            Arrow arrow = (Arrow) shot.getProjectile();
            arrow.setDamage(arrow.getDamage() + 2.0); // Add one heart
            // SCUFFED: Probably make that a config value at some point
        }
    }
}
