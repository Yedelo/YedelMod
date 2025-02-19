package at.yedel.yedelmod;



import java.util.concurrent.TimeUnit;

import at.yedel.yedelmod.config.YedelConfig;
import at.yedel.yedelmod.events.JoinGamePacketEvent;
import at.yedel.yedelmod.utils.Chat;
import at.yedel.yedelmod.utils.ThreadManager;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;



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

    @SubscribeEvent
    public void onServerChange(JoinGamePacketEvent event) {
        if (YedelUtils && !alreadyWarned) {
            ThreadManager.scheduleOnce(() -> {
                Chat.logoDisplay("§cYedelUtils detected, it will likely completely break this mod. Do §7/ct delete YedelUtils §cto remove it.");
            }, 3, TimeUnit.SECONDS);
            alreadyWarned = false;
        }
        if (YedelConfig.getInstance().first) {
            ThreadManager.scheduleOnce(() -> {
                Chat.display("§7Welcome to §9§lYedel§7§lMod! Use §9/yedel §7for more information.");
                YedelConfig.getInstance().first = false;
                YedelConfig.getInstance().save();
            }, 1000, TimeUnit.MILLISECONDS);
        }
    }
}
