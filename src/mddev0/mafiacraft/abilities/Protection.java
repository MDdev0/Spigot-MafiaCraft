package mddev0.mafiacraft.abilities;

import mddev0.mafiacraft.MafiaCraft;
import mddev0.mafiacraft.util.MafiaPlayer;
import mddev0.mafiacraft.util.CombatState;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public final class Protection implements Listener {

    private final MafiaCraft plugin;

    public Protection(MafiaCraft plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerAttacked(EntityDamageByEntityEvent damage) {
        if (!plugin.getActive()) return; // DO NOTHING IF NOT ACTIVE!
        if (damage.getEntityType() == EntityType.PLAYER &&
                plugin.getPlayerList().get(damage.getEntity().getUniqueId()).getRole().hasAbility(Ability.PROTECTION)) {
            // Player attacked has protection ability
            MafiaPlayer attacked = plugin.getPlayerList().get(damage.getEntity().getUniqueId());
            if (!attacked.isAttacker() && !attacked.onCooldown(Ability.PROTECTION)) {
                // Does not trigger if player was the attacker or if ability is on cooldown
                if (CombatState.findAttackingPlayer(damage) != null) {
                    // TRIGGER ABILITY
                    Player toProtect = (Player) damage.getEntity();
                    toProtect.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE,600,1,false,false,true));
                    attacked.startCooldown(Ability.PROTECTION, 0L);
                }
            }
        }
    }
}

