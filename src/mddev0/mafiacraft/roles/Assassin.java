package mddev0.mafiacraft.roles;

import mddev0.mafiacraft.abilities.Ability;

public final class Assassin extends Role {

    public Assassin() {
        super(WinCondition.MAFIA, false);
        abilities.add(Ability.ASSASSINATION);
    }
}
