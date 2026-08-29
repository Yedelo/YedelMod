package at.yedel.yedelmod.features;



import at.yedel.yedelmod.config.YedelConfig;
//? if v0 {
//import cc.polyfrost.oneconfig.libs.universal.UChat;
//?} else
import org.polyfrost.oneconfig.api.platform.v1.Platform;
import net.hypixel.modapi.HypixelModAPI;
import net.hypixel.modapi.packet.impl.clientbound.event.ClientboundLocationPacket;
import net.minecraft.client.Minecraft;
//? if legacy {
//import net.minecraft.world.WorldSettings;
//?} else
import net.minecraft.world.level.GameType;


import java.util.Objects;

import static at.yedel.yedelmod.launch.YedelModConstants.yedelogo;



public class LimboCreative {
    private static final LimboCreative INSTANCE = new LimboCreative();

    public static LimboCreative getInstance() {
        return INSTANCE;
    }

    private boolean inLimboServer;

    // like it could be different for alpha and it actually is but alpha is closed now so i can't test it
    private boolean isInLimbo() {
        return inLimboServer;
    }

    private LimboCreative() {
        HypixelModAPI.getInstance().registerHandler(ClientboundLocationPacket.class, this::handleLocationPacket);
    }

    private void handleLocationPacket(ClientboundLocationPacket packet) {
        inLimboServer = Objects.equals(packet.getServerName(), "limbo");
        if (YedelConfig.getInstance().enabled && YedelConfig.getInstance().limboCreativeMode && inLimboServer) {
            giveCreative();
        }
    }

    public void awardLimboCreative() {
        if (isInLimbo()) {
            if (isAlreadyInCreative()) {
                Platform.compatibility().displayChatMessage(yedelogo + " §cYou are already in creative mode!");
            }
            else {
                giveCreative();
            }
        }
        else {
            Platform.compatibility().displayChatMessage(yedelogo + " §cLimbo check failed, try again in a bit or rejoin!");
        }
    }

    private void giveCreative() {
        //? if v0 {
        //Minecraft.getMinecraft().playerController.setGameType(WorldSettings.GameType.CREATIVE);
        //?} else
        Minecraft.getInstance().gameMode.setLocalMode(GameType.CREATIVE);
        Platform.compatibility().displayChatMessage(yedelogo + " §eSet gamemode to creative!");
    }

    private boolean isAlreadyInCreative() {
        //? if legacy {
        //return Minecraft.getMinecraft().playerController.isInCreativeMode();
        //?} else
        return Minecraft.getInstance().player.isCreative();
    }
}
