package at.yedel.yedelmod.features.ping;



import at.yedel.yedelmod.utils.TextUtils;

//? if v0
//import cc.polyfrost.oneconfig.libs.universal.UChat;
import net.hypixel.modapi.HypixelModAPI;
import net.hypixel.modapi.packet.impl.serverbound.ServerboundPingPacket;
import net.minecraft.client.Minecraft;
//? if legacy {
/*import net.minecraft.network.play.client.C14PacketTabComplete;
import net.minecraft.network.play.client.C16PacketClientStatus;
*///?} else {
import net.minecraft.network.protocol.game.ServerboundClientCommandPacket;
import net.minecraft.network.protocol.game.ServerboundCommandSuggestionPacket;
//?}
import org.polyfrost.oneconfig.api.hypixel.v1.HypixelUtils;

import java.util.function.Function;



public enum PingMethod {
    //? if v0 {
    /*COMMAND_RESPONSE("Command", () -> UChat.say("/" + TextUtils.randomUuid(8))),
    TAB_PACKET("Tab", () -> Minecraft.getMinecraft().getNetHandler().addToSendQueue(new C14PacketTabComplete("#"))),
    STATS_PACKET("Stats", () -> Minecraft.getMinecraft().addToSendQueue(new C16PacketClientStatus(C16PacketClientStatus.EnumState.REQUEST_STATS))),
    HYPIXEL_PING("Hypixel", () -> {
        if (HypixelUtils.INSTANCE.isHypixel()) {
            HypixelModAPI.getInstance().sendPacket(new ServerboundPingPacket());
        }
        else {
            throw new PingException("You must be on Hypixel to use this!");
        }
    }),
    SERVER_LIST_PING("Server list", PingMethod::iGuessBro, (info) -> {
        if (Minecraft.getMinecraft().isSingleplayer()) {
            throw new PingException("This method does not work in singleplayer!");
        }
        long ping = Minecraft.getMinecraft().getCurrentServerData().pingToServer;
        if (ping == 0) {
            throw new PingException("Ping is 0! This might have occured if you used Direct Connect.");
        }
        return ping;
    });
    *///?} else {
    COMMAND_RESPONSE("Command", () -> Minecraft.getInstance().player.connection.sendChat("/" + TextUtils.randomUuid(8))),
    TAB_PACKET("Tab", () -> Minecraft.getInstance().player.connection.send(new ServerboundCommandSuggestionPacket(0, "#"))),
    STATS_PACKET("Stats", () -> Minecraft.getInstance().player.connection.send(new ServerboundClientCommandPacket(ServerboundClientCommandPacket.Action.REQUEST_STATS))),
    HYPIXEL_PING("Hypixel", () -> {
        if (HypixelUtils.isHypixel()) {
            HypixelModAPI.getInstance().sendPacket(new ServerboundPingPacket());
        }
        else {
            throw new PingException("You must be on Hypixel to use this!");
        }
    }),
    SERVER_LIST_PING("Server list", PingMethod::iGuessBro, (info) -> {
        if (Minecraft.getInstance().getSingleplayerServer() != null) {
            throw new PingException("This method does not work in singleplayer!");
        }
        long ping = Minecraft.getInstance().getCurrentServer().ping;
        if (ping == 0) {
            throw new PingException("Ping is 0! This might have occured if you used Direct Connect.");
        }
        return ping;
    });
    //?}

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
