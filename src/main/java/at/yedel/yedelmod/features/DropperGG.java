package at.yedel.yedelmod.features;



import java.util.concurrent.TimeUnit;

import at.yedel.yedelmod.config.YedelConfig;
import gg.essential.api.utils.Multithreading;
import gg.essential.universal.UChat;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;



public class DropperGG {
    @SubscribeEvent
    public void onChat(ClientChatReceivedEvent event) {
        if (!YedelConfig.dropperGG) return;
        String msg = event.message.getUnformattedText();
        if (msg.contains("                                Total Fails: ") || msg.contains("                              You didn't finish!")) {
            Multithreading.schedule(() -> UChat.say("/ac gg"), YedelConfig.dropperGGDelay, TimeUnit.SECONDS);
        }
    }
}
