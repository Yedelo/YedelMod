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
    COMMAND_RESPONSE(() -> UChat.say("/" + TextUtils.randomUuid(8))),
    TAB_PACKET(() -> UMinecraft.getNetHandler().addToSendQueue(new C14PacketTabComplete("#"))),
    STATS_PACKET(() -> UMinecraft.getNetHandler().addToSendQueue(new C16PacketClientStatus(C16PacketClientStatus.EnumState.REQUEST_STATS))),
    HYPIXEL_PING(() -> {
        if (HypixelUtils.INSTANCE.isHypixel()) {
            HypixelModAPI.getInstance().sendPacket(new ServerboundPingPacket());
        }
        else {
            throw new PingException("You must be on Hypixel to use this!");
        }
    }),
    SERVER_LIST_PING(PingMethod::iGuessBro, (info) -> {
        if (UMinecraft.getMinecraft().isSingleplayer()) {
            throw new PingException("This method does not work in singleplayer!");
        }
        long ping = UMinecraft.getMinecraft().getCurrentServerData().pingToServer;
        if (ping == 0) {
            throw new PingException("Ping is 0! This might have occured if you used Direct Connect or the favorite server button.");
        }
        return ping;
    });

    public final Runnable starting;
    public final Function<PingQueueInfo, Long> calculator;

    PingMethod(Runnable starting) {
        this(starting, (info) -> (info.endTime() - info.startTime()) / 1000000);
    }

    PingMethod(Runnable starting, Function<PingQueueInfo, Long> calculator) {
        this.starting = starting;
        this.calculator = calculator;
    }

    private static void iGuessBro() {
        PingQueue.getInstance().post(PingMethod.SERVER_LIST_PING);
    }
}
