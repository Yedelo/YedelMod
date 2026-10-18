package at.yedel.yedelmod.features.ping;



import at.yedel.yedelmod.utils.TextUtils;
import net.minecraft.client.Minecraft;

import org.polyfrost.oneconfig.api.hypixel.v1.HypixelUtils;
import net.hypixel.modapi.HypixelModAPI;
import net.hypixel.modapi.packet.impl.serverbound.ServerboundPingPacket;

//? if legacy {
/*import net.minecraft.network.packet.c2s.play.ClientStatusC2SPacket;
import net.minecraft.network.packet.c2s.play.CommandSuggestionsC2SPacket;
*///?} else {
import net.minecraft.network.protocol.game.ServerboundClientCommandPacket;
import net.minecraft.network.protocol.game.ServerboundCommandSuggestionPacket;
    //?}


import java.util.function.Function;



public enum PingMethod {
    //? if legacy {
    /*COMMAND_RESPONSE("Command", () -> TextUtils.sendChat("/" + TextUtils.randomUuid(8))),
    TAB_PACKET("Tab", () -> Minecraft.getInstance().getNetworkHandler().sendPacket(new CommandSuggestionsC2SPacket("#"))),
    STATS_PACKET("Stats", () -> Minecraft.getInstance().getNetworkHandler().sendPacket(new ClientStatusC2SPacket(ClientStatusC2SPacket.Status.REQUEST_STATS))),
    HYPIXEL_PING("Hypixel", () -> {
        if (HypixelUtils.isHypixel()) {
            HypixelModAPI.getInstance().sendPacket(new ServerboundPingPacket());
        }
        else {
            throw new PingException("You must be on Hypixel to use this!");
        }
    }),
    SERVER_LIST_PING("Server list", PingMethod::iGuessBro, (info) -> {
        if (Minecraft.getInstance().isSingleplayer()) {
            throw new PingException("This method does not work in singleplayer!");
        }
        long ping = Minecraft.getInstance().getCurrentServerEntry().ping;
        if (ping == 0) {
            throw new PingException("Ping is 0! This might have occured if you used Direct Connect.");
        }
        return ping;
    });
    *///?} else {
    COMMAND_RESPONSE("Command", () -> Minecraft.getInstance().player.connection.sendCommand(TextUtils.randomUuid(8))),
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
