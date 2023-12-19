package mddev0.mafiacraft.abilities;

import mddev0.mafiacraft.MafiaCraft;
import mddev0.mafiacraft.player.MafiaPlayer;
import mddev0.mafiacraft.player.StatusData;
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

    @SuppressWarnings("unused")
    @EventHandler
    public void onPlayerAttacked(EntityDamageByEntityEvent damage) {
        if (!plugin.getActive()) return; // DO NOTHING IF NOT ACTIVE!
        MafiaPlayer attacked = plugin.getLivingPlayers().get(damage.getEntity().getUniqueId());
        if (damage.getEntityType() == EntityType.PLAYER && attacked != null &&
                attacked.getRole().getAbilities().contains(Ability.PROTECTION)) {
            // Player attacked has protection ability
            if (!attacked.getStatus().hasStatus(StatusData.Status.IN_COMBAT) && !attacked.getCooldowns().isOnCooldown(Ability.PROTECTION)) {
                // Does not trigger if player was the attacker or if ability is on cooldown
                if (CombatState.findAttackingPlayer(damage) != null) {
                    // TRIGGER ABILITY
                    Player toProtect = (Player) damage.getEntity();
                    toProtect.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE,600,1,false,false,true));
                    long waitUntil = plugin.getServer().getWorlds().get(0).getFullTime() + 24000L;
                    waitUntil = waitUntil - (waitUntil % 24000);
                    attacked.getCooldowns().startCooldown(Ability.PROTECTION, waitUntil);
                }
            }
        }
    }
}

