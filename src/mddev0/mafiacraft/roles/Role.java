package mddev0.mafiacraft.roles;

import mddev0.mafiacraft.abilities.Ability;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public abstract class Role {
    public enum WinCondition {
        MAFIA, // TODO: make mafia able to see other mafia
        VILLAGE,
        ALONE,
        SURVIVING
    }
    final WinCondition winCond;
    Set<Ability> abilities = new HashSet<>();
    Map<Ability, Integer> cooldowns = new HashMap<>();

    protected Role(WinCondition win) {
        winCond = win;
    }

    public Set<Ability> getAbilities() {
        return abilities;
    }

    public boolean hasAbility(Ability a) {
        return abilities.contains(a);
    }

    public void tickCooldowns() {
        for (Ability a : cooldowns.keySet()) {
            if (cooldowns.get(a) > 0) cooldowns.put(a, cooldowns.get(a) - 1);
            else cooldowns.remove(a);
        }
    }

    boolean isMafiaSuspect() {
        if (winCond == WinCondition.MAFIA)
            return !(abilities.contains(Ability.CHARISMA));
        else return false;
    }
    abstract boolean isUnholySuspect();
}
