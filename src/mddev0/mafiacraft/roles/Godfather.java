package mddev0.mafiacraft.roles;

import mddev0.mafiacraft.abilities.Ability;

public class Godfather extends Role {

    public Godfather() {
        super(WinCondition.MAFIA);
        abilities.add(Ability.PROTECTION);
        abilities.add(Ability.CHARISMA);
    }

    @Override
    boolean isUnholySuspect() {
        return false;
    }
}
