package at.yedel.yedelmod.features.ping;



import at.yedel.yedelmod.utils.TextUtils;
import cc.polyfrost.oneconfig.libs.universal.UChat;
import cc.polyfrost.oneconfig.libs.universal.UMinecraft;
import cc.polyfrost.oneconfig.utils.hypixel.HypixelUtils;
import net.hypixel.modapi.HypixelModAPI;
import net.hypixel.modapi.packet.impl.serverbound.ServerboundPingPacket;
import net.minecraft.network.play.client.C14PacketTabComplete;
import net.minecraft.network.play.client.C16PacketClientStatus;

import java.util.function.Function;



public enum PingMethod {
    COMMAND_RESPONSE("Command", () -> UChat.say("/" + TextUtils.randomUuid(8))),
    TAB_PACKET("Tab", () -> UMinecraft.getNetHandler().addToSendQueue(new C14PacketTabComplete("#"))),
    STATS_PACKET("Stats", () -> UMinecraft.getNetHandler().addToSendQueue(new C16PacketClientStatus(C16PacketClientStatus.EnumState.REQUEST_STATS))),
    HYPIXEL_PING("Hypixel", () -> {
        if (HypixelUtils.INSTANCE.isHypixel()) {
            HypixelModAPI.getInstance().sendPacket(new ServerboundPingPacket());
        }
        else {
            throw new PingException("You must be on Hypixel to use this!");
        }
    }),
    SERVER_LIST_PING("Server list", PingMethod::iGuessBro, (info) -> {
        if (UMinecraft.getMinecraft().isSingleplayer()) {
            throw new PingException("This method does not work in singleplayer!");
        }
        long ping = UMinecraft.getMinecraft().getCurrentServerData().pingToServer;
        if (ping == 0) {
            throw new PingException("Ping is 0! This might have occured if you used Direct Connect.");
        }
        return ping;
    });

    public final String friendlyName;
    public final Runnable starting;
    public final Function<PingQueueInfo, Long> calculator;

    PingMethod(String friendlyName, Runnable starting) {
        this(friendlyName, starting, (info) -> (info.endTime() - info.startTime()) / 1000000);
    }

    PingMethod(String friendlyName, Runnable starting, Function<PingQueueInfo, Long> calculator) {
        this.friendlyName = friendlyName;
        this.starting = starting;
        this.calculator = calculator;
    }

    private static void iGuessBro() {
        PingQueue.getInstance().post(PingMethod.SERVER_LIST_PING);
    }
}
