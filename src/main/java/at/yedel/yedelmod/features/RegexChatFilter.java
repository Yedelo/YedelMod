package at.yedel.yedelmod.features;



import at.yedel.yedelmod.config.YedelConfig;
import org.polyfrost.oneconfig.api.event.v1.events.ChatEvent;
import org.polyfrost.oneconfig.api.event.v1.invoke.impl.Subscribe;

import java.util.regex.PatternSyntaxException;



public class RegexChatFilter {
	private RegexChatFilter() {}

	private static final RegexChatFilter instance = new RegexChatFilter();

	public static RegexChatFilter getInstance() {
		return instance;
	}

	@Subscribe
    public void filterMessage(ChatEvent.Receive event) {
		if (YedelConfig.getInstance().regexChatFilter) {
			try {
                if (event.getFullyUnformattedMessage().matches(YedelConfig.getInstance().regexChatFilterPattern)) {
                    event.cancelled = true;
				}
			}
            // This can happen when the user is changing patterns and receiving messages. Don't fill logs
            catch (PatternSyntaxException invalidPatternException) {}
		}
	}
}
