package at.yedel.yedelmod.features;



import at.yedel.yedelmod.config.YedelConfig;
import dev.deftu.omnicore.client.OmniChat;
import dev.deftu.omnicore.client.OmniClient;
import net.minecraft.world.WorldSettings;
import org.polyfrost.oneconfig.api.event.v1.events.HypixelLocationEvent;
import org.polyfrost.oneconfig.api.event.v1.invoke.impl.Subscribe;

import static at.yedel.yedelmod.launch.YedelModConstants.logo;



public class LimboCreativeCheck {
    private LimboCreativeCheck() {}

    private static final LimboCreativeCheck instance = new LimboCreativeCheck();

    public static LimboCreativeCheck getInstance() {
        return instance;
    }

    @Subscribe
    public void autoLimboCreative(HypixelLocationEvent event) {
        if (event.getLocation().getServerName().isPresent() && event.getLocation().getServerName().get() == "limbo" && YedelConfig.getInstance().limboCreativeMode) {
            giveCreative();
        }
    }

    public int checkLimbo() {
        if (OmniClient.getInstance().playerController.isInCreativeMode()) {
            OmniChat.displayClientMessage(logo + " §cYou are already in creative mode!");
            return 0;
        }
        else {
            giveCreative();
            return 1;
        }
    }

    public void giveCreative() {
        OmniClient.getInstance().playerController.setGameType(WorldSettings.GameType.CREATIVE);
        OmniChat.displayClientMessage(logo + " §eSet gamemode to creative!");
    }
}
