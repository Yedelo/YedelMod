package at.yedel.yedelmod.features;



import at.yedel.yedelmod.api.config.YedelConfig;
import cc.polyfrost.oneconfig.events.event.ChatReceiveEvent;
import cc.polyfrost.oneconfig.libs.eventbus.Subscribe;

import java.util.regex.PatternSyntaxException;



public class RegexChatFilter {
	private static final RegexChatFilter INSTANCE = new RegexChatFilter();

	public static RegexChatFilter getInstance() {
		return INSTANCE;
	}

	private RegexChatFilter() {}

	@Subscribe
	public void filterMessage(ChatReceiveEvent event) {
		if (YedelConfig.getInstance().enabled && YedelConfig.getInstance().regexChatFilter) {
			try {
				if (event.message.getUnformattedText().matches(YedelConfig.getInstance().regexChatFilterPattern)) {
					event.isCancelled = true;
				}
			}
			// This can happen when the user is changing patterns and receiving messages. Don't fill the console
			catch (PatternSyntaxException invalidPatternException) {

			}
		}
	}
}
