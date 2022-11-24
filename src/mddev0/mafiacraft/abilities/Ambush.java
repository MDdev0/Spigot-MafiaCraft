package mddev0.mafiacraft.abilities;

import mddev0.mafiacraft.MafiaCraft;
import mddev0.mafiacraft.util.CombatState;
import mddev0.mafiacraft.util.MafiaPlayer;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public final class Ambush implements Listener {

    private final MafiaCraft plugin;

    public Ambush(MafiaCraft plugin) {
        this.plugin = plugin;
    }

    public void onAttackPlayer(EntityDamageByEntityEvent damage) {
        if (damage.getEntityType() == EntityType.PLAYER &&
                !plugin.getPlayerList().get(damage.getEntity().getUniqueId()).isAttacker()) {
            // Attacked entity is a player who has not attacked
            Player damager = CombatState.findAttackingPlayer(damage);
            if (damager != null) {
                // Only take action if not null
                MafiaPlayer attacker = plugin.getPlayerList().get(damager.getUniqueId());
                if (attacker.getRole().hasAbility(Ability.AMBUSH) && !attacker.onCooldown(Ability.AMBUSH)) {
                    damager.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 600, 1, false, false, true));
                    attacker.startCooldown(Ability.AMBUSH, 0L);
                }
            }
        }
    }
}
