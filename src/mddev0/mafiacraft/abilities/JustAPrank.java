package mddev0.mafiacraft.abilities;

import mddev0.mafiacraft.MafiaCraft;
import mddev0.mafiacraft.player.Role;
import mddev0.mafiacraft.player.RoleData;
import mddev0.mafiacraft.player.MafiaPlayer;
import mddev0.mafiacraft.player.StatusData;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.logging.Level;

public final class JustAPrank implements Listener {

    private final MafiaCraft plugin;

    public JustAPrank(MafiaCraft plugin) {
        this.plugin = plugin;
    }

    @SuppressWarnings("unused")
    @EventHandler
    public void onJesterKill(PlayerDeathEvent death) {
        if (!plugin.getActive()) return; // DO NOTHING IF NOT ACTIVE!
        if (death.getEntity().getKiller() != null) {
            MafiaPlayer jest = plugin.getPlayerList().get(death.getEntity().getUniqueId());
            MafiaPlayer killer = plugin.getLivingPlayers().get(death.getEntity().getKiller().getUniqueId());
            if (jest != null && jest.getRole().getAbilities().contains(Ability.JUST_A_PRANK) &&
                    !(Boolean)jest.getRoleData().getData(RoleData.DataType.JESTER_ABILITY_USED) &&
                    killer != null && killer.getRole().getAlignment() == Role.Team.VILLAGE) {
                if (!jest.getStatus().hasStatus(StatusData.Status.IN_COMBAT)) {
                    // For Jester
                    jest.makeAlive();
                    Player jester = Bukkit.getPlayer(jest.getID());
                    assert jester != null; // jester must be online to have been killed
                    jester.sendMessage(ChatColor.DARK_GREEN + "You were killed by a member of the Village! You've triggered your effect!");
                    jest.getRoleData().setData(RoleData.DataType.JESTER_ABILITY_USED, true);
                    death.setKeepInventory(true);
                    death.setKeepLevel(true);
                    // For other player
                    Player toAffect = death.getEntity().getKiller();
                    toAffect.sendMessage(ChatColor.GRAY + "You killed the " + ChatColor.LIGHT_PURPLE + "Jester" + ChatColor.GRAY + "!");
                    jester.getWorld().createExplosion(jester.getLocation(), 3.0f, false, false, jester);
                    toAffect.addPotionEffect(new PotionEffect(PotionEffectType.WITHER, PotionEffect.INFINITE_DURATION, 2, false, true, true));
                    toAffect.getWorld().spawnParticle(Particle.VILLAGER_ANGRY, toAffect.getLocation().add(0, 1, 0), 10, 1, 2, 1);
                    // For all others
                    jester.getWorld().strikeLightningEffect(jester.getLocation());
                    plugin.getServer().broadcastMessage(jester.getName() + " has been revealed as the " + ChatColor.LIGHT_PURPLE + "Jester" + ChatColor.RESET + "!");
                    plugin.getLogger().log(Level.INFO, "The jester (" + jester.getName() + ") was killed by " + toAffect.getName());
                    jester.setDisplayName("[" + ChatColor.LIGHT_PURPLE + "Jester" + ChatColor.RESET + "] " + jester.getDisplayName());
                    jester.setPlayerListName("[" + ChatColor.LIGHT_PURPLE + "Jester" + ChatColor.RESET + "] " + jester.getPlayerListName());
                }
            }
        }
    }
}
