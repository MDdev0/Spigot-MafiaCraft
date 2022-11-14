package mddev0.mafiacraft.roles;

import mddev0.mafiacraft.MafiaCraft;
import mddev0.mafiacraft.abilities.Ability;

public final class Deputy extends Role {

    public Deputy() {
        super(WinCondition.VILLAGE, false);
        abilities.add(Ability.HIGH_NOON);
        abilities.add(Ability.MARKSMAN);
    }
}
