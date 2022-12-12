package mddev0.mafiacraft.util;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import mddev0.mafiacraft.MafiaCraft;

public class HardcoreHearts extends PacketAdapter {

    private final MafiaCraft plugin;

    public HardcoreHearts(MafiaCraft plugin) {
        super(plugin, ListenerPriority.NORMAL, PacketType.Play.Server.LOGIN);
        this.plugin = plugin;
    }

    @Override
    public void onPacketSending(PacketEvent event) {
        if (event.getPacketType() == PacketType.Play.Server.LOGIN) {
            PacketContainer pack = event.getPacket();
            // TODO: SET THIS UP IN CONFIG!
            boolean toSet = true;
            pack.getBooleans().write(0, toSet);
        }
    }


}
