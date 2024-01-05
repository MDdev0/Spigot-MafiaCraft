package mddev0.mafiacraft.player;

import mddev0.mafiacraft.abilities.Ability;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.HashSet;

/**
 * SCUFFED: Storing this this way was a stupid idea, but we're committed lmao
 *  Serializing this is literally hell, DO NOT do what I did
 *  this should be rewritten by someone who is an actual CS major
 */

public class RoleData {
    private final HashMap<DataType, Object> dataMap;

    public RoleData() {
        this.dataMap = new HashMap<>();
    }

    public RoleData(RoleDataSave saveData) {
        this.dataMap = saveData.dataMap();
    }

    public Object getData(DataType key) {
        return dataMap.get(key);
    }

    public void setData(DataType key, Object data) throws IllegalArgumentException {
        if (key.getType().equals(data.getClass())) {
            dataMap.put(key, data);
        } else {
            throw new IllegalArgumentException("Got type " + data.getClass().getTypeName() + ", expected " + key.getType().getTypeName());
        }
    }

    public record RoleDataSave(HashMap<DataType, Object> dataMap) {}
    public RoleDataSave getDataSave() {
        return new RoleDataSave(dataMap);
    }

    public enum DataType {
        WEREWOLF_TRANSFORM(Boolean.class),
        WEREWOLF_KILLS(Integer.class),
        SORCERER_SELECTED(Ability.class),
        SORCERER_ALIGNMENT(String.class),
        JESTER_ABILITY_USED(Boolean.class),
        HUNTER_TARGETS(HashSet.class),
        BODYGUARD_PROTECTEE(String.class);

        private final Type TYPENAME;
        DataType(Type typename) {
            this.TYPENAME = typename;
        }

        public Type getType() {
            return TYPENAME;
        }
    }
}
