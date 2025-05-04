package at.yedel.yedelmod.features;



import at.yedel.yedelmod.config.YedelConfig;
import cc.polyfrost.oneconfig.events.event.ChatReceiveEvent;
import cc.polyfrost.oneconfig.libs.eventbus.Subscribe;
import cc.polyfrost.oneconfig.libs.universal.UChat;
import cc.polyfrost.oneconfig.utils.Multithreading;

import java.util.concurrent.TimeUnit;



public class DropperGG {
    private static final DropperGG INSTANCE = new DropperGG();

    private DropperGG() {}

    public static DropperGG getInstance() {
        return INSTANCE;
    }

    @Subscribe
    public void triggerDropperGG(ChatReceiveEvent event) {
        if (YedelConfig.getInstance().dropperAutoGG) {
			String msg = event.message.getUnformattedText();
			if (msg.contains("                                Total Fails: ") || msg.contains("                              You didn't finish!")) {
                Multithreading.schedule(() -> UChat.say("/ac gg"), YedelConfig.getInstance().autoGGDelay, TimeUnit.SECONDS);
			}
		}
	}
}
