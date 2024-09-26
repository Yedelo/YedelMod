package at.yedel.yedelmod.features;



import at.yedel.yedelmod.handlers.HypixelManager;
import at.yedel.yedelmod.utils.Chat;
import at.yedel.yedelmod.utils.Constants.Messages;
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
        if (HypixelManager.getInstance().getInLimbo()) {
            if (minecraft.playerController.isInCreativeMode()) Chat.display(Messages.alreadyCreative);
            else giveCreative();
        }
        else Chat.display(Messages.limboCheckFailed);
    }

    public void giveCreative() {
        minecraft.playerController.setGameType(creative);
        Chat.display(Messages.gamemodeCreative);
    }
}
