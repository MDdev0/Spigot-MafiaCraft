package mddev0.mafiacraft.abilities;

import mddev0.mafiacraft.MafiaCraft;
import mddev0.mafiacraft.player.StatusData;
import mddev0.mafiacraft.util.CombatState;
import mddev0.mafiacraft.player.MafiaPlayer;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

@SuppressWarnings("unused")
public final class Ambush implements Listener {

    private final MafiaCraft plugin;

    public Ambush(MafiaCraft plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onAttackPlayer(EntityDamageByEntityEvent damage) {
        if (!plugin.getActive()) return; // DO NOTHING IF NOT ACTIVE!
        MafiaPlayer damagerMP = plugin.getLivingPlayers().get(damage.getDamager().getUniqueId());
        MafiaPlayer damagedMP = plugin.getLivingPlayers().get(damage.getEntity().getUniqueId());
        if (damage.getEntity().getType() == EntityType.PLAYER && damagerMP != null &&
                damage.getDamager().getType() == EntityType.PLAYER && damagedMP != null && !damagedMP.getStatus().hasStatus(StatusData.Status.IN_COMBAT)) {
            // Attacked entity is a player who has not attacked
            Player damager = CombatState.findAttackingPlayer(damage);
            if (damager != null) {
                // Only take action if not null
                if (damagerMP.getRole().getAbilities().contains(Ability.AMBUSH) && !damagerMP.getCooldowns().isOnCooldown(Ability.AMBUSH)) {
                    damager.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 600, 1, false, false, true));
                    long waitUntil = plugin.getWorldFullTime() + 24000L;
                    waitUntil = waitUntil - (waitUntil % 24000);
                    damagerMP.getCooldowns().startCooldown(Ability.AMBUSH, waitUntil);
                }
            }
        }
    }
}
