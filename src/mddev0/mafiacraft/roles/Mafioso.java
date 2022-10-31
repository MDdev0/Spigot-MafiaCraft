package mddev0.mafiacraft.roles;

import mddev0.mafiacraft.abilities.Ability;

public class Mafioso extends Role {

    public Mafioso() {
        super(WinCondition.MAFIA);
        abilities.add(Ability.SUCCESSION);
    }
}
