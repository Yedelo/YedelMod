package at.yedel.yedelmod.features;



import at.yedel.yedelmod.config.YedelConfig;
import net.hypixel.modapi.HypixelModAPI;
import net.hypixel.modapi.packet.impl.clientbound.event.ClientboundLocationPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.world.level.GameType;
import org.polyfrost.oneconfig.api.platform.v1.Platform;

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
            if (Minecraft.getInstance().player.isCreative()) {
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
        Minecraft.getInstance().gameMode.setLocalMode(GameType.CREATIVE);
        Platform.compatibility().displayChatMessage(yedelogo + " §eSet gamemode to creative!");
    }
}
