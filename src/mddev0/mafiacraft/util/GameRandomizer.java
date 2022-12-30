package mddev0.mafiacraft.util;

import mddev0.mafiacraft.MafiaCraft;
import mddev0.mafiacraft.roles.Hunter;
import mddev0.mafiacraft.roles.Role;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.logging.Level;

public class GameRandomizer {

    private final MafiaCraft plugin;

    private final List<OfflinePlayer> players;
    private List<OfflinePlayer> prevPlayers;

    public GameRandomizer (MafiaCraft plugin) {
        this.plugin = plugin;
        players = new ArrayList<>();
    }

    public int addAllOffline() {
        int added = 0;
        for (OfflinePlayer p : Bukkit.getOfflinePlayers())
            if (!players.contains(p)) {
                if (p.getName() == null) {
                    // DO NOT ADD PLAYERS WITH NULL USERNAMES!!!
                    continue;
                }
                players.add(p);
                added++;
            }
        return added;
    }

    public boolean addPlayer(String playerName) {
        OfflinePlayer player = Bukkit.getPlayer(playerName);
        if (player == null) return false;
        if (!players.contains(player)) {
            players.add(player);
            return true;
        } else return false;
    }

    public boolean removePlayer(String playerName) {
        if (Bukkit.getPlayer(playerName) != null) {
            return players.remove(Bukkit.getPlayer(playerName));
        } else {
            for (OfflinePlayer p : players) {
                if (p.getName() != null && p.getName().equals(playerName)) {
                    return players.remove(p);
                }
            }
            return false;
        }
    }

    public void removeAll() {
        players.clear();
    }

