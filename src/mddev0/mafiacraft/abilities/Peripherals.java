package mddev0.mafiacraft.abilities;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.reflect.StructureModifier;
import com.comphenix.protocol.wrappers.WrappedDataValue;
import com.comphenix.protocol.wrappers.WrappedDataWatcher;
import com.google.common.collect.Lists;
import mddev0.mafiacraft.MafiaCraft;
import mddev0.mafiacraft.player.MafiaPlayer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.potion.PotionEffectType;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public final class Peripherals implements Listener {

    private final MafiaCraft plugin;
    private final ProtocolManager manager;

    public Peripherals(MafiaCraft plugin, ProtocolManager manager) {
        this.plugin = plugin;
        this.manager = manager;
    }


    @SuppressWarnings("unused")
    @EventHandler
    public void onPlayerApproach(PlayerMoveEvent move) {
        if (!plugin.getActive()) return; // DO NOTHING IF NOT ACTIVE!
        // Scuffed: Yeah it just checks all players every time someone moves. "This is terrible. Oh well."
        for (Map.Entry<UUID, MafiaPlayer> viewer : plugin.getLivingPlayers().entrySet()) {
            if (!Bukkit.getOfflinePlayer(viewer.getKey()).isOnline()) continue; // Do nothing for offline players!
            if (viewer.getValue().getRole().getAbilities().contains(Ability.PERIPHERALS)) {
                // Reset all players first
                for (Player p : plugin.getServer().getOnlinePlayers()) {
                    if (p.hasPotionEffect(PotionEffectType.INVISIBILITY)) {
                        PacketContainer packet = manager.createPacket(PacketType.Play.Server.ENTITY_METADATA); // Create packet
                        packet.getIntegers().write(0, p.getEntityId()); // Set player to modify

                        // Lists which contain metadata values to modify
                        // SEE:
                        // https://wiki.vg/Entity_metadata#Entity_Metadata
                        // https://github.com/dmulloy2/ProtocolLib/issues/2032
                        StructureModifier<List<WrappedDataValue>> watchableAccessor = packet.getDataValueCollectionModifier();
                        List<WrappedDataValue> values = Lists.newArrayList(
                                new WrappedDataValue(0, WrappedDataWatcher.Registry.get(Byte.class), (byte)0x20) // 0x20 sets as invisible
                        );

                        watchableAccessor.write(0, values);
                        manager.sendServerPacket(Bukkit.getPlayer(viewer.getKey()), packet);
                    }
                }
                // Now remove invis for nearby players
                int range = plugin.getConfig().getInt("peripheralsRange");
                for (Entity ent : Objects.requireNonNull(Bukkit.getPlayer(viewer.getKey()))
                        .getNearbyEntities(range,range,range)) {
                    if (ent.getType() != EntityType.PLAYER) continue;
                    Player found = (Player) ent;
                    if (found.hasPotionEffect(PotionEffectType.INVISIBILITY)) {
                        PacketContainer packet = manager.createPacket(PacketType.Play.Server.ENTITY_METADATA); // Create packet
                        packet.getIntegers().write(0, ent.getEntityId()); // Set player to modify

                        StructureModifier<List<WrappedDataValue>> watchableAccessor = packet.getDataValueCollectionModifier();
                        List<WrappedDataValue> values = Lists.newArrayList(
                                new WrappedDataValue(0, WrappedDataWatcher.Registry.get(Byte.class), (byte)0x00) // 0 sets as visible
                        );

                        watchableAccessor.write(0, values);
                        manager.sendServerPacket(Bukkit.getPlayer(viewer.getKey()), packet);
                    }
                }
            }
        }
    }
}
