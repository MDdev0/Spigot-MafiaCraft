package mddev0.mafiacraft.roles;

import mddev0.mafiacraft.abilities.Ability;

import java.util.HashSet;
import java.util.Set;

public abstract class Role {
    public enum WinCondition {
        MAFIA, // TODO: make mafia able to see other mafia
        VILLAGE,
        ALONE,
        SURVIVING,
        ROLE
    }
    private final WinCondition winCond;
    protected final Set<Ability> abilities = new HashSet<>();
    private final boolean unique;

    public Role(WinCondition win, boolean uniqueness) {
        winCond = win;
        unique = uniqueness;
    }

    public Set<Ability> getAbilities() {
        return abilities;
    }

    public boolean hasAbility(Ability a) {
        return abilities.contains(a);
    }

    public boolean isUnique() {
        return unique;
    }

    public WinCondition getWinCond() {
        return winCond;
    }

    @Override
    abstract public String toString();
}
