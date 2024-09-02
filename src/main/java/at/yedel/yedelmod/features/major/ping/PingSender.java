package at.yedel.yedelmod.features.major.ping;



import java.net.UnknownHostException;

import at.yedel.yedelmod.config.YedelConfig;
import at.yedel.yedelmod.handlers.OldServerPingResponder;
import at.yedel.yedelmod.utils.Chat;
import at.yedel.yedelmod.utils.Constants.Messages;
import at.yedel.yedelmod.utils.ThreadManager;
import at.yedel.yedelmod.utils.typeutils.TextUtils;
import gg.essential.api.EssentialAPI;
import net.hypixel.modapi.HypixelModAPI;
import net.hypixel.modapi.packet.impl.serverbound.ServerboundPingPacket;
import net.minecraft.network.play.client.C14PacketTabComplete;
import net.minecraft.network.play.client.C16PacketClientStatus;

import static at.yedel.yedelmod.YedelMod.minecraft;



public class PingSender {
    private PingSender() {}
    private static final PingSender instance = new PingSender();

    public static PingSender getInstance() {
        return instance;
    }

    private final C16PacketClientStatus.EnumState EnumRequestStats = C16PacketClientStatus.EnumState.REQUEST_STATS;
    public boolean commandCheck = false;
    public boolean statsCheck = false;
    public boolean tabCheck = false;
    public boolean hypixelCheck = false;
    public boolean serverListCheck = false;
    public long lastTime;

    public void processPingCommand(String pingArg) {
        if (pingArg == null) {
            defaultMethodPing();
            return;
        }
        switch (pingArg) {
            case "ping":
            case "p":
                Chat.command("ping");
                break;
            case "command":
            case "c":
                commandPing();
                break;
            case "tab":
            case "t":
                tabPing();
                break;
            case "stats":
            case "s":
                statsPing();
                break;
            case "list":
            case "l":
                serverListPing();
                break;
            case "hypixel":
            case "h":
                hypixelPing();
                break;
            default:
                defaultMethodPing();
                break;
        }
    }

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
        Chat.command("ping");
    }

    public void commandPing() {
        updateLastTime();
        Chat.command(TextUtils.randomUuid(8));
        commandCheck = true;
    }

    public void tabPing() {
        updateLastTime();
		minecraft.getNetHandler().addToSendQueue(new C14PacketTabComplete("#"));
        tabCheck = true;
    }

    public void statsPing() {
        updateLastTime();
        minecraft.getNetHandler().addToSendQueue(new C16PacketClientStatus(EnumRequestStats));
        statsCheck = true;
    }

    public void hypixelPing() {
        // Yedel uses essential features ???
        if (EssentialAPI.getMinecraftUtil().isHypixel()) {
            updateLastTime();
            HypixelModAPI.getInstance().sendPacket(new ServerboundPingPacket());
            hypixelCheck = true;
        }
        else Chat.display(Messages.notOnHypixelMessage);
    }

    public void serverListPing() {
        ThreadManager.scheduleOnce(() -> {
            try {
                new OldServerPingResponder().ping(minecraft.getCurrentServerData());
            }
            catch (UnknownHostException e) {
                Chat.display(Messages.failedServerPingMessage);
                e.printStackTrace();
            }
        }, 0);
    }

    public void updateLastTime() {
        lastTime = System.nanoTime();
    }
}
