package mddev0.mafiacraft.roles;

import mddev0.mafiacraft.abilities.Ability;

public final class Veteran extends Role {

    public Veteran() {
        super(WinCondition.VILLAGE, false);
        abilities.add(Ability.RETALIATION);
    }

    @Override
    public String toString() {
        return "Veteran";
    }
}
