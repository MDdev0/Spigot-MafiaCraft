package mddev0.mafiacraft.roles;

import mddev0.mafiacraft.abilities.Ability;

public final class Werewolf extends Role {

    private boolean transformed;

    public Werewolf() {
        super(WinCondition.TEAM, true);
        abilities.add(Ability.TRANSFORM);
        abilities.add(Ability.RAMPAGE);
        abilities.add(Ability.BITE);
        abilities.add(Ability.NEMESIS);
    }

    public boolean getTransformed() {
        return transformed;
    }

    public void setTransformed(boolean t) {
        transformed = t;
    }
}
