package mddev0.mafiacraft.roles;

import mddev0.mafiacraft.abilities.Ability;

public final class Deacon extends Role {

    public Deacon() {
        super(WinCondition.VILLAGE, false);
        abilities.add(Ability.INQUISITION);
    }

    @Override
    public String toString() {
        return "Deacon";
    }
}
