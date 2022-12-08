package mddev0.mafiacraft.abilities;

import mddev0.mafiacraft.MafiaCraft;
import mddev0.mafiacraft.roles.Werewolf;
import mddev0.mafiacraft.util.MafiaPlayer;
import org.bukkit.ChatColor;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Objects;

public final class Rampage extends BukkitRunnable implements Listener {

    private final MafiaCraft plugin;

    public Rampage(MafiaCraft plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerKill(PlayerDeathEvent death) {
        Player killer = death.getEntity().getKiller();
        if (killer != null && killer.getType() == EntityType.PLAYER) {
            MafiaPlayer killerMP = plugin.getLivingPlayers().get(killer.getUniqueId());
            if (killerMP.getRole().hasAbility(Ability.RAMPAGE)) {
                // is a werewolf with rampage
                ((Werewolf) killerMP.getRole()).incrementKills();
                killer.sendMessage(ChatColor.DARK_RED + "You get stronger with every kill. " + ChatColor.RED + "You have " +
                        ((Werewolf) killerMP.getRole()).getKills() + " tonight!");
            }
        }
    }

    @Override
    public void run() {
        for (MafiaPlayer p : plugin.getLivingPlayers().values()) {
            if (p.getRole().hasAbility(Ability.RAMPAGE)) {
                if (((Werewolf) p.getRole()).getTransformed()) {
                    int level = Math.min(5, ((Werewolf) p.getRole()).getKills() - 1);
                    if (level >= 0)
                        Objects.requireNonNull(plugin.getServer().getPlayer(p.getID())).addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 120, level, false, false, true));
                }
            }
        }
    }
}
