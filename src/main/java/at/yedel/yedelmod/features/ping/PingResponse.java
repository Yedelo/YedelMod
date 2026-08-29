package at.yedel.yedelmod.features.ping;



import net.hypixel.modapi.HypixelModAPI;
import net.hypixel.modapi.error.ErrorReason;
import net.hypixel.modapi.packet.impl.clientbound.ClientboundPingPacket;
import net.minecraft.network.protocol.game.ClientboundAwardStatsPacket;
import net.minecraft.network.protocol.game.ClientboundCommandSuggestionsPacket;
import org.polyfrost.oneconfig.api.event.v1.events.ChatEvent;
import org.polyfrost.oneconfig.api.event.v1.events.PacketEvent;
import org.polyfrost.oneconfig.api.event.v1.invoke.impl.Subscribe;



public class PingResponse {
    private static final PingResponse INSTANCE = new PingResponse();

    public static PingResponse getInstance() {
        return INSTANCE;
    }

    private PingResponse() {
        HypixelModAPI.getInstance().createHandler(ClientboundPingPacket.class, this::handleHypixelPingResponse).onError(this::handlePingError);
    }

    @Subscribe
    public void handleCommandPingResponse(ReceiveChatEvent event) {
        if (event.getFullyUnformattedMessage().contains("Unknown command")) {
            if (PingQueue.getInstance().post(PingMethod.COMMAND_RESPONSE)) {
                event.cancelled = true;
            }
        }
    }

    @Subscribe
    public void handleStatsPingResponse(PacketEvent.Receive event) {
        if (event.getPacket() instanceof ClientboundAwardStatsPacket) {
            PingQueue.getInstance().post(PingMethod.STATS_PACKET);
        }
    }

    @Subscribe
    public void handleTabPingResponse(PacketEvent.Receive event) {
        if (event.getPacket() instanceof ClientboundCommandSuggestionsPacket) {
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
