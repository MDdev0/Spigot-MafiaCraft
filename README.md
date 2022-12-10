### Status

![](https://img.shields.io/badge/Project%20Phase-Implementation-yellow)  
![](https://img.shields.io/badge/Total%20Roles-19-lightgrey)
![](https://img.shields.io/badge/Implemented%20Roles-17-red)  
![](https://img.shields.io/badge/Total%20Abilities-35-lightgrey)
![](https://img.shields.io/badge/Implemented%20Abilities-30-red)  
![](https://img.shields.io/badge/Commands-Not%20Started-darkred)  
![](https://img.shields.io/badge/Game%20Flow-Not%20Started-darkred)    
![](https://img.shields.io/badge/Serialization-Not%20Started-darkred)  
![](https://img.shields.io/badge/Testing-None-black)  
![](https://img.shields.io/badge/Estimated%20Finish%20Date-No%20Clue-blue)
![](https://img.shields.io/badge/December%20Hopefully!-teal)

---

# MafiaCraft

<p>MafiaCraft is a plugin for Bukkit that recreates the classic hidden role game
Mafia (also played under the name Werewolf) into Minecraft. This plugin was heavily inspired by those
games, as well as other online hidden role games like Town of Salem.</p>
<p>This plugin is still early in development, and I only really get time to work on it when my schedule
is free for a whole afternoon, so expect lengthy delays. See the status above for more info.</p>

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
| Assassin    | The dedicated killer within the Mafia's ranks.    | Assassination            | Follow the orders of the Godfather.       |

### <font color="#33cf33">Village</font> Roles

| Role         | Description                                                                        | Abilities                               | Tasks                                                     |
|--------------|------------------------------------------------------------------------------------|-----------------------------------------|-----------------------------------------------------------|
| *Reanimator* | A Villager who discovered the long lost art of resurrection.                       | Reanimation                             | Reanimate players to help the Village survive.            |
| Veteran      | A war hero whose defense instincts never left when they retired from the military. | Retaliation                             | Defend yourself from attackers to help the Villagers win. |
| Deputy       | A fighter with honor and standards, who will always rise to a fair challenge.      | High Noon,<br/>Marksman                 | Take down those who the Village sees as a threat.         |
| Investigator | An armchair detective whose skills can finally be put to good use.                 | Investigate                             | Discover who is really on the side of the Villagers.      |
| Lookout      | An eagle-eyed observer who always seems to know who's been where.                  | Watch,<br/>Peripherals,<br/>Clear Sight | Gather information to help the Village.                   |
| Doctor       | A skilled medic capable of helping those in need.                                  | Rescue                                  | Protect other Villagers.                                  |
| *Apothecary* | A skilled brewer capable of producing special concoctions.                         | Ambrosia                                | Brew potions for the Village.                             |
| Deacon       | A religious devotee who can sense the presence of unholy abilities.                | Inquisition                             | Find out who in the town may be harboring a secret power. |

### <font color="#6666ee">Neutral</font> Roles

_Notes:  
Alone and Same Role mean only themselves (or those who share their role) and those
who win by Surviving remaining alive.  
Win by Surviving roles can win with anyone, as long as they survive until the end of the game._

| Role          | Description                                                                                 | Abilities                                                        | Win Conditions | Tasks                                                                      |
|---------------|---------------------------------------------------------------------------------------------|------------------------------------------------------------------|----------------|----------------------------------------------------------------------------|
| Serial Killer | An insane murderer who wants nothing more than to be alone.                                 | Protection,<br/>Ambush                                           | Alone          | Kill everyone without being caught.                                        |
| Trapmaker     | Some people just want to see the world burn.                                                | This is Fine,<br/>Dodge Roll                                     | Alone          | Use traps and fire to kill everyone without being caught.                  |
| Hunter        | Ensure that the targets you are given die by the end of the game, and remain dead.          | Target,<br/>Tracking                                             | Surviving      | Use any means necessary to ensure your target players die.<p/>Avoid dying. |
| Sorcerer      | A powerful spell-caster bound by no allegiances.                                            | Scatter,<br/>Toadify,<br/>Fog of War,<br/>Vanish,<br/>Spell Book | Surviving      | Use your abilities to survive.                                             |
| *Werewolf*    | A regular person who suddenly transforms under the Full Moon.                               | Transform,<br/>Rampage,<br/>Bite,<br/>Nemesis                    | Same Role      | Convert or kill all others.<p/>Do not get caught.                          |
| *Vampire*     | A mythical creature whose only goal is to spread their reach to as many people as possible. | Convert,<br/>Hunting Night,<br/>Night Owl,<br/>Staked            | Same Role      | Convert or kill all others.<p/>Do not get caught.                          |
| *Jester*      | A clown who just wants to trick the Village into making a grave mistake.                    | Just a Prank                                                     | Surviving*     | Be killed by a member of the Village.                                      |

_* A Jester only wins if they survive and get their **Just a Prank** ability is triggered._

## Abilities

| Ability       | Description                                                                                                                                                                                                                   | Cooldown                             |
|---------------|-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|--------------------------------------|
| Protection    | When attacked first, receive Resistance II for 30 seconds.                                                                                                                                                                    | Once per in-game day.                |
| Charisma      | Appear innocent to Investigators, despite being a member of the Mafia.                                                                                                                                                        |                                      |
| Succession    | When the Godfather dies, one Mafioso will be selected at random to become the new Godfather.                                                                                                                                  |                                      |
| Forgery       | Drop a paper named *Forgery* on a player to make them appear suspicious to Investigators. Resets at the end of the in-game day.                                                                                               |                                      |
| Assassination | When attacking first, gains Strength I and Speed I for 60 seconds.                                                                                                                                                            | Once per in-game day.                |
| Reanimation   | Throw a totem of undying into a soul flame. Then, choose a dead player to resurrect.                                                                                                                                          | Once per in-game week.               |
| Retaliation   | When attacked first, receive Strength II and Resistance II for 30 seconds.                                                                                                                                                    | Once per in-game day.                |
| High Noon     | Receive Strength II for 60 seconds at noon.                                                                                                                                                                                   | Occurs automatically.                |
| Marksman      | Arrows do one additional heart of damage.                                                                                                                                                                                     |                                      |
| Investigate   | Use a spyglass directly on a player for 10 seconds to learn if they are suspected of being in the Mafia. *Note this only applies to Mafia roles.*                                                                             |                                      |
| Watch         | Can see invisible players when using a Spyglass.                                                                                                                                                                              |                                      |
| Peripherals   | Can see invisible players within 20 blocks.                                                                                                                                                                                   |                                      |
| Clear Sight   | Not affected by Blindness.                                                                                                                                                                                                    |                                      |
| Rescue        | If a player (other than the medic themselves), would otherwise die within 16 blocks of the medic, they will be placed at 0.5 hearts and receive Regeneration III for 15 seconds.                                              | Once per in-game day.                |
| Ambrosia      | Brew a special splash potion by throwing a golden apple, bottle of honey, and bucket of milk into a heated cauldron. This will revert a Vampire or Werewolf back to their original role, unless that was their original role. | Can be brewed once per in-game week. |
| Inquisition   | Any player who has is using or has recently used an Unholy Ability will display a subtle particle effect to Deacons.                                                                                                          |                                      |
| Ambush        | When attacking first, gains Strength II for 30 seconds.                                                                                                                                                                       | Once per in-game day.                |
| This is Fine  | Take reduced damage from fire and lava.                                                                                                                                                                                       |                                      |
| Dodge Roll    | Take significantly less damage from explosions and fall damage.                                                                                                                                                               |                                      |
| Target        | Receive a list at the beginning of the game of targets.                                                                                                                                                                       | At the start of the game.            |
| Tracking      | See whether your targets are dead or alive.                                                                                                                                                                                   |                                      |
| Spell Book    | Craft a spell book by throwing any enchanted book into normal fire.                                                                                                                                                           |                                      |
| Scatter       | Using the spell book, effect one person with Speed IV for 20 seconds. Costs 5 levels of experience.                                                                                                                           |                                      |
| Toadify       | Using the spell book, effect one person with Slowness V and Jump Boost V for 15 seconds. Costs 5 levels of experience.                                                                                                        |                                      |
| Fog of War    | Using the spell book, effect everyone within 30 blocks with Blindness I for 30 seconds. Costs 10 levels of experience.                                                                                                        |                                      |
| Vanish        | Using the spell book, effect one person with Invisibility for 180 seconds. Costs 10 levels of experience.                                                                                                                     |                                      |
| Transform     | Transform on full-moon nights.                                                                                                                                                                                                | Occurs automatically.                |
| Rampage       | For every kill while Transformed, gain one level of strength for the rest of the night (maximum Strength V).                                                                                                                  |                                      |
| Bite          | Any players killed while Transformed will respawn as Werewolves.                                                                                                                                                              |                                      |
| Nemesis       | Weak to Iron Tools while Transformed.                                                                                                                                                                                         |                                      |
| Convert       | All kills will not permanently kill the player, instead they will respawn as a Vampire.                                                                                                                                       |                                      |
| Hunting Night | On nights with a new moon, any kills by a Vampire will not Convert players and will instead simply kill them.                                                                                                                 |                                      |
| Night Owl     | Affected by Weakness II during the day.                                                                                                                                                                                       | Occurs automatically.                |
| Staked        | Weak to Wooden Tools.                                                                                                                                                                                                         |                                      |
| Just a Prank  | Any Villager who kills a Jester unprovoked will be affected with Wither I indefinitely, until they are able to find a cure. The Jester will respawn.                                                                          |                                      |

## Rules and Definitions

* **In-Game Day**: starts and ends at sunrise (tick 0)
* **Attack**: Hit a player with a Sword, Axe, or Bow. Punches do not count to prevent false triggers.
* **Attacked First**: Be attacked when not attacking anyone for 30 seconds prior.
* **Attacking First**: Attack someone who has not attacked anyone for 30 seconds.
* **Unholy Abilities**: Reanimation, Ambrosia, Spell Book, Scatter, Toadify, Fog of War, Vanish, Transform, Convert, Hunting Night
  * Unholy Abilities display to Deacons for two complete in-game days (does not reset at sunrise). This is refreshed every time an unholy ability is used.
* Wins by Surviving roles win with **anyone**, even Wins Alone roles.
