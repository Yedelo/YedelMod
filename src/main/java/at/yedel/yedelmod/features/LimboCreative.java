package at.yedel.yedelmod.features;



import at.yedel.yedelmod.config.YedelConfig;
import cc.polyfrost.oneconfig.libs.universal.UChat;
import cc.polyfrost.oneconfig.libs.universal.UMinecraft;
import net.hypixel.modapi.HypixelModAPI;
import net.hypixel.modapi.packet.impl.clientbound.event.ClientboundLocationPacket;
import net.minecraft.world.WorldSettings;

import java.util.Objects;

import static at.yedel.yedelmod.launch.YedelModConstants.logo;



public class LimboCreative {
    private static final LimboCreative instance = new LimboCreative();

    public static LimboCreative getInstance() {
        return instance;
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
                UChat.chat(logo + " §cYou are already in creative mode!");
            }
            else giveCreative();
        }
        else UChat.chat(logo + " §cLimbo check failed, try again in a bit or rejoin!");
    }

    public void giveCreative() {
        UMinecraft.getMinecraft().playerController.setGameType(WorldSettings.GameType.CREATIVE);
        UChat.chat(logo + " §eSet gamemode to creative!");
    }
}
