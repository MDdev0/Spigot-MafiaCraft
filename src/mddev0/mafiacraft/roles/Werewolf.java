package mddev0.mafiacraft.roles;

import mddev0.mafiacraft.abilities.Ability;

public final class Werewolf extends Role {

    private boolean transformed; // Saved Member
    private int killsWhileTransformed; // Saved Member

    public Werewolf() {
        super(WinCondition.ROLE, true);
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
        if (t) killsWhileTransformed = 0; // reset kills
    }

    public void incrementKills() {
        killsWhileTransformed++;
    }

    public int getKills() {
        return killsWhileTransformed;
    }

    @Override
    public String toString() {
        return "Werewolf";
    }
}
