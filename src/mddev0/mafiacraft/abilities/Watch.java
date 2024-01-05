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
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;

public final class Watch implements Listener {

    private final MafiaCraft plugin;
    private final ProtocolManager manager;

    public Watch(MafiaCraft plugin, ProtocolManager manager) {
        this.plugin = plugin;
        this.manager = manager;
    }


    @SuppressWarnings("unused")
    @EventHandler
    public void onSpyglassLook(PlayerInteractEvent click) {
        if (!plugin.getActive()) return; // DO NOTHING IF NOT ACTIVE!
        if (click.getItem() != null && click.getItem().getType() == Material.SPYGLASS) {
            // Material is spyglass, check player
            MafiaPlayer clicker = plugin.getLivingPlayers().get(click.getPlayer().getUniqueId());
            if (clicker != null && clicker.getRole().getAbilities().contains(Ability.WATCH)) {

                // Player has right ability
                // create new runnable that keeps players visible until player stops using spyglass
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        // Spaghetti code time!
                        // Thanks to: https://www.spigotmc.org/threads/simulating-potion-effect-glowing-with-protocollib.218828/#post-2246160
                        ItemStack using = click.getPlayer().getItemInUse();
                        boolean active = using != null && using.getType() == Material.SPYGLASS;
                        if (active) {
                            // if spyglass is active, keep setting all players inside the spyglass as visible
                            for (Player p : plugin.getServer().getOnlinePlayers()) {
                                if (!p.equals(click.getPlayer()) && p.hasPotionEffect(PotionEffectType.INVISIBILITY)) {
                                    PacketContainer packet = manager.createPacket(PacketType.Play.Server.ENTITY_METADATA); // Create packet
                                    packet.getIntegers().write(0, p.getEntityId()); // Set player to modify

                                    // Lists which contain metadata values to modify
                                    // SEE:
                                    // https://wiki.vg/Entity_metadata#Entity_Metadata
                                    // https://github.com/dmulloy2/ProtocolLib/issues/2032
                                    StructureModifier<List<WrappedDataValue>> watchableAccessor = packet.getDataValueCollectionModifier();
                                    List<WrappedDataValue> values = Lists.newArrayList(
                                            new WrappedDataValue(0, WrappedDataWatcher.Registry.get(Byte.class), (byte)0x00) // 0 sets as visible
                                    );

                                    watchableAccessor.write(0, values);
                                    manager.sendServerPacket(click.getPlayer(), packet);
                                }
                            }
                        } else {
                            // spyglass is down, reset all invis players to invis
                            for (Player p : plugin.getServer().getOnlinePlayers()) {
                                if (!p.equals(click.getPlayer()) && p.hasPotionEffect(PotionEffectType.INVISIBILITY)) {
                                    PacketContainer packet = manager.createPacket(PacketType.Play.Server.ENTITY_METADATA); // Create packet
                                    packet.getIntegers().write(0, p.getEntityId()); // Set player to modify

                                    StructureModifier<List<WrappedDataValue>> watchableAccessor = packet.getDataValueCollectionModifier();
                                    List<WrappedDataValue> values = Lists.newArrayList(
                                            new WrappedDataValue(0, WrappedDataWatcher.Registry.get(Byte.class), (byte)0x20) // 0x20 sets as invisible
                                    );

                                    watchableAccessor.write(0, values);
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
