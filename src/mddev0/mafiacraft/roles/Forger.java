package mddev0.mafiacraft.roles;

import mddev0.mafiacraft.abilities.Ability;

public final class Forger extends Role {

    public Forger() {
        super(WinCondition.MAFIA, false);
        abilities.add(Ability.FORGERY);
    }
}
