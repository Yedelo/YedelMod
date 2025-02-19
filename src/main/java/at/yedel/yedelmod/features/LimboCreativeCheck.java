package at.yedel.yedelmod.features;


import at.yedel.yedelmod.handlers.HypixelManager;
import cc.polyfrost.oneconfig.libs.universal.UChat;
import cc.polyfrost.oneconfig.libs.universal.UMinecraft;
import net.minecraft.world.WorldSettings;

import static at.yedel.yedelmod.launch.YedelModConstants.logo;


public class LimboCreativeCheck {
    private LimboCreativeCheck() {}
    private static final LimboCreativeCheck instance = new LimboCreativeCheck();

    public static LimboCreativeCheck getInstance() {
        return instance;
    }

    public void checkLimbo() {
        if (HypixelManager.getInstance().isInLimbo()) {
            if (UMinecraft.getMinecraft().playerController.isInCreativeMode())
                UChat.chat(logo + " §cYou are already in creative mode!");
            else giveCreative();
        } else UChat.chat(logo + " §cLimbo check failed, try again in a bit or rejoin!");
    }

    public void giveCreative() {
        UMinecraft.getMinecraft().playerController.setGameType(WorldSettings.GameType.CREATIVE);
        UChat.chat(logo + " §eSet gamemode to creative!");
    }
}
