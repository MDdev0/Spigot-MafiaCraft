package mddev0.mafiacraft.roles;

import mddev0.mafiacraft.abilities.Ability;

public final class Reanimator extends Role {

    public Reanimator() {
        super(WinCondition.VILLAGE, true);
        abilities.add(Ability.REANIMATION);
    }
}
