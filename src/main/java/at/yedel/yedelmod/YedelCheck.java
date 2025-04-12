package at.yedel.yedelmod;



import at.yedel.yedelmod.config.YedelConfig;
import dev.deftu.omnicore.client.OmniChat;
import net.minecraft.network.play.server.S01PacketJoinGame;
import org.polyfrost.oneconfig.api.event.v1.events.PacketEvent;
import org.polyfrost.oneconfig.api.event.v1.invoke.impl.Subscribe;
import org.polyfrost.oneconfig.utils.v1.Multithreading;

import java.util.concurrent.TimeUnit;

import static at.yedel.yedelmod.launch.YedelModConstants.logo;



public class YedelCheck {
    // This class is for YedelUtils to check if this mod is active. This mod would return JavaClass instead of JavaPackage
    // YedelUtils makes this boolean true if it loads
    // alreadyWarned is not supposed to be saved

    // Also used for the first time message
    private YedelCheck() {}

    private static final YedelCheck instance = new YedelCheck();

    public static YedelCheck getInstance() {
        return instance;
    }

    public static boolean YedelUtils = false;
    private boolean alreadyWarned = true;

    @Subscribe
    public void checkUponServerChange(PacketEvent.Receive event) {
        if (event.getPacket() instanceof S01PacketJoinGame) {
            if (YedelUtils && !alreadyWarned) {
                Multithreading.schedule(() -> {
                    OmniChat.displayClientMessage(logo + " §cYedelUtils detected, it will likely completely break this mod. Do §7/ct delete YedelUtils §cto remove it.");
                    }, 3, TimeUnit.SECONDS
                );
                alreadyWarned = false;
            }
            if (YedelConfig.getInstance().firstTime) {
                Multithreading.schedule(() -> {
                    OmniChat.displayClientMessage("§7Welcome to §9§lYedel§7§lMod! Use §9/yedel §7for more information.");
                        YedelConfig.getInstance().firstTime = false;
                        YedelConfig.getInstance().save();
                    }, 1, TimeUnit.SECONDS
                );
            }
        }
    }
}
