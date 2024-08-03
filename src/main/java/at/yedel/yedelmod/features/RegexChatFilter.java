package at.yedel.yedelmod.features;



import java.util.regex.PatternSyntaxException;

import at.yedel.yedelmod.config.YedelConfig;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;



public class RegexChatFilter {
	private RegexChatFilter() {}

	private static RegexChatFilter instance = new RegexChatFilter();

	public static RegexChatFilter getInstance() {
		return instance;
	}

	@SubscribeEvent
	public void onFilteredMessage(ClientChatReceivedEvent event) {
		if (YedelConfig.getInstance().regexChatFilterToggled) {
			try {
				if (event.message.getUnformattedText().matches(YedelConfig.getInstance().regexChatFilterPattern)) {
					event.setCanceled(true);
				}
			}
			// This can happen when the user is changing patterns and receiving messages. Don't fill the console
			catch (PatternSyntaxException invalidPatternException) {

			}
		}
	}
}
