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

public final class Retaliation implements Listener {

    private final MafiaCraft plugin;

    public Retaliation(MafiaCraft plugin) {
        this.plugin = plugin;
    }

    @SuppressWarnings("unused")
    @EventHandler
    public void onPlayerAttacked(EntityDamageByEntityEvent damage) {
        if (!plugin.getActive()) return; // DO NOTHING IF NOT ACTIVE!
        MafiaPlayer attacked = plugin.getLivingPlayers().get(damage.getEntity().getUniqueId());
        if (damage.getEntityType() == EntityType.PLAYER && attacked != null &&
                attacked.getRole().getAbilities().contains(Ability.RETALIATION)) {
            // Player attacked has retaliation ability
            if (!attacked.getStatus().hasStatus(StatusData.Status.IN_COMBAT) && !attacked.getCooldowns().isOnCooldown(Ability.RETALIATION)) {
                // Does not trigger if player was the attacker or if ability is on cooldown
                if (CombatState.findAttackingPlayer(damage) != null) {
                    // was attacked by a player
                    Player toRetaliate = (Player) damage.getEntity();
                    toRetaliate.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE,600,1,false,false,true));
                    toRetaliate.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE,600,1,false,false,true));
                    long waitUntil = plugin.getWorldFullTime() + 24000L;
                    waitUntil = waitUntil - (waitUntil % 24000); // Cooldown ends at dawn
                    attacked.getCooldowns().startCooldown(Ability.RETALIATION, waitUntil);
                }
            }
        }
    }
}
