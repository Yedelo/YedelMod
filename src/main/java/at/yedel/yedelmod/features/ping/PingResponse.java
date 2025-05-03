package at.yedel.yedelmod.features.ping;



import at.yedel.yedelmod.utils.Constants;
import cc.polyfrost.oneconfig.events.event.ChatReceiveEvent;
import cc.polyfrost.oneconfig.events.event.ReceivePacketEvent;
import cc.polyfrost.oneconfig.libs.eventbus.Subscribe;
import cc.polyfrost.oneconfig.libs.universal.UChat;
import cc.polyfrost.oneconfig.libs.universal.UMinecraft;
import cc.polyfrost.oneconfig.libs.universal.USound;
import net.hypixel.modapi.HypixelModAPI;
import net.hypixel.modapi.packet.impl.clientbound.ClientboundPingPacket;
import net.minecraft.network.play.server.S37PacketStatistics;
import net.minecraft.network.play.server.S3APacketTabComplete;
import net.minecraft.util.ResourceLocation;

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
            float ping = getPing();
            UChat.chat(yedelogo + " &ePing: " + color(ping) + (int) ping + " &ems &7(command)");
            playPingSound(ping);
            PingSender.getInstance().commandCheck = false;
        }
    }

    @Subscribe
    public void handleStatsPingResponse(ReceivePacketEvent event) {
        if (!PingSender.getInstance().statsCheck) return;
        if (event.packet instanceof S37PacketStatistics) {
            float ping = getPing();
            UChat.chat(yedelogo + " &ePing: " + color(ping) + (int) ping + " &ems &7(stats)");
            USound.INSTANCE.playSoundStatic(Constants.PLING_SOUND_LOCATION, 1, (float) (ping * -0.006 + 2));
            PingSender.getInstance().statsCheck = false;
        }
    }

    @Subscribe
    public void handleTabPingResponse(ReceivePacketEvent event) {
        if (!PingSender.getInstance().tabCheck) return;
        if (event.packet instanceof S3APacketTabComplete) {
            float ping = getPing();
            UChat.chat(yedelogo + " &ePing: " + color(ping) + (int) ping + " &ems &7(tab)");
            USound.INSTANCE.playSoundStatic(Constants.PLING_SOUND_LOCATION, 1, (float) (ping * -0.006 + 2));
            PingSender.getInstance().tabCheck = false;
        }
    }

    public void handleHypixelPingResponse(ClientboundPingPacket packet) {
        if (!PingSender.getInstance().hypixelCheck) return;
        float ping = getPing();
        UChat.chat(yedelogo + " &ePing: " + color(ping) + (int) ping + " &ems &7(hypixel)");
        USound.INSTANCE.playSoundStatic(Constants.PLING_SOUND_LOCATION, 1, (float) (ping * -0.006 + 2));
        PingSender.getInstance().hypixelCheck = false;
    }

    public void handleServerListPing() {
        if (UMinecraft.getMinecraft().isSingleplayer()) {
            UChat.chat(yedelogo + " §cThis method does not work in singleplayer!");
        }
        float ping = (float) UMinecraft.getMinecraft().getCurrentServerData().pingToServer;
        if (ping == 0) {
            UChat.chat(yedelogo + " §cPing is 0! This might have occured if you used Direct Connect or the favorite server button.");
        }
        else {
            UChat.chat(yedelogo + " &ePing: " + color(ping) + (int) ping + " &ems &7(server list)");
            USound.INSTANCE.playSoundStatic(new ResourceLocation("random.successful_hit"), 1, (float) (ping * -0.006 + 2));
        }
    }

    private void playPingSound(float ping) {
        USound.INSTANCE.playSoundStatic(Constants.PLING_SOUND_LOCATION, 1, (float) (ping * -0.006 + 2));
    }

    private float getPing() {
        return (float) (System.nanoTime() - PingSender.getInstance().lastTime) / 1000000;
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
        else if (ping < 750) return "§8";
        else return "§0";
    }
}
