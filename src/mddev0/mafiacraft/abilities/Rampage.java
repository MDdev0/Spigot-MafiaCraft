package mddev0.mafiacraft.abilities;

import mddev0.mafiacraft.MafiaCraft;
import mddev0.mafiacraft.player.RoleData;
import mddev0.mafiacraft.player.MafiaPlayer;
import org.bukkit.Bukkit;
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

    @SuppressWarnings("unused")
    @EventHandler
    public void onPlayerKill(PlayerDeathEvent death) {
        if (!plugin.getActive()) return; // DO NOTHING IF NOT ACTIVE!
        Player killer = death.getEntity().getKiller();
        if (killer != null && killer.getType() == EntityType.PLAYER) {
            MafiaPlayer killerMP = plugin.getLivingPlayers().get(killer.getUniqueId());
            if (killerMP.getRole().getAbilities().contains(Ability.RAMPAGE) && (Boolean)killerMP.getRoleData().getData(RoleData.DataType.WEREWOLF_TRANSFORM)) {
                // is a werewolf with rampage who is currently transformed
                killerMP.getRoleData().setData(RoleData.DataType.WEREWOLF_KILLS, (Integer)killerMP.getRoleData().getData(RoleData.DataType.WEREWOLF_KILLS) + 1);
                killer.sendMessage(ChatColor.DARK_RED + "You get stronger with every kill. " + ChatColor.RED + "You have " +
                        killerMP.getRoleData().getData(RoleData.DataType.WEREWOLF_KILLS) + " tonight!");
            }
        }
    }

    @Override
    public void run() {
        if (!plugin.getActive()) return; // DO NOTHING IF NOT ACTIVE!
        for (MafiaPlayer p : plugin.getLivingPlayers().values()) {
            if (p.getRole().getAbilities().contains(Ability.RAMPAGE)) {
                if ((Boolean)p.getRoleData().getData(RoleData.DataType.WEREWOLF_TRANSFORM)) {
                    int level = Math.min(5, (Integer)p.getRoleData().getData(RoleData.DataType.WEREWOLF_KILLS) - 1);
                    if (level >= 0)
                        Objects.requireNonNull(Bukkit.getPlayer(p.getID())).addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 120, level, false, false, true));
                }
            }
        }
    }
}
