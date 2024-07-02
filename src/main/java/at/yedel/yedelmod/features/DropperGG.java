package at.yedel.yedelmod.features;



import java.util.concurrent.TimeUnit;

import at.yedel.yedelmod.config.YedelConfig;
import at.yedel.yedelmod.utils.Chat;
import at.yedel.yedelmod.utils.ThreadManager;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

;



public class DropperGG {
    private static final DropperGG instance = new DropperGG();

    public static DropperGG getInstance() {
        return instance;
    }

    @SubscribeEvent
    public void onChat(ClientChatReceivedEvent event) {
        if (!YedelConfig.getInstance().dropperGG) return;
        String msg = event.message.getUnformattedText();
        if (msg.contains("                                Total Fails: ") || msg.contains("                              You didn't finish!")) {
            ThreadManager.scheduleOnce(() -> Chat.command("ac gg"), YedelConfig.getInstance().dropperGGDelay, TimeUnit.SECONDS);
        }
    }
}
