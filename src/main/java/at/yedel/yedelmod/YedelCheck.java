package at.yedel.yedelmod;



import at.yedel.yedelmod.config.YedelConfig;
import cc.polyfrost.oneconfig.events.event.ReceivePacketEvent;
import cc.polyfrost.oneconfig.libs.eventbus.Subscribe;
import cc.polyfrost.oneconfig.libs.universal.UChat;
import cc.polyfrost.oneconfig.utils.Multithreading;
import net.minecraft.network.play.server.S01PacketJoinGame;

import java.util.concurrent.TimeUnit;

import static at.yedel.yedelmod.launch.YedelModConstants.yedelogo;



public class YedelCheck {
    // This class is for YedelUtils to check if this mod is active. This mod would return JavaClass instead of JavaPackage
    // YedelUtils makes this boolean true if it loads
    // alreadyWarned is not supposed to be saved

    private static final YedelCheck INSTANCE = new YedelCheck();

    public static YedelCheck getInstance() {
        return INSTANCE;
    }

    public static boolean YedelUtils = false;
    private boolean alreadyWarned = true;

    // Also used for the first time message
    private YedelCheck() {}

    @Subscribe
    public void checkUponServerChange(ReceivePacketEvent event) {
        if (event.packet instanceof S01PacketJoinGame) {
            if (YedelUtils && !alreadyWarned) {
                Multithreading.schedule(() -> {
                    UChat.chat(yedelogo + " §cYedelUtils detected, it will likely completely break this mod. Do §7/ct delete YedelUtils §cto remove it.");
                }, 3, TimeUnit.SECONDS);
                alreadyWarned = false;
            }
            if (YedelConfig.getInstance().firstTime) {
                Multithreading.schedule(() -> {
                    UChat.chat("§7Welcome to §9§lYedel§7§lMod! Use §9/yedel §7for more information.");
                    YedelConfig.getInstance().firstTime = false;
                    YedelConfig.getInstance().save();
                }, 1, TimeUnit.SECONDS);
            }
        }
    }
}
