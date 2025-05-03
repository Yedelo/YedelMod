package at.yedel.yedelmod.features.ping;



import at.yedel.yedelmod.utils.Constants;
import at.yedel.yedelmod.utils.typeutils.TextUtils;
import cc.polyfrost.oneconfig.events.event.ChatReceiveEvent;
import cc.polyfrost.oneconfig.events.event.ReceivePacketEvent;
import cc.polyfrost.oneconfig.libs.eventbus.Subscribe;
import cc.polyfrost.oneconfig.libs.universal.UChat;
import cc.polyfrost.oneconfig.libs.universal.USound;
import net.hypixel.modapi.HypixelModAPI;
import net.hypixel.modapi.packet.impl.clientbound.ClientboundPingPacket;
import net.minecraft.network.play.server.S37PacketStatistics;
import net.minecraft.network.play.server.S3APacketTabComplete;

import static at.yedel.yedelmod.launch.YedelModConstants.yedelogo;


public class PingResponse {
    private static final PingResponse INSTANCE = new PingResponse();

    public static PingResponse getInstance() {
        return INSTANCE;
    }

    private PingResponse() {
        // note: using the method reference PingResponse.getInstance()::handleHypixelPingResponse doesn't work
        // because that would be referring to the instance in the constructor
        HypixelModAPI.getInstance().registerHandler(ClientboundPingPacket.class, this::handleHypixelPingResponse);
    }

    @Subscribe
    public void handleCommandPingResponse(ChatReceiveEvent event) {
        if (!PingSender.getInstance().commandCheck) return;
        if (event.message.getUnformattedText().contains("Unknown command")) {
            event.isCancelled = true;
            float delay = (float) (System.nanoTime() - PingSender.getInstance().lastTime) / 1000000;
            UChat.chat(yedelogo + " &ePing: " + TextUtils.color(delay) + (int) delay + " &ems &7(command)");
            USound.INSTANCE.playSoundStatic(Constants.PLING_SOUND_LOCATION, 1, (float) (delay * -0.006 + 2));
            PingSender.getInstance().commandCheck = false;
        }
    }

    @Subscribe
    public void handleStatsPingResponse(ReceivePacketEvent event) {
        if (!PingSender.getInstance().statsCheck) return;
        if (event.packet instanceof S37PacketStatistics) {
            float delay = (float) (System.nanoTime() - PingSender.getInstance().lastTime) / 1000000;
            UChat.chat(yedelogo + " &ePing: " + TextUtils.color(delay) + (int) delay + " &ems &7(stats)");
            USound.INSTANCE.playSoundStatic(Constants.PLING_SOUND_LOCATION, 1, (float) (delay * -0.006 + 2));
            PingSender.getInstance().statsCheck = false;
        }
    }

    @Subscribe
    public void handleTabPingResponse(ReceivePacketEvent event) {
        if (!PingSender.getInstance().tabCheck) return;
        if (event.packet instanceof S3APacketTabComplete) {
            float delay = (float) (System.nanoTime() - PingSender.getInstance().lastTime) / 1000000;
            UChat.chat(yedelogo + " &ePing: " + TextUtils.color(delay) + (int) delay + " &ems &7(tab)");
            USound.INSTANCE.playSoundStatic(Constants.PLING_SOUND_LOCATION, 1, (float) (delay * -0.006 + 2));
            PingSender.getInstance().tabCheck = false;
        }
    }

    public void handleHypixelPingResponse(ClientboundPingPacket packet) {
        if (!PingSender.getInstance().hypixelCheck) return;
        float delay = (float) (System.nanoTime() - PingSender.getInstance().lastTime) / 1000000;
        UChat.chat(yedelogo + " &ePing: " + TextUtils.color(delay) + (int) delay + " &ems &7(hypixel)");
        USound.INSTANCE.playSoundStatic(Constants.PLING_SOUND_LOCATION, 1, (float) (delay * -0.006 + 2));
        PingSender.getInstance().hypixelCheck = false;
    }

    // Server list handled in ping sender
}
