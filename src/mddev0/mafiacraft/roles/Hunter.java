package mddev0.mafiacraft.roles;

import mddev0.mafiacraft.MafiaCraft;
import mddev0.mafiacraft.abilities.Ability;
import mddev0.mafiacraft.util.Targeter;

import java.util.List;
import java.util.Random;
import java.util.UUID;

public final class Hunter extends Role implements Targeter {

    private final MafiaCraft plugin;

    private final UUID self;

    private List<UUID> targets;

    public Hunter(MafiaCraft plugin, UUID self) {
        super(WinCondition.SURVIVING, false);
        this.plugin = plugin;
        this.self = self;
        abilities.add(Ability.TARGET);
        abilities.add(Ability.TRACKING);
    }

    @Override
    public List<UUID> getTargets() {
        return targets;
    }

    @Override
    public void getTargets(int num) {
        Random rand = new Random();
        List<UUID> players = new java.util.ArrayList<>(plugin.getLivingPlayers().keySet().stream().toList());
        players.remove(self);
        num = Math.min(players.size(), num);
        for (int i = 0; i < num; i++) {
            int index = rand.nextInt(players.size());
            targets.add(players.remove(index));
        }
    }

    @Override
    public boolean targetsKilled() {
        boolean dead = true;
        for (UUID t : targets) {
            if (plugin.getPlayerList().get(t).isLiving())
                dead = false;
        }
        return dead;
    }
}
