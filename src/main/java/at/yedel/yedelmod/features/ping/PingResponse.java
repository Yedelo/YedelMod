package at.yedel.yedelmod.features.ping;



import cc.polyfrost.oneconfig.events.event.ChatReceiveEvent;
import cc.polyfrost.oneconfig.events.event.ReceivePacketEvent;
import cc.polyfrost.oneconfig.libs.eventbus.Subscribe;
import net.hypixel.modapi.HypixelModAPI;
import net.hypixel.modapi.error.ErrorReason;
import net.hypixel.modapi.packet.impl.clientbound.ClientboundPingPacket;
import net.minecraft.network.play.server.S37PacketStatistics;
import net.minecraft.network.play.server.S3APacketTabComplete;



public class PingResponse {
    private static final PingResponse INSTANCE = new PingResponse();

    public static PingResponse getInstance() {
        return INSTANCE;
    }

    private PingResponse() {
        HypixelModAPI.getInstance().createHandler(ClientboundPingPacket.class, this::handleHypixelPingResponse).onError(this::handlePingError);
    }

    @Subscribe
    public void handleCommandPingResponse(ChatReceiveEvent event) {
        if (event.message.getUnformattedText().contains("Unknown command")) {
            if (PingQueue.getInstance().post(PingMethod.COMMAND_RESPONSE)) {
                event.isCancelled = true;
            }
        }
    }

    @Subscribe
    public void handleStatsPingResponse(ReceivePacketEvent event) {
        if (event.packet instanceof S37PacketStatistics) {
            PingQueue.getInstance().post(PingMethod.STATS_PACKET);
        }
    }

    @Subscribe
    public void handleTabPingResponse(ReceivePacketEvent event) {
        if (event.packet instanceof S3APacketTabComplete) {
            PingQueue.getInstance().post(PingMethod.TAB_PACKET);
        }
    }

    public void handleHypixelPingResponse(ClientboundPingPacket packet) {
        PingQueue.getInstance().post(PingMethod.HYPIXEL_PING);
    }

    private void handlePingError(ErrorReason reason) {
        PingQueueInfo queued = PingQueue.getInstance().getQueue().get(PingMethod.HYPIXEL_PING);
        if (queued != null) {
            queued.handleError(new PingException("Hypixel ping errored with reason " + ErrorReason.getById(reason.getId()) + "!"));
        }
    }
}
