package mddev0.mafiacraft.roles;

import mddev0.mafiacraft.abilities.Ability;

public final class Lookout extends Role {

    public Lookout() {
        super(WinCondition.VILLAGE, false);
        abilities.add(Ability.WATCH);
        abilities.add(Ability.PERIPHERALS);
        abilities.add(Ability.CLEAR_SIGHT);
    }

    @Override
    public String toString() {
        return "Lookout";
    }
}
