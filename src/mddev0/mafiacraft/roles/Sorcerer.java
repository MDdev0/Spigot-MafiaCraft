package mddev0.mafiacraft.roles;

import mddev0.mafiacraft.abilities.Ability;

public final class Sorcerer extends Role {

    public Sorcerer() {
        super(WinCondition.SURVIVING, false);
        abilities.add(Ability.SCATTER);
        abilities.add(Ability.TOADIFY);
        abilities.add(Ability.FOG_OF_WAR);
        abilities.add(Ability.VANISH);
    }
}
