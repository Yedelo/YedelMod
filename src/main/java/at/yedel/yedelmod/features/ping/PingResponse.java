package at.yedel.yedelmod.features.ping;



import at.yedel.yedelmod.utils.Constants;
import dev.deftu.omnicore.client.OmniChat;
import dev.deftu.omnicore.client.OmniClient;
import dev.deftu.omnicore.client.OmniClientSound;
import net.minecraft.network.play.server.S37PacketStatistics;
import net.minecraft.network.play.server.S3APacketTabComplete;
import org.polyfrost.oneconfig.api.event.v1.events.ChatEvent;
import org.polyfrost.oneconfig.api.event.v1.events.PacketEvent;
import org.polyfrost.oneconfig.api.event.v1.invoke.impl.Subscribe;

import static at.yedel.yedelmod.launch.YedelModConstants.logo;



public class PingResponse {
    private PingResponse() {}

    private static final PingResponse instance = new PingResponse();

    public static PingResponse getInstance() {
        return instance;
    }

    @Subscribe
    public void handleCommandPingResponse(ChatEvent.Receive event) {
        if (!PingSender.getInstance().commandCheck) return;
        if (event.getFullyUnformattedMessage().contains("Unknown command")) {
            event.cancelled = true;
            float delay = (float) (System.nanoTime() - PingSender.getInstance().lastTime) / 1000000;
            OmniChat.displayClientMessage(logo + " §ePing: " + color(delay) + (int) delay + " §ems §7(command)");
            OmniClientSound.play(Constants.plingSound, 1, (float) (delay * -0.006 + 2));
            PingSender.getInstance().commandCheck = false;
        }
    }

    @Subscribe
    public void handleStatsPingResponse(PacketEvent.Receive event) {
        if (!PingSender.getInstance().statsCheck) return;
        if (event.getPacket() instanceof S37PacketStatistics) {
            float delay = (float) (System.nanoTime() - PingSender.getInstance().lastTime) / 1000000;
            OmniChat.displayClientMessage(logo + " §ePing: " + color(delay) + (int) delay + " §ems §7(stats)");
            OmniClientSound.play(Constants.plingSound, 1, (float) (delay * -0.006 + 2));
            PingSender.getInstance().statsCheck = false;
        }
    }

    @Subscribe
    public void handleTabPingResponse(PacketEvent.Receive event) {
        if (!PingSender.getInstance().tabCheck) return;
        if (event.getPacket() instanceof S3APacketTabComplete) {
            float delay = (float) (System.nanoTime() - PingSender.getInstance().lastTime) / 1000000;
            OmniChat.displayClientMessage(logo + " §ePing: " + color(delay) + (int) delay + " §ems §7(tab)");
            OmniClientSound.play(Constants.plingSound, 1, (float) (delay * -0.006 + 2));
            PingSender.getInstance().tabCheck = false;
        }
    }

    public void handleHypixelPingResponse() {
        if (!PingSender.getInstance().hypixelCheck) return;
        float delay = (float) (System.nanoTime() - PingSender.getInstance().lastTime) / 1000000;
        OmniChat.displayClientMessage(logo + " §ePing: " + color(delay) + (int) delay + " §ems §7(hypixel)");
        OmniClientSound.play(Constants.plingSound, 1, (float) (delay * -0.006 + 2));
        PingSender.getInstance().hypixelCheck = false;
    }

    public void handleServerListPing() {
        if (OmniClient.getInstance().isSingleplayer()) {
            OmniChat.displayClientMessage(logo + " §cThis method does not work in singleplayer!");
        }
        float ping = (float) OmniClient.getInstance().getCurrentServerData().pingToServer;
        if (ping == 0) {
            OmniChat.displayClientMessage(logo + " §cPing is 0! This might have occured if you used Direct Connect or the favorite server button.");
        }
        else {
            OmniChat.displayClientMessage(logo + " §ePing: " + color(ping) + (int) ping + " §ems §7(server list)");
            OmniClientSound.play(Constants.plingSound, 1, (float) (ping * -0.006 + 2));
        }
    }

    private String color(Float ping) {
        if (ping < 50) return "§a";
        else if (ping < 100) return "§2";
        else if (ping < 150) return "§e";
        else if (ping < 200) return "§6";
        else if (ping < 250) return "§c";
        else if (ping < 300) return "§4";
        else if (ping < 350) return "§5"; // wtf?
        else if (ping < 400) return "§d";
        else if (ping < 450) return "§f";
        else if (ping < 500) return "§b";
        else if (ping < 550) return "§3";
        else if (ping < 600) return "§9";
        else if (ping < 650) return "§1";
        else if (ping < 700) return "§7";
        else if (ping < 740) return "§8";
        else return "§0";
    }
}
