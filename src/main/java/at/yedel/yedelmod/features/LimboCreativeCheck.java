package at.yedel.yedelmod.features;



import at.yedel.yedelmod.handlers.HypixelManager;
import at.yedel.yedelmod.utils.Chat;
import net.minecraft.world.WorldSettings;

import static at.yedel.yedelmod.YedelMod.minecraft;



public class LimboCreativeCheck {
    private LimboCreativeCheck() {}
    private static final LimboCreativeCheck instance = new LimboCreativeCheck();

    public static LimboCreativeCheck getInstance() {
        return instance;
    }

    private final WorldSettings.GameType creative = WorldSettings.GameType.CREATIVE;

    public void checkLimbo() {
        if (HypixelManager.getInstance().isInLimbo()) {
            if (minecraft.playerController.isInCreativeMode()) Chat.logoDisplay("§cYou are already in creative mode!");
            else giveCreative();
        }
        else Chat.logoDisplay("§cLimbo check failed, try again in a bit or rejoin!");
    }

    public void giveCreative() {
        minecraft.playerController.setGameType(creative);
        Chat.logoDisplay("§eSet gamemode to creative!");
    }
}
