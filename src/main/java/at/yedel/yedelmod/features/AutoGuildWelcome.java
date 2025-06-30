package at.yedel.yedelmod.features;



import at.yedel.yedelmod.api.config.YedelConfig;
import cc.polyfrost.oneconfig.events.event.ChatReceiveEvent;
import cc.polyfrost.oneconfig.libs.eventbus.Subscribe;
import cc.polyfrost.oneconfig.libs.universal.UChat;

import java.util.regex.Matcher;
import java.util.regex.Pattern;



public class AutoGuildWelcome {
	private static final AutoGuildWelcome INSTANCE = new AutoGuildWelcome();

	public static AutoGuildWelcome getInstance() {
		return INSTANCE;
	}

	private static final Pattern GUILD_JOIN_PATTERN = Pattern.compile("(?<newMember>\\w+) joined the guild!");

    private AutoGuildWelcome() {}

	@Subscribe
	public void welcomeNewGuildMember(ChatReceiveEvent event) {
		if (YedelConfig.getInstance().enabled && YedelConfig.getInstance().autoWelcomeGuildMembers) {
			String msg = event.message.getUnformattedText();
			Matcher guildJoinMatcher = GUILD_JOIN_PATTERN.matcher(msg);
			while (guildJoinMatcher.find()) {
				String newMember = guildJoinMatcher.group("newMember");
				UChat.say("/gc " + YedelConfig.getInstance().guildWelcomeMessage.replace("[player]", newMember));
			}
		}
	}
}
