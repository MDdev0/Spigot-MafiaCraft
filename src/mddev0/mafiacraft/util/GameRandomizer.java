package mddev0.mafiacraft.util;

import mddev0.mafiacraft.MafiaCraft;
import mddev0.mafiacraft.player.MafiaPlayer;
import mddev0.mafiacraft.player.Role;
import mddev0.mafiacraft.player.RoleData;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import java.util.*;
import java.util.logging.Level;

public class GameRandomizer {

    private final MafiaCraft plugin;

    private final List<OfflinePlayer> playersToRoll;
    private List<OfflinePlayer> prevRolledPlayers;

    public GameRandomizer (MafiaCraft plugin) {
        this.plugin = plugin;
        playersToRoll = new ArrayList<>();
    }

    public int addAllOffline() {
        int added = 0;
        for (OfflinePlayer p : Bukkit.getOfflinePlayers())
            if (!playersToRoll.contains(p)) {
                if (p.getName() == null) {
                    // DO NOT ADD PLAYERS WITH NULL USERNAMES!!!
                    continue;
                }
                playersToRoll.add(p);
                added++;
            }
        return added;
    }

    public boolean addPlayer(String playerName) {
        OfflinePlayer player = Bukkit.getPlayer(playerName);
        if (player == null) return false;
        if (!playersToRoll.contains(player)) {
            playersToRoll.add(player);
            return true;
        } else return false;
    }

    public boolean removePlayer(String playerName) {
        if (Bukkit.getPlayer(playerName) != null) {
            return playersToRoll.remove(Bukkit.getPlayer(playerName));
        } else {
            for (OfflinePlayer p : playersToRoll) {
                if (p.getName() != null && p.getName().equals(playerName)) {
                    return playersToRoll.remove(p);
                }
            }
            return false;
        }
    }

    public void removeAll() {
        playersToRoll.clear();
    }

    // This will likely be an expensive method that will have to be called asynchronously.
    public void randomizeGame() throws RandomizationException {
        if (plugin.getActive())
            throw new RandomizationException("Cannot randomize while active.");

        final int numPlayers = playersToRoll.size();
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
        Set<Role> availableRoles = new HashSet<>();

        // Initial Role possibilities
        for (Role r : Role.values()) {
            if (requiredRoles.contains(r.name()))
                // Role is required
                availableRoles.add(r);
            else if (!bannedRoles.contains(r.name()))
                // Role is not banned
                availableRoles.add(r);
        }

        // Remove all players to be rolled from the game if they are already in
        for (OfflinePlayer p : playersToRoll)
            plugin.getPlayerList().remove(p.getUniqueId());

        // Count players who have their roles and limit the number of duplicates
        for (MafiaPlayer mp : plugin.getPlayerList().values()) {
            switch (mp.getRole().getAlignment()) {
                case MAFIA -> numMafia--;
                case VILLAGE -> numVillage--;
                default -> numNeutral--;
            }
            availableRoles.remove(mp.getRole());
            requiredRoles.remove(mp.getRole().name());
        }

        // Throw an error if the required ratios aren't possible
        if (numMafia + numVillage + numNeutral > numPlayers)
            throw new RandomizationException("Required minimum ratios of mafia, village, and neutral roles cannot be satisfied with " +
                    numPlayers + " players.");

        // Check if it's possible to fill all required roles
        if (requiredRoles.size() > numPlayers)
            throw new RandomizationException(requiredRoles.size() + " roles need to be filled, but only " + numPlayers + " players are available.");

        // INTENTIONAL SHALLOW COPY!!
        // Done to keep a record of who was randomized
        prevRolledPlayers = new ArrayList<>(playersToRoll);

        /*
         * XXX: At some point this needs to be revised to avoid potentially infinite time.
         *  Use lists of mafia, village, and neutral roles then assign based on ratio
         */

        // Player role assignment (This is the reason for asynchronous)
        // This loop could go on for ages... oh well
        // and by ages I mean worst case is potentially infinite
        final Random rand = new Random();
        while (!playersToRoll.isEmpty()) {
            // Get random player
            OfflinePlayer player = playersToRoll.get(rand.nextInt(playersToRoll.size()));

            Role role; // this will store the assigned role

            // Restock roles if there are none left
            for (Role r : Role.values())
                if (!r.isUnique() && !bannedRoles.contains(r.name())) // Role is not unique/banned
                    availableRoles.add(r);

            // If there are required roles outstanding, assign them first
            if (!requiredRoles.isEmpty())
                role = Role.valueOf(requiredRoles.get(0));
            else // Otherwise, assign a role from the full available list
                role = availableRoles.stream().toList().get(rand.nextInt(availableRoles.size()));

            // Make sure role's group is not full
            // number of each group value from above will be decremented each time
            if (numMafia > 0 || numVillage > 0 || numNeutral > 0) {
                switch (role.getAlignment()) {
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
            // Prevent reselect of role
            requiredRoles.remove(role.name());
            availableRoles.remove(role);

            // Assign to player
            MafiaPlayer mafiaPlayer = new MafiaPlayer(plugin, player.getUniqueId(), role);

            // Player is all set to go!
            plugin.getPlayerList().put(mafiaPlayer.getID(), mafiaPlayer);
            if (plugin.getPlayerList().containsKey(mafiaPlayer.getID())){
                playersToRoll.remove(player);
                // I think the logger is thread safe
                Bukkit.getLogger().log(Level.INFO, "[MafiaCraft] Set random role for player " + player.getName() + " (" + player.getUniqueId() + ")");
            } else {
                Bukkit.getLogger().log(Level.WARNING, "[MafiaCraft] Could not add " + player.getName() + " (" + player.getUniqueId() + ") to the MafiaCraft player list");
            }
        }

        // All players should have been set up by this point do hunter targets
        for (MafiaPlayer mp : plugin.getPlayerList().values()) {
            if (mp.getRole() == Role.HUNTER) {
                Set<UUID> targets = new HashSet<>();
                List<UUID> allLiving = new ArrayList<>(plugin.getLivingPlayers().keySet().stream().toList());
                allLiving.remove(mp.getID());
                int num = Math.min(allLiving.size(), plugin.getConfig().getInt("hunterNumTargets"));
                for (int i = 0; i < num; i++)
                    targets.add(allLiving.remove(rand.nextInt(allLiving.size())));
                mp.getRoleData().setData(RoleData.DataType.HUNTER_TARGETS, targets);
            }
        }
    }

    public List<OfflinePlayer> getPlayersToRoll() {
        return playersToRoll;
    }

    public final List<OfflinePlayer> getPrevRolledPlayers() {
        return prevRolledPlayers;
    }

    public static class RandomizationException extends Exception {
        public RandomizationException(String description) {
            super(description);
        }
    }
}
