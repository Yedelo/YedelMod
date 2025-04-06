package at.yedel.yedelmod.features;



import at.yedel.yedelmod.config.YedelConfig;
import cc.polyfrost.oneconfig.events.event.ChatReceiveEvent;
import cc.polyfrost.oneconfig.libs.eventbus.Subscribe;
import cc.polyfrost.oneconfig.libs.universal.UChat;

import java.util.regex.Matcher;
import java.util.regex.Pattern;



public class AutoGuildWelcome {
    private AutoGuildWelcome() {}

    private static final AutoGuildWelcome instance = new AutoGuildWelcome();

    public static AutoGuildWelcome getInstance() {
        return instance;
    }

    public final Pattern guildJoinPattern = Pattern.compile("(?<newMember>\\w+) joined the guild!");

	@Subscribe
	public void welcomeNewGuildMember(ChatReceiveEvent event) {
		if (YedelConfig.getInstance().autoWelcomeGuildMembers) {
			String msg = event.message.getUnformattedText();
			Matcher guildJoinMatcher = guildJoinPattern.matcher(msg);
			while (guildJoinMatcher.find()) {
				String newMember = guildJoinMatcher.group("newMember");
				UChat.say("/gc " + YedelConfig.getInstance().guildWelcomeMessage.replace("[player]", newMember));
			}
		}
	}
}
