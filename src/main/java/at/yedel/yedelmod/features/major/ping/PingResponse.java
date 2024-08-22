package at.yedel.yedelmod.features.major.ping;



import at.yedel.yedelmod.events.PacketEvent;
import at.yedel.yedelmod.utils.Chat;
import at.yedel.yedelmod.utils.Functions;
import at.yedel.yedelmod.utils.typeutils.TextUtils;
import net.minecraft.network.play.server.S37PacketStatistics;
import net.minecraft.network.play.server.S3APacketTabComplete;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;



public class PingResponse {
    private PingResponse() {}
    private static final PingResponse instance = new PingResponse();

    public static PingResponse getInstance() {
        return instance;
    }

    @SubscribeEvent
    public void onUnknownCommandResponse(ClientChatReceivedEvent event) {
        if (!PingSender.getInstance().commandCheck) return;
        if (event.message.getUnformattedText().contains("Unknown command")) {
            event.setCanceled(true);
            float delay = (float) (System.nanoTime() - PingSender.getInstance().lastTime) / 1000000;
            Chat.logoDisplay("&ePing: " + TextUtils.color(delay) + (int) delay + " &ems &7(command)");
            Functions.playSound("random.successful_hit", (float) (delay * -0.006 + 2));
            PingSender.getInstance().commandCheck = false;
        }
    }

    @SubscribeEvent
    public void onStatsPacket(PacketEvent.ReceiveEvent event) {
        if (!PingSender.getInstance().statsCheck) return;
        if (event.getPacket() instanceof S37PacketStatistics) {
            float delay = (float) (System.nanoTime() - PingSender.getInstance().lastTime) / 1000000;
            Chat.logoDisplay("&ePing: " + TextUtils.color(delay) + (int) delay + " &ems &7(stats)");
            Functions.playSound("random.successful_hit", (float) (delay * -0.006 + 2));
            PingSender.getInstance().statsCheck = false;
        }
    }

    @SubscribeEvent
    public void onTabPacket(PacketEvent.ReceiveEvent event) {
        if (!PingSender.getInstance().tabCheck) return;
        if (event.getPacket() instanceof S3APacketTabComplete) {
            float delay = (float) (System.nanoTime() - PingSender.getInstance().lastTime) / 1000000;
            Chat.logoDisplay("&ePing: " + TextUtils.color(delay) + (int) delay + " &ems &7(tab)");
            Functions.playSound("random.successful_hit", (float) (delay * -0.006 + 2));
            PingSender.getInstance().tabCheck = false;
        }
    }

    public void onHypixelPingPacket() {
        if (!PingSender.getInstance().hypixelCheck) return;
        float delay = (float) (System.nanoTime() - PingSender.getInstance().lastTime) / 1000000;
        Chat.logoDisplay("&ePing: " + TextUtils.color(delay) + (int) delay + " &ems &7(hypixel)");
        Functions.playSound("random.successful_hit", (float) (delay * -0.006 + 2));
        PingSender.getInstance().hypixelCheck = false;
    }

    // Server list handled in ping sender
}
