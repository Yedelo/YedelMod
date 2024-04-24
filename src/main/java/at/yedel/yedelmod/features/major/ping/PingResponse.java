package at.yedel.yedelmod.features.major.ping;



import at.yedel.yedelmod.events.PacketEvent;
import at.yedel.yedelmod.utils.Chat;
import at.yedel.yedelmod.utils.typeutils.TextUtils;
import net.minecraft.network.play.server.S37PacketStatistics;
import net.minecraft.network.play.server.S3APacketTabComplete;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import static at.yedel.yedelmod.YedelMod.minecraft;

;



public class PingResponse {
    public static PingResponse instance = new PingResponse();
    @SubscribeEvent
    public void onUnknownCommandResponse(ClientChatReceivedEvent event) {
        if (!PingSender.instance.commandCheck) return;
        if (event.message.getUnformattedText().contains("Unknown command")) {
            event.setCanceled(true);
            float delay = (float) (System.nanoTime() - PingSender.instance.lastTime) / 1000000;
            Chat.logoDisplay("&ePing: " + TextUtils.color(delay) + (int) delay + " &ems &7(command)");
            minecraft.thePlayer.playSound("random.successful_hit", (float) 10, (float) (delay * -0.006 + 2));
            PingSender.instance.commandCheck = false;
        }
    }

    @SubscribeEvent
    public void onStatsPacket(PacketEvent.ReceiveEvent event) {
        if (!PingSender.instance.statsCheck) return;
        if (event.packet instanceof S37PacketStatistics) {
            float delay = (float) (System.nanoTime() - PingSender.instance.lastTime) / 1000000;
            Chat.logoDisplay("&ePing: " + TextUtils.color(delay) + (int) delay + " &ems &7(stats)");
            minecraft.thePlayer.playSound("random.successful_hit", (float) 10, (float) (delay * -0.006 + 2));
            PingSender.instance.statsCheck = false;
        }
    }

    @SubscribeEvent
    public void onTabPacket(PacketEvent.ReceiveEvent event) {
        if (!PingSender.instance.tabCheck) return;
        if (event.packet instanceof S3APacketTabComplete) {
            float delay = (float) (System.nanoTime() - PingSender.instance.lastTime) / 1000000;
            Chat.logoDisplay("&ePing: " + TextUtils.color(delay) + (int) delay + " &ems &7(tab)");
            minecraft.thePlayer.playSound("random.successful_hit", (float) 10, (float) (delay * -0.006 + 2));
            PingSender.instance.tabCheck = false;
        }
    }

    // Server list handled in ping sender
}
