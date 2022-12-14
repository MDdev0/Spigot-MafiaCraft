package mddev0.mafiacraft.abilities;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.WrappedDataWatcher;
import mddev0.mafiacraft.MafiaCraft;
import mddev0.mafiacraft.util.MafiaPlayer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.potion.PotionEffectType;

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
        for (Map.Entry<UUID, MafiaPlayer> executor : plugin.getLivingPlayers().entrySet()) {
            if (!Bukkit.getOfflinePlayer(executor.getKey()).isOnline()) continue; // Do nothing for offline players!
            if (executor.getValue().getRole().hasAbility(Ability.PERIPHERALS)) {
                // Reset all players first
                for (Player p : plugin.getServer().getOnlinePlayers()) {
                    if (p.hasPotionEffect(PotionEffectType.INVISIBILITY)) {
                        PacketContainer packet = new PacketContainer(PacketType.Play.Server.ENTITY_METADATA);
                        packet.getIntegers().write(0, p.getEntityId());
                        WrappedDataWatcher watcher = new WrappedDataWatcher();
                        WrappedDataWatcher.Serializer serializer = WrappedDataWatcher.Registry.get(Byte.class);
                        watcher.setEntity(p);
                        watcher.setObject(0, serializer, (byte) (0x20)); // 0x20 set invis
                        packet.getWatchableCollectionModifier().write(0, watcher.getWatchableObjects());
                        manager.sendServerPacket(Bukkit.getPlayer(executor.getKey()), packet);
                    }
                }
                // Now remove invis for nearby players
                int range = plugin.getConfig().getInt("peripheralsRange");
                for (Entity ent : Objects.requireNonNull(Bukkit.getPlayer(executor.getKey()))
                        .getNearbyEntities(range,range,range)) {
                    if (ent.getType() != EntityType.PLAYER) continue;
                    Player found = (Player) ent;
                    if (found.hasPotionEffect(PotionEffectType.INVISIBILITY)) {
                        PacketContainer packet = new PacketContainer(PacketType.Play.Server.ENTITY_METADATA);
                        packet.getIntegers().write(0, found.getEntityId());
                        WrappedDataWatcher watcher = new WrappedDataWatcher();
                        WrappedDataWatcher.Serializer serializer = WrappedDataWatcher.Registry.get(Byte.class);
                        watcher.setEntity(found);
                        watcher.setObject(0, serializer, (byte) (0)); // 0 remove invis
                        packet.getWatchableCollectionModifier().write(0, watcher.getWatchableObjects());
                        manager.sendServerPacket(Bukkit.getPlayer(executor.getKey()), packet);
                    }
                }
            }
        }
    }
}
