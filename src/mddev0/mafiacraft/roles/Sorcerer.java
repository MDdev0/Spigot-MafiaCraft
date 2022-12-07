package mddev0.mafiacraft.roles;

import mddev0.mafiacraft.abilities.Ability;

public final class Sorcerer extends Role {

    private Ability selected;

    public Sorcerer() {
        super(WinCondition.SURVIVING, false);
        abilities.add(Ability.SPELL_BOOK);
        abilities.add(Ability.SCATTER);
        abilities.add(Ability.TOADIFY);
        abilities.add(Ability.FOG_OF_WAR);
        abilities.add(Ability.VANISH);
    }

    public Ability getSelected() {
        return selected;
    }

    public void nextAbility() {
        selected = abilities.iterator().next();
    }
}
