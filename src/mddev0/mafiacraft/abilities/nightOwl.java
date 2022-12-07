package mddev0.mafiacraft.abilities;

import mddev0.mafiacraft.MafiaCraft;
import mddev0.mafiacraft.util.MafiaPlayer;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Map;
import java.util.UUID;

public final class nightOwl extends BukkitRunnable {

    private final MafiaCraft plugin;

    public nightOwl(MafiaCraft plugin) {
      
        this.plugin = plugin;
    }
    
    int worldTime;

    @Override
    public void run() {
        worldTime = plugin.getServer().getWorlds().get(0).getTime();
       
        if (plugin.getServer().getWorlds().get(0).getTime() >= 1000 || <= 13000) { //At 7:00 AM through 7:00 PM
            /
            Map<UUID, MafiaPlayer> living = plugin.getLivingPlayers();
          
            for (Map.Entry<UUID, MafiaPlayer> p : living.entrySet()) {
              
                if (p.getValue().getRole().hasAbility(Ability.nightOwl)) {
                  
                    Player affected = plugin.getServer().getPlayer(p.getKey());
                  
                    if (affected != null && affected.isOnline()) {
                      
                        affected.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 200, 1, false, false, true)); //Repeatedly apply until night
                    }
                }
            }
        }
    }
}
