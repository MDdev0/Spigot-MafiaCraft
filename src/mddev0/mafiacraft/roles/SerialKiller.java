package mddev0.mafiacraft.roles;

import mddev0.mafiacraft.abilities.Ability;

public final class SerialKiller extends Role {

    public SerialKiller() {
        super(WinCondition.ALONE, false);
        abilities.add(Ability.PROTECTION);
        abilities.add(Ability.AMBUSH);
    }
}
