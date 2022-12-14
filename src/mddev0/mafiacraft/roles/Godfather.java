package mddev0.mafiacraft.roles;

import mddev0.mafiacraft.abilities.Ability;

public final class Godfather extends Role {

    public Godfather() {
        super(WinCondition.MAFIA, true);
        abilities.add(Ability.PROTECTION);
        abilities.add(Ability.CHARISMA);
    }

    @Override
    public String toString() {
        return "Godfather";
    }
}
