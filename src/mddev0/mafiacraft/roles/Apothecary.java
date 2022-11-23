package mddev0.mafiacraft.roles;

import mddev0.mafiacraft.abilities.Ability;

public final class Apothecary extends Role {

    public Apothecary() {
        super(WinCondition.VILLAGE, true);
        abilities.add(Ability.AMBROSIA);
    }
}
