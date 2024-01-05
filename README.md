### Status
![](https://img.shields.io/github/v/release/MDdev0/Spigot-MafiaCraft?display_name=tag&label=latest%20release)
![](https://img.shields.io/github/v/release/MDdev0/Spigot-MafiaCraft?display_name=tag&include_prereleases&label=latest%20pre-release)
<br>
![](https://img.shields.io/github/issues/MDdev0/Spigot-MafiaCraft)
![](https://img.shields.io/github/issues/MDdev0/Spigot-MafiaCraft/bug?label=bug%20reports)

---

# MafiaCraft

<p>MafiaCraft is a plugin for Spigot-based Minecraft servers that recreates the classic hidden role game
Mafia (also played under the name Werewolf) into Minecraft. This plugin was heavily inspired by those
games, as well as other online hidden role games like Town of Salem.</p>
<p>This plugin is now heading into final testing, and then will be used on my server. Let's hope it works!</p>

# Gameplay

Listed below are all the roles and abilities planned, as well as some definitions.

## Roles
*Italic roles are unique*. A maximum of one of each be created at the beginning of the game.

### <font color="#dd4444">Mafia</font> Roles

| Role        | Description                                       | Abilities                | Tasks                                     |
|-------------|---------------------------------------------------|--------------------------|-------------------------------------------|
| *Godfather* | The head of the local crime family.               | Protection,<br/>Charisma | Tell the members of the Mafia what to do. |
| Mafioso     | A member of the local crime family.               | Succession               | Follow the orders of the Godfather.       |
| Framer      | A skilled bookkeeper turned crime-staging genius. | Forgery                  | Follow the orders of the Godfather.       |
| Assassin    | The dedicated killer within the Mafia's ranks.    | Assassinate              | Follow the orders of the Godfather.       |

### <font color="#33cf33">Village</font> Roles

| Role         | Description                                                                        | Abilities                               | Tasks                                                     |
|--------------|------------------------------------------------------------------------------------|-----------------------------------------|-----------------------------------------------------------|
| *Reanimator* | A Villager who discovered the long lost art of resurrection.                       | Revive                                  | Reanimate players to help the Village survive.            |
| Veteran      | A war hero whose defense instincts never left when they retired from the military. | Retaliate                               | Defend yourself from attackers to help the Villagers win. |
| Deputy       | A fighter with honor and standards, who will always rise to a fair challenge.      | High Noon,<br/>Marksman                 | Take down those who the Village sees as a threat.         |
| Investigator | An armchair detective whose skills can finally be put to good use.                 | Investigate                             | Discover who is really on the side of the Villagers.      |
| Lookout      | An eagle-eyed observer who always seems to know who's been where.                  | Watch,<br/>Peripherals,<br/>Clear Sight | Gather information to help the Village.                   |
| Doctor       | A skilled medic capable of helping those in need.                                  | Rescue                                  | Protect other Villagers.                                  |
| *Apothecary* | A skilled brewer capable of producing special concoctions.                         | Ambrosia                                | Stop the spread of the Vampires and protect the Village.  |
| Priest       | A religious devotee who can sense the presence of unholy abilities.                | Inquisition                             | Find out who in the town may be harboring a secret power. |

### <font color="#6666ee">Neutral</font> Roles

_Notes:  
Players who win Alone win only if there are no Mafia, Village, or Vampires remaining.  
Vampires must kill all players with the alignments the Mafia, Village, or Alone.  
Those with no alignment can win with any team, as long as their tasks and win conditions are complete._

| Role          | Description                                                                                 | Abilities                                                        | Alignment | Tasks and Win Conditions                                    |
|---------------|---------------------------------------------------------------------------------------------|------------------------------------------------------------------|-----------|-------------------------------------------------------------|
| Serial Killer | An insane murderer who wants nothing more than to be alone.                                 | Protection,<br/>Ambush                                           | Alone     | Kill everyone without being caught.                         |
| Bodyguard     | Someone who's willing to put their life on the line to save another.                        | Protectee,<br/>This is Fine,<br/>Dodge Roll,</br>Protection      | None      | Keep your protectee alive at any cost.                      |
| Hunter        | Ensure that the targets you are given die by the end of the game, and remain dead.          | Target,<br/>Tracking                                             | None      | Use any means necessary to ensure your target players die.  |
| Sorcerer      | A powerful spell-caster bound by no allegiances.                                            | Scatter,<br/>Toadify,<br/>Fog of War,<br/>Vanish,<br/>Spell Book | None      | Use your abilities to survive.                              |
| *Werewolf*    | A regular person who suddenly transforms under the Full Moon.                               | Transform,<br/>Rampage,<br/>Nemesis                              | Alone     | Kill everyone without being caught.                         |
| *Vampire*     | A mythical creature whose only goal is to spread their reach to as many people as possible. | Convert,<br/>Night Owl,<br/>Staked                               | Vampires  | Convert or kill all others.<br/>Do not get caught.          |
| *Jester*      | A clown who just wants to trick the Village into making a grave mistake.                    | Just a Prank                                                     | None      | Be killed by a member of the Village without fighting back. |

## Abilities

| Ability      | Description                                                                                                                                                                                                       | Cooldown                               |
|--------------|-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|----------------------------------------|
| Protection   | When attacked first, receive Resistance II for 60 seconds.                                                                                                                                                        | Once every 3 in-game days.             |
| Charisma     | Appear innocent to Investigators, despite being a member of the Mafia.                                                                                                                                            |                                        |
| Succession   | When the Godfather dies, one Mafioso will be selected at random to become the new Godfather.                                                                                                                      |                                        |
| Forgery      | Drop a paper named *Forgery* on a player to make them appear suspicious to Investigators. Resets at the end of the in-game day.                                                                                   |                                        |
| Assassinate  | When attacking first, gains Strength I and Speed I for 2 minutes.                                                                                                                                                 | Once every 3 in-game days.             |
| Revive       | Throw a Totem of Undying into a soul flame. Then, choose a dead player to resurrect.                                                                                                                              | Once every 24 (real) hours.            |
| Retaliate    | When attacked first, receive Strength II and Resistance II for 30 seconds.                                                                                                                                        | Once every 3 in-game days.             |
| High Noon    | Receive Strength II for 30 seconds at noon of the in-game day.                                                                                                                                                    | Occurs automatically.                  |
| Marksman     | Arrows do one additional heart of damage.                                                                                                                                                                         |                                        |
| Investigate  | Use a spyglass to spy on a player for 10 seconds to learn if they are suspected of being in the Mafia. *Note this only applies to Mafia roles.*                                                                   |                                        |
| Watch        | Can see invisible players when using a Spyglass.                                                                                                                                                                  |                                        |
| Peripherals  | Can see invisible players within 20 blocks.                                                                                                                                                                       |                                        |
| Clear Sight  | Not affected by Blindness or Darkness.                                                                                                                                                                            |                                        |
| Rescue       | If a player (other than the medic themselves), would otherwise die within 16 blocks of the medic, they will be placed at 0.5 hearts and receive Regeneration III for 15 seconds.                                  | Once every 3 in-game days.             |
| Ambrosia     | Brew a special splash potion by throwing a golden apple, bottle of honey, and bucket of milk into a heated cauldron. This will revert a Vampire back to their original role, unless that was their original role. | Can be *brewed* every 12 (real) hours. |
| Inquisition  | Any player who has is using or has recently used an Unholy Ability will display a subtle particle effect to Priests.                                                                                              |                                        |
| Ambush       | When attacking first, gains Strength II for 60 seconds.                                                                                                                                                           | Once every 3 in-game days.             |
| Protectee    | Assigned one player to protect at the beginning of the game.                                                                                                                                                      | At the start of the game.              |
| This is Fine | Take reduced damage from fire and lava.                                                                                                                                                                           |                                        |
| Dodge Roll   | Take significantly less damage from explosions and fall damage.                                                                                                                                                   |                                        |
| Target       | Receive a list at the beginning of the game of targets.                                                                                                                                                           | At the start of the game.              |
| Tracking     | See whether your targets are dead or alive.                                                                                                                                                                       |                                        |
| Spell Book   | Craft a spell book by throwing any enchanted book into normal fire.                                                                                                                                               |                                        |
| Scatter      | Using the spell book, grant one person Speed IV for 20 seconds. Costs 4 levels of experience.                                                                                                                     |                                        |
| Toadify      | Using the spell book, inflict one person with Slowness V and Jump Boost V for 15 seconds. Costs 4 levels of experience.                                                                                           |                                        |
| Fog of War   | Using the spell book, inflict everyone within 30 blocks with Blindness I for 30 seconds. Costs 8 levels of experience.                                                                                            |                                        |
| Vanish       | Using the spell book, grant one person with Invisibility for 3 minutes. Costs 8 levels of experience.                                                                                                             |                                        |
| Transform    | Transform on full-moon nights.                                                                                                                                                                                    | Occurs automatically.                  |
| Rampage      | For every kill while Transformed, gain one level of strength for the rest of the night (maximum Strength III).                                                                                                    |                                        |
| Nemesis      | Weak to Iron Tools while Transformed.                                                                                                                                                                             |                                        |
| Convert      | All kills will not permanently kill the player, instead they will respawn as a Vampire.                                                                                                                           |                                        |
| Night Owl    | Affected by Weakness II during the day.                                                                                                                                                                           | Occurs automatically.                  |
| Staked       | Weak to Wooden Tools.                                                                                                                                                                                             |                                        |
| Just a Prank | Any Villager who kills a Jester who does not fight back will cause an explosion and various negative effects. The Jester will respawn and have their role revealed.                                               | **One-time use.**                      |

## Rules and Definitions

* **In-Game Day**: starts and ends at sunrise (tick 0)
* **Attack**: Hit a player with a Sword, Axe, or Bow. Punches do not count (to prevent false triggers).
* **Attacked First**: Be attacked when not attacking anyone for 30 seconds prior.
* **Attacking First**: Attack someone who has not attacked anyone for 30 seconds.
* **Unholy Abilities**: Revive, Ambrosia, Spell Book, Scatter, Toadify, Fog of War, Vanish, Transform, Convert, Hunting Night
  * Unholy Abilities display to Priests for two complete in-game days (does not reset at sunrise). This is refreshed every time an unholy ability is used.
* Wins by Surviving roles win with **anyone**, even Wins Alone roles.
* The game will end at sundown, when the living players have met their win conditions (or failed to, in the case of some neutral roles)

## Commands

`/mafiacraft`: Available to all players. Shows information about their role.  
`/mafiacraftadmin`: Available to operators only. Controls all aspects of MafiaCraft that are not controlled in the config.  
These commands also have aliases to make them easier to use quickly.

### The `/mafiacraftadmin` command tree

* `setrole <player> <role>`: Sets a player's role. The player must be online. No spaces allowed in role name.
* `removeplayer <player>`: Removes the specified player from the game. The player must be online.
* `randomize`
  * `add <player>`: Adds the player to the list of players who need their role randomized. The player must be online. Players can have their role re-randomized if they already have one.
  * `addall`: Adds all players who have ever joined this world into the list of players to be randomized. The list is taken from the `world/playerdata` folder.
  * `remove <player/uuid>`: Removes the specified player from the list of players to be randomized. Use UUID if the player is offline (see `/mafiacraft randomize list`).
  * `removeall`: Clears the list of players to be randomized.
  * `list`: Lists all players set to be randomized and their UUIDs (click to copy).
  * `start`: Starts the randomization process and announces roles.
* `start`: Starts the game. Does not affect the config value.
* `stop`: Stops the game. Does not affect the config value.
* `revive <player>`: Revives a dead player. The player must be online.
* `list`: Lists all players in this game of MafiaCraft

## Thanks To
Code Contributions: [@kalyus](https://github.com/kalyus)  
Development Testing: dustyporcupine, set_h, SpaghetBoi, [@kalyus](https://github.com/kalyus)  
and everyone who helped workshop and encourage this idea along the way
