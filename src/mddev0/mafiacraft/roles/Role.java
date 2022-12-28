package mddev0.mafiacraft.roles;

import mddev0.mafiacraft.abilities.Ability;

import java.util.HashSet;
import java.util.Set;

public abstract class Role {
    public enum WinCondition {
        MAFIA,
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

    // I think this breaks some best practices but ya know what it'll work
    public enum RoleClasses {
        GODFATHER(Godfather.class),
        MAFIOSO(Mafioso.class),
        FORGER(Forger.class),
        ASSASSIN(Assassin.class),
        REANIMATOR(Reanimator.class),
        VETERAN(Veteran.class),
        DEPUTY(Deputy.class),
        INVESTIGATOR(Investigator.class),
        LOOKOUT(Lookout.class),
        DOCTOR(Doctor.class),
        APOTHECARY(Apothecary.class),
        DEACON(Deacon.class),
        SERIAL_KILLER(SerialKiller.class),
        TRAPMAKER(Trapmaker.class),
        HUNTER(Hunter.class),
        SORCERER(Sorcerer.class),
        WEREWOLF(Werewolf.class),
        VAMPIRE(Vampire.class),
        JESTER(Jester.class);

        private final Class<?> roleClass;

        RoleClasses(Class<?> roleClass) {
            this.roleClass = roleClass;
        }

        public Class<?> getRoleClass() {
            return roleClass;
        }
    }
}
