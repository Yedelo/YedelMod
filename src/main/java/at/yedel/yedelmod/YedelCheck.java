package at.yedel.yedelmod;



import java.util.concurrent.TimeUnit;

import at.yedel.yedelmod.config.YedelConfig;
import at.yedel.yedelmod.events.JoinGamePacketEvent;
import at.yedel.yedelmod.utils.Chat;
import at.yedel.yedelmod.utils.Constants.Messages;
import at.yedel.yedelmod.utils.ThreadManager;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;



public class YedelCheck {
    // This class is for YedelUtils to check if this mod is active. This mod would return JavaClass instead of JavaPackage
    // YedelUtils makes this boolean true if it loads
    // alreadyWarned is not supposed to be saved

    // Also used for the first time message
    public static YedelCheck instance = new YedelCheck();
    public static boolean YedelUtils = false;
    public static boolean alreadyWarned = true;

    @SubscribeEvent
    public void onServerChange(JoinGamePacketEvent event) {
        if (YedelUtils && !alreadyWarned) {
            ThreadManager.scheduleOnce(() -> {
                Chat.display(Messages.YedelUtilsMessage);
            }, 3, TimeUnit.SECONDS);
            alreadyWarned = false;
        }
        if (YedelConfig.first) {
            Chat.display(Messages.welcomeMessage);
            YedelConfig.first = false;
            YedelConfig.save();
        }
    }
}
