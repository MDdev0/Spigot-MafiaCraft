package mddev0.mafiacraft.roles;

import mddev0.mafiacraft.abilities.Ability;

public final class Vampire extends Role {

    public Vampire() {
        super(WinCondition.ROLE, true);
        abilities.add(Ability.CONVERT);
        abilities.add(Ability.HUNTING_NIGHT);
        abilities.add(Ability.NIGHT_OWL);
        abilities.add(Ability.STAKED);
    }

    @Override
    public String toString() {
        return "Vampire";
    }
}
