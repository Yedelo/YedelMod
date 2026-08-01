package at.yedel.yedelmod.features;



import at.yedel.yedelmod.config.YedelConfig;

import net.minecraft.client.Minecraft;
import org.polyfrost.oneconfig.api.event.v1.events.ChatEvent;
import org.polyfrost.oneconfig.api.event.v1.invoke.impl.Subscribe;
import org.polyfrost.oneconfig.utils.v1.Multithreading;

import java.util.concurrent.TimeUnit;



public class DropperGG {
    private static final DropperGG INSTANCE = new DropperGG();

	public static DropperGG getInstance() {
		return INSTANCE;
	}

    private DropperGG() {}

    @Subscribe
    public void triggerDropperGG(ChatEvent.Receive event) {
        if (YedelConfig.getInstance().enabled && YedelConfig.getInstance().dropperAutoGG) {
	        String msg = event.getFullyUnformattedMessage();
			if (msg.contains("                                Total Fails: ") || msg.contains("                              You didn't finish!")) {
				Multithreading.schedule(() -> {
					Minecraft.getInstance().player.connection.sendChat("/ac gg");
				}, YedelConfig.getInstance().autoGGDelay, TimeUnit.SECONDS);
			}
		}
	}
}
