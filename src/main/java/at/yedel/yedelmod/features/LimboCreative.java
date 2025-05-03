package at.yedel.yedelmod.features;



import at.yedel.yedelmod.config.YedelConfig;
import cc.polyfrost.oneconfig.libs.universal.UChat;
import cc.polyfrost.oneconfig.libs.universal.UMinecraft;
import net.hypixel.modapi.HypixelModAPI;
import net.hypixel.modapi.packet.impl.clientbound.event.ClientboundLocationPacket;
import net.minecraft.world.WorldSettings;

import java.util.Objects;

import static at.yedel.yedelmod.launch.YedelModConstants.LOGO;



public class LimboCreative {
    private static final LimboCreative INSTANCE = new LimboCreative();

    public static LimboCreative getInstance() {
        return INSTANCE;
    }

    private LimboCreative() {
        HypixelModAPI.getInstance().registerHandler(ClientboundLocationPacket.class, this::handleLocationPacket);
    }

    private boolean inLimbo;

    private void handleLocationPacket(ClientboundLocationPacket packet) {
        inLimbo = Objects.equals(packet.getServerName(), "limbo");
        if (YedelConfig.getInstance().limboCreativeMode && inLimbo) {
            giveCreative();
        }
    }

    public void checkLimbo() {
        if (inLimbo) {
            if (UMinecraft.getMinecraft().playerController.isInCreativeMode()) {
                UChat.chat(LOGO + " §cYou are already in creative mode!");
            }
            else giveCreative();
        }
        else UChat.chat(LOGO + " §cLimbo check failed, try again in a bit or rejoin!");
    }

    public void giveCreative() {
        UMinecraft.getMinecraft().playerController.setGameType(WorldSettings.GameType.CREATIVE);
        UChat.chat(LOGO + " §eSet gamemode to creative!");
    }
}
