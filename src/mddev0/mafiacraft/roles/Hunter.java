package mddev0.mafiacraft.roles;

import mddev0.mafiacraft.MafiaCraft;
import mddev0.mafiacraft.abilities.Ability;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

public final class Hunter extends Role {

    private final MafiaCraft plugin;

    private final UUID self;

    private final List<UUID> targets; // Saved Member

    public Hunter(MafiaCraft plugin, UUID self) {
        super(WinCondition.SURVIVING, false);
        this.plugin = plugin;
        this.self = self;
        abilities.add(Ability.TARGET);
        abilities.add(Ability.TRACKING);
        targets = new ArrayList<>();
    }

    public Hunter(MafiaCraft plugin, UUID self, List<UUID> targets) {
        super(WinCondition.SURVIVING, false);
        this.plugin = plugin;
        this.self = self;
        abilities.add(Ability.TARGET);
        abilities.add(Ability.TRACKING);
        this.targets = targets;
    }

    public List<UUID> getTargets() {
        return targets;
    }

    public void findTargets(int num) {
        Random rand = new Random();
        List<UUID> players = new java.util.ArrayList<>(plugin.getLivingPlayers().keySet().stream().toList());
        players.remove(self);
        num = Math.min(players.size(), num);
        for (int i = 0; i < num; i++) {
            int index = rand.nextInt(players.size());
            targets.add(players.remove(index));
        }
    }

    public boolean targetsKilled() {
        boolean dead = true;
        for (UUID t : targets) {
            if (plugin.getPlayerList().get(t).isLiving())
                dead = false;
        }
        return dead;
    }

    @Override
    public String toString() {
        return "Hunter";
    }
}
