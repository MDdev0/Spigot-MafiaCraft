package mddev0.mafiacraft.abilities;

@SuppressWarnings("unused")
public enum Ability {
    PROTECTION("Protection",
            "When attacked first, receive Resistance II for 30 seconds."),
    CHARISMA("Charisma",
            "Appear innocent to Investigators, despite being a member of the Mafia."), // Has no class
    SUCCESSION("Succession",
            "When the Godfather dies, one Mafioso will be selected at random to become the new Godfather."),
    FORGERY("Forgery",
            "Drop a paper named 'Forgery' on a player to make them appear suspicious to Investigators. Resets at the end of the in-game day."),
    ASSASSINATE("Assassinate",
            "When attacking first, gains Strength I and Speed I for 60 seconds."),
    REVIVE("Revive",
            "Throw a Totem of Undying into a soul flame. Then, choose a dead player to resurrect."),
    RETALIATE("Retaliate",
            "When attacked first, receive Strength II and Resistance II for 30 seconds."),
    HIGH_NOON("High Noon",
            "Receive Strength II for 60 seconds at noon of the in-game day."),
    MARKSMAN("Marksman",
            "Arrows do one additional heart of damage."),
    INVESTIGATE("Investigate",
            "Use a spyglass to spy on a player for 10 seconds to learn if they are suspected of being in the Mafia."),
    WATCH("Watch",
            "Can see invisible players when using a Spyglass."),
    PERIPHERALS("Peripherals",
            "Can see invisible players within 20 blocks."),
    CLEAR_SIGHT("Clear Sight",
            "Not affected by Blindness or Darkness."),
    RESCUE("Rescue",
            "If a player (other than you), would otherwise die within 16 blocks of you, they will be placed at 0.5 hearts and receive Regeneration III for 15 seconds."),
    AMBROSIA("Ambrosia",
            "Brew a special potion by throwing a Golden Apple, Bottle of Honey, and Bucket of Milk into a heated cauldron."),
    INQUISITION("Inquisition",
            "Any player who has is using or has recently used any magical ability will display a subtle particle effect to Priests."),
    AMBUSH("Ambush",
            "When attacking first, gains Strength II for 30 seconds."),
    THIS_IS_FINE("This is Fine",
            "Take reduced damage from fire and lava."),
    DODGE_ROLL("Dodge Roll",
            "Take significantly less damage from explosions and fall damage."),
    TARGET("Target",
            "Receive a list at the beginning of the game of targets."), // Has no class
    TRACKING("Tracking",
            "See whether your targets are dead or alive."), // Has no class
    SPELL_BOOK("Spell Book",
            "Craft a spell book by throwing any enchanted book into normal fire. Use to switch spells."),
    SCATTER("Scatter",
            "Grant one person Speed IV for 20 seconds. Costs 5 levels of experience."),
    TOADIFY("Toadify",
            "Inflict one person with Slowness V and Jump Boost V for 15 seconds. Costs 5 levels of experience."),
    FOG_OF_WAR("Fog of War",
            "Inflict everyone within 30 blocks with Blindness I for 30 seconds. Costs 10 levels of experience."),
    VANISH("Vanish",
            "Grant one person Invisibility for 3 minutes. Costs 10 levels of experience."),
    TRANSFORM("Transform",
            "Transform on full-moon nights."),
    RAMPAGE("Rampage",
            "For every kill while Transformed, gain one level of strength for the rest of the night (maximum Strength V)."),
    NEMESIS("Nemesis",
            "Weak to Iron Tools while Transformed."),
    CONVERT("Convert",
            "All kills will not permanently kill the player, instead they will respawn as a Vampire."),
    NIGHT_OWL("Night Owl",
            "Affected by Weakness II during the day."),
    STAKED("Staked",
            "Weak to Wooden Tools."),
    JUST_A_PRANK("Just a Prank","Be killed by a member of the Village without fighting back to respawn.");

    private final String NAME;

    private final String DESCRIPTION;

    Ability(String name, String desc) {
        this.NAME = name;
        this.DESCRIPTION = desc;
    }

    public String toString() {
        return NAME;
    }

    public String getDesc() {
        return DESCRIPTION;
    }
}
