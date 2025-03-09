package at.yedel.yedelmod.features;



import at.yedel.yedelmod.config.YedelConfig;
import cc.polyfrost.oneconfig.events.event.ChatReceiveEvent;
import cc.polyfrost.oneconfig.libs.eventbus.Subscribe;

import java.util.regex.PatternSyntaxException;



public class RegexChatFilter {
	private RegexChatFilter() {}

	private static final RegexChatFilter instance = new RegexChatFilter();

	public static RegexChatFilter getInstance() {
		return instance;
	}

	@Subscribe
	public void filterMessage(ChatReceiveEvent event) {
		if (YedelConfig.getInstance().regexChatFilter) {
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
