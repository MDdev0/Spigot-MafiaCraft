package mddev0.mafiacraft.player;

import mddev0.mafiacraft.abilities.Ability;

import java.util.Set;

public enum Role {
    GODFATHER("Godfather", true, Team.MAFIA, Ability.PROTECTION, Ability.AMBUSH),
    MAFIOSO("Mafioso", false, Team.MAFIA, Ability.SUCCESSION),
    FRAMER("Framer", false, Team.MAFIA, Ability.FORGERY),
    ASSASSIN("Assassin", false, Team.MAFIA, Ability.ASSASSINATE),
    // TODO: BREWER("Brewer", false, Team.MAFIA, ???),
    REANIMATOR("Reanimator", true, Team.VILLAGE, Ability.REVIVE),
    VETERAN("Veteran", false, Team.VILLAGE, Ability.RETALIATE),
    DEPUTY("Deputy", false, Team.VILLAGE, Ability.HIGH_NOON, Ability.MARKSMAN),
    INVESTIGATOR("Investigator", false, Team.VILLAGE, Ability.INVESTIGATE),
    LOOKOUT("Lookout", false, Team.VILLAGE, Ability.WATCH, Ability.PERIPHERALS, Ability.CLEAR_SIGHT),
    DOCTOR("Doctor", false, Team.VILLAGE, Ability.RESCUE),
    APOTHECARY("Apothecary", true, Team.VILLAGE, Ability.AMBROSIA),
    PRIEST("Priest", false, Team.VILLAGE, Ability.INQUISITION),
    SERIAL_KILLER("Serial Killer", false, Team.SOLO, Ability.PROTECTION, Ability.AMBUSH),
    BODYGUARD("Bodyguard", false, Team.NONE, Ability.PROTECTEE, Ability.THIS_IS_FINE, Ability.DODGE_ROLL, Ability.PROTECTION),
    HUNTER("Hunter", false, Team.NONE, Ability.TARGET, Ability.TRACKING),
    SORCERER("Sorcerer", false, Team.NONE, Ability.SCATTER, Ability.TOADIFY, Ability.FOG_OF_WAR, Ability.VANISH, Ability.SPELL_BOOK),
    WEREWOLF("Werewolf", false, Team.SOLO, Ability.TRANSFORM, Ability.RAMPAGE, Ability.NEMESIS),
    VAMPIRE("Vampire", true, Team.VAMPIRES, Ability.CONVERT, Ability.NIGHT_OWL, Ability.STAKED),
    JESTER("Jester", true, Team.NONE, Ability.JUST_A_PRANK);

    private final String NAME;
    private final boolean UNIQUE;
    private final Team ALIGNMENT;
    private final Set<Ability> ABILITIES;

    Role(String name, boolean unique, Team alignment, Ability... abilities) {
        NAME = name;
        UNIQUE = unique;
        ALIGNMENT = alignment;
        ABILITIES = Set.of(abilities);
    }

    public String toString() {
        return NAME;
    }

    public boolean isUnique() {
        return UNIQUE;
    }

    public Team getAlignment() {
        return ALIGNMENT;
    }

    public Set<Ability> getAbilities() {
        return ABILITIES;
    }
    
    public enum Team {
        MAFIA,
        VILLAGE,
        VAMPIRES,
        SOLO,
        NONE
    }
}
