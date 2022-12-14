package mddev0.mafiacraft.roles;

import mddev0.mafiacraft.abilities.Ability;

public final class Mafioso extends Role {

    public Mafioso() {
        super(WinCondition.MAFIA, false);
        abilities.add(Ability.SUCCESSION);
    }

    @Override
    public String toString() {
        return "Mafioso";
    }
}
