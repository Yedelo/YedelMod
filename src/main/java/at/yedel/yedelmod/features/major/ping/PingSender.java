package at.yedel.yedelmod.features.major.ping;



import at.yedel.yedelmod.config.YedelConfig;
import at.yedel.yedelmod.utils.Chat;
import at.yedel.yedelmod.utils.Constants.Messages;
import at.yedel.yedelmod.utils.Functions;
import at.yedel.yedelmod.utils.typeutils.TextUtils;
import cc.polyfrost.oneconfig.utils.hypixel.HypixelUtils;
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
        Chat.command("ping");
    }

    public void commandPing() {
        lastTime = System.nanoTime();
        Chat.command(TextUtils.randomUuid(8));
        commandCheck = true;
    }

    public void tabPing() {
        lastTime = System.nanoTime();
		minecraft.getNetHandler().addToSendQueue(new C14PacketTabComplete("#"));
        tabCheck = true;
    }

    public void statsPing() {
        lastTime = System.nanoTime();
        minecraft.getNetHandler().addToSendQueue(new C16PacketClientStatus(EnumRequestStats));
        statsCheck = true;
    }

    public void hypixelPing() {
        if (HypixelUtils.INSTANCE.isHypixel()) {
            lastTime = System.nanoTime();
            HypixelModAPI.getInstance().sendPacket(new ServerboundPingPacket());
            hypixelCheck = true;
        }
        else Chat.display(Messages.notOnHypixel);
    }

    public void serverListPing() {
        if (minecraft.isSingleplayer()) Chat.display(Messages.listPingInSingleplayer);
        float ping = (float) minecraft.getCurrentServerData().pingToServer;
        if (ping == 0)
            Chat.display(Messages.pingIs0);
        else {
            Chat.logoDisplay("&ePing: " + TextUtils.color(ping) + (int) ping + " &ems &7(server list)");
            Functions.playSound("random.successful_hit", (float) (ping * -0.006 + 2));
        }
    }
}
