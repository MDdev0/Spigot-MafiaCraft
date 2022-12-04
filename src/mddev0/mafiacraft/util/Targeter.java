package mddev0.mafiacraft.util;

import java.util.List;
import java.util.UUID;

public interface Targeter {

    public List<UUID> getTargets();

    public void getTargets(int num);

    public boolean targetsKilled();
}
