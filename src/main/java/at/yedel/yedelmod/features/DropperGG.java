package at.yedel.yedelmod.features;



import java.util.concurrent.TimeUnit;

import at.yedel.yedelmod.config.YedelConfig;
import at.yedel.yedelmod.utils.Chat;
import at.yedel.yedelmod.utils.ThreadManager;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

;



public class DropperGG {
    public static DropperGG instance = new DropperGG();
    @SubscribeEvent
    public void onChat(ClientChatReceivedEvent event) {
        if (!YedelConfig.dropperGG) return;
        String msg = event.message.getUnformattedText();
        if (msg.contains("                                Total Fails: ") || msg.contains("                              You didn't finish!")) {
            ThreadManager.scheduleOnce(() -> Chat.command("ac gg"), YedelConfig.dropperGGDelay, TimeUnit.SECONDS);
        }
    }
}
