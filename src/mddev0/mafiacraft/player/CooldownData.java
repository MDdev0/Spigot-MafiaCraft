package mddev0.mafiacraft.player;

import mddev0.mafiacraft.MafiaCraft;
import mddev0.mafiacraft.abilities.Ability;

import java.util.HashMap;
import java.util.Map;

public class CooldownData {
    private final HashMap<Ability, Long> cooldownMap;

    private final MafiaCraft plugin;

    public CooldownData(MafiaCraft plugin) {
        this.plugin = plugin;
        this.cooldownMap = new HashMap<>();
    }

    public CooldownData(MafiaCraft plugin, CooldownDataSave saveData) {
        this.plugin = plugin;
        this.cooldownMap = saveData.cooldownMap();
    }

    public void startCooldown(Ability ability, Long timeToEnd) {
        cooldownMap.put(ability, timeToEnd);
    }

    public boolean isOnCooldown(Ability ability) {
        Long cooldown = cooldownMap.get(ability);
        if (cooldown == null || cooldown < plugin.getServer().getWorlds().get(0).getFullTime()) {
            // Cooldown has expired or is unset
            cooldownMap.remove(ability);
            return false;
        } else {
            // Is still on cooldown
            return true;
        }
    }

    public record CooldownDataSave(HashMap<Ability, Long> cooldownMap) {}
    public CooldownDataSave getDataSave() {
        Long curTime = plugin.getServer().getWorlds().get(0).getFullTime();
        for (Map.Entry<Ability, Long> e : cooldownMap.entrySet())
            if (e.getValue() < curTime) cooldownMap.remove(e.getKey());
        return new CooldownDataSave(cooldownMap);
    }
}
