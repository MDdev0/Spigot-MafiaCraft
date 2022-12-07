package mddev0.mafiacraft.roles;

import mddev0.mafiacraft.abilities.Ability;

public final class Werewolf extends Role {

    public Werewolf() {
        super(WinCondition.ALONE, false);
        abilities.add(Ability.TRANSFORM);
        abilities.add(Ability.RAMPAGE);
        abilities.add(Ability.NEMESIS);
        abilities.add(Ability.BITE);
    }
}
