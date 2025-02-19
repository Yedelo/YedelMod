package at.yedel.yedelmod.features;


import at.yedel.yedelmod.config.YedelConfig;
import cc.polyfrost.oneconfig.libs.universal.UChat;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.regex.Matcher;
import java.util.regex.Pattern;



public class AutoGuildWelcome {
    private AutoGuildWelcome() {}
    private static final AutoGuildWelcome instance = new AutoGuildWelcome();

    public static AutoGuildWelcome getInstance() {
        return instance;
    }

    public final Pattern guildJoinPattern = Pattern.compile("(?<newMember>\\w+) joined the guild!");

    @SubscribeEvent
    public void onGuildJoinMessage(ClientChatReceivedEvent event) {
		if (YedelConfig.getInstance().guildWelcome) {
			String msg = event.message.getUnformattedText();
			Matcher guildJoinMatcher = guildJoinPattern.matcher(msg);
			while (guildJoinMatcher.find()) {
				String newMember = guildJoinMatcher.group("newMember");
				UChat.say("/gc " + YedelConfig.getInstance().guildWelcomeMessage.replace("[player]", newMember));
			}
		}
	}
}
