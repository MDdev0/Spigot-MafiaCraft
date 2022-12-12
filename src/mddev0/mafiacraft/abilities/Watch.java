package mddev0.mafiacraft.abilities;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.WrappedDataWatcher;
import mddev0.mafiacraft.MafiaCraft;
import mddev0.mafiacraft.util.MafiaPlayer;
import mddev0.mafiacraft.util.SpyglassUtil;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

public final class Watch implements Listener {

    private final MafiaCraft plugin;
    private final ProtocolManager manager;

    public Watch(MafiaCraft plugin, ProtocolManager manager) {
        this.plugin = plugin;
        this.manager = manager;
    }

    // SCUFFED: THIS PROBABLY IS FULL OF BUGS!

    @EventHandler
    public void onSpyglassLook(PlayerInteractEvent click) {
        if (!plugin.getActive()) return; // DO NOTHING IF NOT ACTIVE!
        if (click.getItem() != null && click.getItem().getType() == Material.SPYGLASS) {
            // Material is spyglass, check player
            MafiaPlayer clicker = plugin.getLivingPlayers().get(click.getPlayer().getUniqueId());
            if (clicker != null && clicker.getRole().hasAbility(Ability.WATCH)) {
                // Player has right ability
                SpyglassUtil spyglass = clicker.getSpyglass();
                spyglass.refresh();
                if (!spyglass.isSpyglassActive()) {
                    // create new runnable that keeps players visible until player stops using spyglass
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            // Spaghetti code time!
                            // Thanks to: https://www.spigotmc.org/threads/simulating-potion-effect-glowing-with-protocollib.218828/#post-2246160
                            if (spyglass.isSpyglassActive()) {
                                // if spyglass is active, keep setting all players inside the spyglass as visible
                                for (Player p : plugin.getServer().getOnlinePlayers()) {
                                    if (!p.equals(click.getPlayer()) && p.hasPotionEffect(PotionEffectType.INVISIBILITY)) {
                                        PacketContainer packet = new PacketContainer(PacketType.Play.Server.ENTITY_METADATA);
                                        packet.getIntegers().write(0, p.getEntityId());
                                        WrappedDataWatcher watcher = new WrappedDataWatcher();
                                        WrappedDataWatcher.Serializer serializer = WrappedDataWatcher.Registry.get(Byte.class);
                                        watcher.setEntity(p);
                                        watcher.setObject(0, serializer, (byte) (0)); // 0 remove invis
                                        packet.getWatchableCollectionModifier().write(0, watcher.getWatchableObjects());
                                        manager.sendServerPacket(click.getPlayer(), packet);
                                    }
                                }
                            } else {
                                // spyglass is down, reset all invis players to invis
                                for (Player p : plugin.getServer().getOnlinePlayers()) {
                                    if (!p.equals(click.getPlayer()) && p.hasPotionEffect(PotionEffectType.INVISIBILITY)) {
                                        PacketContainer packet = new PacketContainer(PacketType.Play.Server.ENTITY_METADATA);
                                        packet.getIntegers().write(0, p.getEntityId());
                                        WrappedDataWatcher watcher = new WrappedDataWatcher();
                                        WrappedDataWatcher.Serializer serializer = WrappedDataWatcher.Registry.get(Byte.class);
                                        watcher.setEntity(p);
                                        watcher.setObject(0, serializer, (byte) (0x20)); // 0x20 set invis
                                        packet.getWatchableCollectionModifier().write(0, watcher.getWatchableObjects());
                                        manager.sendServerPacket(click.getPlayer(), packet);
                                    }
                                }
                                // Cancel the timer
                                this.cancel();
                            }
                        }
                    }.runTaskTimer(plugin,1L,1L);
                }
            }
        }
    }
}
