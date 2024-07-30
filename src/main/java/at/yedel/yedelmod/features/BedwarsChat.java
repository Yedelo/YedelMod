package at.yedel.yedelmod.features;



import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import at.yedel.yedelmod.config.YedelConfig;
import at.yedel.yedelmod.handlers.HypixelManager;
import net.minecraft.util.ChatComponentText;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;



public class BedwarsChat {
	private BedwarsChat() {}

	private static final BedwarsChat instance = new BedwarsChat();

	public static BedwarsChat getInstance() {
		return instance;
	}

	private static final Pattern tokenMessagePattern = Pattern.compile("\\+[0-9]+ tokens! .*");

	@SubscribeEvent
	public void onTokenMessage(ClientChatReceivedEvent event) {
		if (!YedelConfig.getInstance().lightGreenTokenMessages || !HypixelManager.getInstance().getInBedwars()) return;
		String message = event.message.getUnformattedText();
		Matcher matcher = tokenMessagePattern.matcher(message);
		while (matcher.find()) {
			event.message = new ChatComponentText(event.message.getFormattedText().replace("§2", "§a"));
		}
	}

	@SubscribeEvent
	public void onDreamersSoulFragmentMessage(ClientChatReceivedEvent event) {
		if (YedelConfig.getInstance().hideDreamerSoulFragmentMessages && Objects.equals(event.message.getUnformattedText(), "+1 Dreamer's Soul Fragment!")) {
			event.setCanceled(true);
		}
	}
}
