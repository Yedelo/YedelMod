package at.yedel.yedelmod.features;


import at.yedel.yedelmod.config.YedelConfig;
import cc.polyfrost.oneconfig.libs.universal.UChat;
import cc.polyfrost.oneconfig.utils.Multithreading;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.concurrent.TimeUnit;



public class DropperGG {
    private DropperGG() {}
    private static final DropperGG instance = new DropperGG();

    public static DropperGG getInstance() {
        return instance;
    }

    @SubscribeEvent
    public void onChat(ClientChatReceivedEvent event) {
		if (YedelConfig.getInstance().dropperGG) {
			String msg = event.message.getUnformattedText();
			if (msg.contains("                                Total Fails: ") || msg.contains("                              You didn't finish!")) {
                Multithreading.schedule(() -> UChat.say("/ac gg"), YedelConfig.getInstance().dropperGGDelay, TimeUnit.SECONDS);
			}
		}
	}
}
