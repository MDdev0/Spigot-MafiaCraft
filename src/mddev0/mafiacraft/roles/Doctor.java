package mddev0.mafiacraft.roles;

import mddev0.mafiacraft.abilities.Ability;

public final class Doctor extends Role {

    public Doctor() {
        super(WinCondition.VILLAGE, false);
        abilities.add(Ability.RESCUE);
    }

    @Override
    public String toString() {
        return "Doctor";
    }
}
