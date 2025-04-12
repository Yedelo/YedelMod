package at.yedel.yedelmod.features.ping;



import at.yedel.yedelmod.config.YedelConfig;
import at.yedel.yedelmod.utils.typeutils.TextUtils;
import dev.deftu.omnicore.client.OmniChat;
import dev.deftu.omnicore.client.OmniClient;
import net.hypixel.modapi.HypixelModAPI;
import net.hypixel.modapi.packet.impl.serverbound.ServerboundPingPacket;
import net.minecraft.network.play.client.C14PacketTabComplete;
import net.minecraft.network.play.client.C16PacketClientStatus;
import org.polyfrost.oneconfig.api.hypixel.v1.HypixelUtils;

import static at.yedel.yedelmod.launch.YedelModConstants.logo;



public class PingSender {
    private PingSender() {}

    private static final PingSender instance = new PingSender();

    public static PingSender getInstance() {
        return instance;
    }

    public boolean commandCheck = false;
    public boolean statsCheck = false;
    public boolean tabCheck = false;
    public boolean hypixelCheck = false;
    public long lastTime;

    public void defaultMethodPing() {
        switch (YedelConfig.getInstance().pingMethod) {
            case 0:
                pingPing();
                break;
            case 1:
                commandPing();
                break;
            case 2:
                tabPing();
                break;
            case 3:
                statsPing();
                break;
            case 5:
                hypixelPing();
                break;
            case 4:
            default:
                serverListPing();
                break;
        }
    }

    public void pingPing() {
        OmniChat.sendPlayerMessage("/ping");
    }

    public void commandPing() {
        lastTime = System.nanoTime();
        OmniChat.sendPlayerMessage("/" + TextUtils.randomUuid(8));
        commandCheck = true;
    }

    public void tabPing() {
        lastTime = System.nanoTime();
        OmniClient.getInstance().getNetHandler().addToSendQueue(new C14PacketTabComplete("#"));
        tabCheck = true;
    }

    public void statsPing() {
        lastTime = System.nanoTime();
        OmniClient.getInstance().getNetHandler().addToSendQueue(new C16PacketClientStatus(C16PacketClientStatus.EnumState.REQUEST_STATS));
        statsCheck = true;
    }

    public void hypixelPing() {
        // Yedel no longer uses essential features
        if (HypixelUtils.isHypixel()) {
            lastTime = System.nanoTime();
            HypixelModAPI.getInstance().sendPacket(new ServerboundPingPacket());
            hypixelCheck = true;
        }
        else OmniChat.displayClientMessage(logo + " §cYou must be on Hypixel to use this!");
    }

    public void serverListPing() {
        PingResponse.getInstance().handleServerListPing();
    }
}