    // This will likely be an expensive method that will have to be called asynchronously.
    public void randomizeGame() throws RandomizationException {
        if (plugin.getActive())
            throw new RandomizationException("Cannot randomize while active.");

        final int numPlayers = players.size();
        if (numPlayers == 0)
            throw new RandomizationException("There are no players added to the game to be randomized.");

        // Set up
        double mafiaRatio = plugin.getConfig().getDouble("roleRatio.mafia");
        double villageRatio = plugin.getConfig().getDouble("roleRatio.village");
        double neutralRatio = plugin.getConfig().getDouble("roleRatio.neutral");
        if (mafiaRatio + villageRatio + neutralRatio > 1.0)
            throw new RandomizationException("Required minimum ratios of mafia, village, and neutral roles add to " +
                    (mafiaRatio + villageRatio + neutralRatio) + ", should not add to more than 1.0");
        int numMafia = (int) Math.ceil(numPlayers * mafiaRatio);
        int numVillage = (int) Math.ceil(numPlayers * villageRatio);
        int numNeutral = (int) Math.ceil(numPlayers * neutralRatio);

        // Start selection process
        List<String> requiredRoles = plugin.getConfig().getStringList("requiredRoles");
        List<String> bannedRoles = plugin.getConfig().getStringList("bannedRoles");
        List<Class<?>> availableRoles = new ArrayList<>();

        // Role possibilities
        for (Role.RoleClasses roleClass : Role.RoleClasses.values()) {
            if (requiredRoles.contains(roleClass.getRoleClass().getSimpleName()))
                // Role is required
                availableRoles.add(roleClass.getRoleClass());
            else if (!bannedRoles.contains(roleClass.getRoleClass().getSimpleName()))
                // Role is not banned
                availableRoles.add(roleClass.getRoleClass());
        }

        // In case there are already players in the game, take that into account
        // If a player will be replaced (rerolled), remove them from the game (they will be added back)
        for (MafiaPlayer mp : plugin.getPlayerList().values()) { // For all players already in the game
            boolean checkThisPlayer = true;
            for (OfflinePlayer p : players)
                // Do not consider the player as "in the game" if they will be replaced
                if (p.getUniqueId() == mp.getID()) {
                    checkThisPlayer = false;
                    plugin.getPlayerList().remove(p.getUniqueId());
                    break; // break out of [for (OfflinePlayer p : players)]
                }
            if (checkThisPlayer) {
                switch (mp.getRole().getWinCond()) {
                    case MAFIA -> numMafia--;
                    case VILLAGE -> numVillage--;
                    default -> numNeutral--;
                }
                requiredRoles.remove(mp.getRole().getClass().getSimpleName()); // if role is required, mark as fulfilled
                if (mp.getRole().isUnique()) availableRoles.remove(mp.getRole().getClass());
            }
        }

        if (numMafia + numVillage + numNeutral > numPlayers)
            throw new RandomizationException("Required minimum ratios of mafia, village, and neutral roles cannot be satisfied with " +
                    numPlayers + " players.");

        // Check if it's possible to fill all required roles
        if (requiredRoles.size() > numPlayers)
            throw new RandomizationException(requiredRoles.size() + " roles need to be filled, but only " + numPlayers + " players are available.");

        // SHALLOW COPY!!
        prevPlayers = new ArrayList<>(players);

        // Player role assignment
        // This loop could go on for ages... oh well
        // and by ages I mean worst case is potentially infinite
        // and it nests loops rip
        final Random rand = new Random();
        while (!players.isEmpty()) {
            OfflinePlayer player = players.get(rand.nextInt(players.size()));
            Class<?> roleCls;
            do {
                roleCls = availableRoles.get(rand.nextInt(availableRoles.size()));
            } while (requiredRoles.size() > 0 && !requiredRoles.contains(roleCls.getSimpleName()));
            Role role;
            // Create role
            try {
                if (roleCls.getSimpleName().equals("Hunter")) {
                    role = (Hunter) roleCls.getDeclaredConstructor(MafiaCraft.class, UUID.class).newInstance(plugin, player.getUniqueId());
                } else {
                    role = (Role) roleCls.getDeclaredConstructor().newInstance();
                }
            } catch (ReflectiveOperationException | IllegalArgumentException e) {
                // This shouldn't happen, but in case it does for some reason,
                continue; // Just skip this iteration :)
            }

            // Make sure role's group is not full
            // number of each group value from above will be decremented each time
            if (numMafia > 0 || numVillage > 0 || numNeutral > 0) {
                switch (role.getWinCond()) {
                    case MAFIA -> {
                        if (numMafia <= 0) // Finish others before adding more mafia
                            continue; // Just skip this iteration :)
                        else // keep going, and update number of mafia
                            numMafia--;
                    }
                    case VILLAGE -> {
                        if (numVillage <= 0) // Finish others before adding more villagers
                            continue; // Just skip this iteration :)
                        else // keep going, and update number of villagers
                            numVillage--;
                    }
                    default -> {
                        if (numNeutral <= 0) // Finish others before adding more neutrals
                            continue; // Just skip this iteration :)
                        else // keep going, and update number of neutrals
                            numNeutral--;
                    }
                }
            }

            // at this point we know the player can be added to the role
            MafiaPlayer mafiaPlayer = new MafiaPlayer(plugin, player.getUniqueId(), role);
            requiredRoles.remove(roleCls.getSimpleName());
            if (mafiaPlayer.getRole().isUnique()) availableRoles.remove(mafiaPlayer.getRole().getClass());

            // Player is all set to go!
            plugin.getPlayerList().put(mafiaPlayer.getID(), mafiaPlayer);
            if (plugin.getPlayerList().containsKey(mafiaPlayer.getID())){
                players.remove(player);
                // I think the logger is thread safe
                Bukkit.getLogger().log(Level.INFO, "[MafiaCraft] Set random role for player " + player.getName() + " (" + player.getUniqueId() + ")");
            } else {
                Bukkit.getLogger().log(Level.WARNING, "[MafiaCraft] Could not add " + player.getName() + " (" + player.getUniqueId() + ") to the MafiaCraft player list");
            }
        }
        // All players should have been set up by this point
        // Do hunter targets
        for (MafiaPlayer mp : plugin.getPlayerList().values()) {
            if (mp.getRole() instanceof Hunter hunter) {
                hunter.findTargets(plugin.getConfig().getInt("hunterNumTargets"));
            }
        }
    }

    public List<OfflinePlayer> getPlayers() {
        return players;
    }

    public final List<OfflinePlayer> getPrevPlayers() {
        return prevPlayers;
    }

    public static class RandomizationException extends Exception {
        public RandomizationException(String description) {
            super(description);
        }
    }
}
