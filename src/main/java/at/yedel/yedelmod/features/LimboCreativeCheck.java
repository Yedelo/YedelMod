package at.yedel.yedelmod.features;



import dev.deftu.omnicore.client.OmniChat;
import dev.deftu.omnicore.client.OmniClient;
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
            if (OmniClient.getInstance().playerController.isInCreativeMode()) {
                OmniChat.displayClientMessage(logo + " §cYou are already in creative mode!");
            }
            else giveCreative();
        }
        else OmniChat.displayClientMessage(logo + " §cLimbo check failed, try again in a bit or rejoin!");
    }

    public void giveCreative() {
        OmniClient.getInstance().playerController.setGameType(WorldSettings.GameType.CREATIVE);
        OmniChat.displayClientMessage(logo + " §eSet gamemode to creative!");
    }
}
