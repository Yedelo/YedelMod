package at.yedel.yedelmod.features;



import at.yedel.yedelmod.config.YedelConfig;
import cc.polyfrost.oneconfig.libs.universal.UChat;
import cc.polyfrost.oneconfig.libs.universal.UMinecraft;
import net.hypixel.modapi.HypixelModAPI;
import net.hypixel.modapi.packet.impl.clientbound.event.ClientboundLocationPacket;
import net.minecraft.world.WorldSettings;

import java.util.Objects;

import static at.yedel.yedelmod.launch.YedelModConstants.yedelogo;



public class LimboCreative {
    private static final LimboCreative INSTANCE = new LimboCreative();

    public static LimboCreative getInstance() {
        return INSTANCE;
    }

    private boolean inLimbo;

    private LimboCreative() {
        HypixelModAPI.getInstance().registerHandler(ClientboundLocationPacket.class, this::handleLocationPacket);
    }

    private void handleLocationPacket(ClientboundLocationPacket packet) {
        inLimbo = Objects.equals(packet.getServerName(), "limbo");
        if (YedelConfig.getInstance().limboCreativeMode && inLimbo) {
            giveCreative();
        }
    }

    public void checkLimbo() {
        if (inLimbo) {
            if (UMinecraft.getMinecraft().playerController.isInCreativeMode()) {
                UChat.chat(yedelogo + " §cYou are already in creative mode!");
            }
            else {
                giveCreative();
            }
        }
        else {
            UChat.chat(yedelogo + " §cLimbo check failed, try again in a bit or rejoin!");
        }
    }

    public void giveCreative() {
        UMinecraft.getMinecraft().playerController.setGameType(WorldSettings.GameType.CREATIVE);
        UChat.chat(yedelogo + " §eSet gamemode to creative!");
    }
}
