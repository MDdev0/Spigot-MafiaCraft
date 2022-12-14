package mddev0.mafiacraft.abilities;

import mddev0.mafiacraft.MafiaCraft;
import mddev0.mafiacraft.util.CombatState;
import mddev0.mafiacraft.util.MafiaPlayer;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public final class Assassination implements Listener {

    private final MafiaCraft plugin;

    public Assassination(MafiaCraft plugin) {
        this.plugin = plugin;
    }

    @SuppressWarnings("unused")
    @EventHandler
    public void onAttackPlayer(EntityDamageByEntityEvent damage) {
        if (!plugin.getActive()) return; // DO NOTHING IF NOT ACTIVE!
        MafiaPlayer damaged = plugin.getLivingPlayers().get(damage.getEntity().getUniqueId());
        if (damage.getEntityType() == EntityType.PLAYER && damaged != null && damaged.isNotAttacker()) {
            // Attacked entity is a player who has not attacked
            Player damager = CombatState.findAttackingPlayer(damage);
            if (damager != null) {
                // Only take action if not null
                MafiaPlayer attacker = plugin.getLivingPlayers().get(damager.getUniqueId());
                if (attacker != null && attacker.getRole().hasAbility(Ability.ASSASSINATION) && !attacker.onCooldown(Ability.ASSASSINATION)) {
                    damager.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 1200, 0, false, false, true));
                    damager.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 1200, 0, false, false, true));
                    attacker.startCooldown(Ability.ASSASSINATION, 0L);
                }
            }
        }
    }
}
