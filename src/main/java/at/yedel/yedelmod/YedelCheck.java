package at.yedel.yedelmod;



import at.yedel.yedelmod.api.config.YedelConfig;
import cc.polyfrost.oneconfig.events.event.ReceivePacketEvent;
import cc.polyfrost.oneconfig.libs.eventbus.Subscribe;
import cc.polyfrost.oneconfig.libs.universal.UChat;
import cc.polyfrost.oneconfig.utils.Multithreading;
import net.minecraft.network.play.server.S01PacketJoinGame;

import java.util.concurrent.TimeUnit;

import static at.yedel.yedelmod.launch.YedelModConstants.yedelogo;



/**
 * This class is for Yedelutils to check if this mod is loaded.
 * This class would make Java.type("at.yedel.yedelmod.YedelCheck") be a JavaClass or 'function' (why?) instead of a JavaPackage
 * When this is detected, it sets YedelUtils to true to let both to report a message about the incompatibility.
 * alreadyWarned is not supposed to be saved.
 * <p>
 * Also used for the first time message, which may be extracted to YedelMod (the class) later.
 */
public class YedelCheck {

    private static final YedelCheck INSTANCE = new YedelCheck();

    public static YedelCheck getInstance() {
        return INSTANCE;
    }

    public static boolean YedelUtils = false;
    private boolean alreadyWarned = false;

    private YedelCheck() {}

    @Subscribe
    public void checkUponServerChange(ReceivePacketEvent event) {
        if (event.packet instanceof S01PacketJoinGame) {
            if (YedelUtils && !alreadyWarned) {
                Multithreading.schedule(() -> {
                    UChat.chat(yedelogo + " §cYedelUtils detected, it will likely completely break this mod. Do §7/ct delete YedelUtils §cto remove it.");
                }, 3, TimeUnit.SECONDS);
                alreadyWarned = true;
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
