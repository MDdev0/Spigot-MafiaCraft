package mddev0.mafiacraft.roles;

import mddev0.mafiacraft.abilities.Ability;

public final class Trapmaker extends Role {

    public Trapmaker() {
        super(WinCondition.ALONE, false);
        abilities.add(Ability.THIS_IS_FINE);
        abilities.add(Ability.DODGE_ROLL);
    }
}
