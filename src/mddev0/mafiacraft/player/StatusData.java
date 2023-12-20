package mddev0.mafiacraft.player;

import mddev0.mafiacraft.MafiaCraft;

import java.util.HashMap;
import java.util.Map;

public class StatusData {
    private final HashMap<Status, Long> statusMap;

    private final MafiaCraft plugin;

    public StatusData(MafiaCraft plugin) {
        this.plugin = plugin;
        this.statusMap = new HashMap<>();
    }

    public StatusData(MafiaCraft plugin, StatusDataSave saveData) {
        this.plugin = plugin;
        this.statusMap = saveData.statusMap();
    }

    public void startStatus(Status status, Long timeToEnd) {
        statusMap.put(status, timeToEnd);
    }

    public boolean hasStatus(Status status) {
        Long statusTime = statusMap.get(status);
        if (statusTime == null || statusTime < plugin.getWorldFullTime()) {
            // Status has expired or is unset
            statusMap.remove(status);
            return false;
        } else {
            // Still has status
            return true;
        }
    }

    public enum Status {
        IN_COMBAT,
        FRAMED,
        UNHOLY
    }

    public record StatusDataSave(HashMap<Status, Long> statusMap) {}
    public StatusDataSave getDataSave() {
        Long curTime = plugin.getWorldFullTime();
        for (Map.Entry<Status, Long> e : statusMap.entrySet())
            if (e.getValue() < curTime) statusMap.remove(e.getKey());
        return new StatusDataSave(statusMap);
    }
}
