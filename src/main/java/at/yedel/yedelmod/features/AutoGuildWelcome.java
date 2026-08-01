package at.yedel.yedelmod.features;



import at.yedel.yedelmod.config.YedelConfig;

import net.minecraft.client.Minecraft;
import org.polyfrost.oneconfig.api.event.v1.events.ChatEvent;
import org.polyfrost.oneconfig.api.event.v1.invoke.impl.Subscribe;

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
	public void welcomeNewGuildMember(ChatEvent.Receive event) {
		if (YedelConfig.getInstance().enabled && YedelConfig.getInstance().autoWelcomeGuildMembers) {
			String msg = event.getFullyUnformattedMessage();
			Matcher guildJoinMatcher = GUILD_JOIN_PATTERN.matcher(msg);
			while (guildJoinMatcher.find()) {
				String newMember = guildJoinMatcher.group("newMember");
				Minecraft.getInstance().player.connection.sendChat("/gc " + YedelConfig.getInstance().guildWelcomeMessage.replace("[player]", newMember));
			}
		}
	}
}
