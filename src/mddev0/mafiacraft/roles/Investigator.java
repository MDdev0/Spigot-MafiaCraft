package mddev0.mafiacraft.roles;

import mddev0.mafiacraft.abilities.Ability;

public final class Investigator extends Role {

    public Investigator() {
        super(WinCondition.VILLAGE, false);
        abilities.add(Ability.INVESTIGATE);
    }

    @Override
    public String toString() {
        return "Investigator";
    }
}
