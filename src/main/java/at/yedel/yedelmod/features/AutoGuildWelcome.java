package at.yedel.yedelmod.features;



import at.yedel.yedelmod.config.YedelConfig;
import dev.deftu.omnicore.client.OmniChat;
import org.polyfrost.oneconfig.api.event.v1.events.ChatEvent;
import org.polyfrost.oneconfig.api.event.v1.invoke.impl.Subscribe;

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
    public void welcomeNewGuildMember(ChatEvent.Receive event) {
		if (YedelConfig.getInstance().autoWelcomeGuildMembers) {
            String msg = event.getFullyUnformattedMessage();
			Matcher guildJoinMatcher = guildJoinPattern.matcher(msg);
			while (guildJoinMatcher.find()) {
				String newMember = guildJoinMatcher.group("newMember");
                OmniChat.sendPlayerMessage("/gc " + YedelConfig.getInstance().guildWelcomeMessage.replace("[player]", newMember));
			}
		}
	}
}
