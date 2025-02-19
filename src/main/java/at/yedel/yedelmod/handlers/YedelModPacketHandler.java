package at.yedel.yedelmod.handlers;


import at.yedel.yedelmod.events.PacketEvent;
import cc.polyfrost.oneconfig.events.event.ReceivePacketEvent;
import cc.polyfrost.oneconfig.events.event.SendPacketEvent;
import cc.polyfrost.oneconfig.libs.eventbus.Subscribe;
import net.minecraftforge.common.MinecraftForge;



public class YedelModPacketHandler {
    private YedelModPacketHandler() {}

    private static final YedelModPacketHandler instance = new YedelModPacketHandler();

    public static YedelModPacketHandler getInstance() {
        return instance;
	}

    @Subscribe
    public void submitPacketSentEvent(SendPacketEvent event) {
        MinecraftForge.EVENT_BUS.post(new PacketEvent.SendEvent(event.packet));
    }

    @Subscribe
    public void submitPacketReceiveEvent(ReceivePacketEvent event) {
        MinecraftForge.EVENT_BUS.post(new PacketEvent.ReceiveEvent(event.packet));
	}
}
