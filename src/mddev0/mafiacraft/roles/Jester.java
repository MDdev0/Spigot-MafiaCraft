package mddev0.mafiacraft.roles;

import mddev0.mafiacraft.abilities.Ability;

public final class Jester extends Role {

    private boolean hasActivatedAbility;

    public Jester(){
        super(WinCondition.SURVIVING, true);
        abilities.add(Ability.JUST_A_PRANK);
        hasActivatedAbility = false;
    }

    public void activate() {
        this.hasActivatedAbility = true;
    }

    public boolean getAbilityActivated() {
        return hasActivatedAbility;
    }
}
