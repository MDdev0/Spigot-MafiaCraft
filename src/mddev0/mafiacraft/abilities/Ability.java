package mddev0.mafiacraft.abilities;

public enum Ability { // TODO: if grayed out, not implemented yet.
    PROTECTION("Protection"),
    CHARISMA("Charisma"), // Has no class
    SUCCESSION("Succession"),
    FORGERY("Forgery"),
    ASSASSINATION("Assassination"),
    REANIMATION("Reanimation"),
    RETALIATION("Retaliation"),
    HIGH_NOON("High Noon"),
    MARKSMAN("Marksman"),
    INVESTIGATE("Investigate"),
    WATCH("Watch"),
    PERIPHERALS("Peripherals"),
    CLEAR_SIGHT("Clear Sight"),
    RESCUE("Rescue"),
    AMBROSIA("Ambrosia"),
    INQUISITION("Inquisition"),
    AMBUSH("Ambush"),
    THIS_IS_FINE("This is Fine"),
    DODGE_ROLL("Dodge Roll"),
    TARGET("Target"), // Has no class
    TRACKING("Tracking"), // Has no class
    SPELL_BOOK("Spell Book"),
    SCATTER("Scatter"),
    TOADIFY("Toadify"),
    FOG_OF_WAR("Fog of War"),
    VANISH("Vanish"),
    TRANSFORM("Transform"),
    RAMPAGE("Rampage"),
    BITE("Bite"),
    NEMESIS("Nemesis"),
    CONVERT("Convert"),
    HUNTING_NIGHT("Hunting Night"),
    NIGHT_OWL("Night Owl"),
    STAKED("Staked"),
    JUST_A_PRANK("Just a Prank");

    public final String NAME;


    Ability(String name) {
        this.NAME = name;
    }
}
