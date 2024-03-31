package at.yedel.yedelmod;



import java.util.concurrent.TimeUnit;

import at.yedel.yedelmod.config.YedelConfig;
import gg.essential.api.utils.Multithreading;
import gg.essential.universal.UChat;
import gg.essential.universal.wrappers.message.UMessage;
import net.minecraft.event.ClickEvent;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatStyle;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import static at.yedel.yedelmod.YedelMod.logo;



public class YedelCheck {
    // This class is for YedelUtils to check if this mod is active. This mod would return JavaClass instead of JavaPackage
    // YedelUtils makes this boolean true if it loads
    // alreadyWarned is not supposed to be saved

    // Also used for the first time message
    public static boolean YedelUtils = false;
    public static boolean alreadyWarned = true;

    private final ChatStyle uninstallYedelUtils = new ChatStyle().setChatClickEvent(
            new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/ct delete YedelUtils")
    );
    private final UMessage warnMessage = new UMessage(
            logo,
            " &9&lYedel&7&lUtils &cdetected, it's likely that it will completely break this mod.",
            " If you are not seeing a similar message from &9&lYedel&7&lUtils, &cignore this message. ",
            new ChatComponentText("&c&n&lUninstall YedelUtils").setChatStyle(uninstallYedelUtils)
    );

    @SubscribeEvent
    public void onWorldLoad(WorldEvent.Load event) {
        if (YedelUtils && !alreadyWarned) {
            Multithreading.schedule(() -> {
                warnMessage.chat();
            }, 3, TimeUnit.SECONDS);
            alreadyWarned = false;
        }
        if (YedelConfig.first) {
            UChat.chat(logo + " &7Welcome to &9&lYedel&7&lMod! Use &9/yedel &7for more information.");
            YedelConfig.first = false;
            YedelConfig.save();
        }
    }
}
