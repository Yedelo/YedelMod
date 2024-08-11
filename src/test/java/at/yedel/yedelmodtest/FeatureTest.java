package at.yedel.yedelmodtest;



import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;



public class FeatureTest {
	@Test
	public void testGuildJoinPattern() {
		Pattern guildJoinPattern = Pattern.compile("(?<newMember>\\w+) joined the guild!");

		String msg = "nonrankedperson joined the guild!";
		Matcher guildJoinMatcher = guildJoinPattern.matcher(msg);
		while (guildJoinMatcher.find()) {
			String newMember = guildJoinMatcher.group("newMember");
			Assertions.assertEquals(newMember, "nonrankedperson");
		}

		String msg2 = "[MVP+] rankedperson joined the guild!";
		Matcher guildJoinMatcher2 = guildJoinPattern.matcher(msg2);
		while (guildJoinMatcher.find()) {
			String newMember = guildJoinMatcher2.group("newMember");
			Assertions.assertEquals(newMember, "rankedperson");
		}
	}

	@Test
	public void testTokenMessagePattern() {
		String tokenMessagePattern = "\\+[0-9]+ tokens! .*";
		Assertions.assertTrue("+20 tokens! (Final Kill)".matches(tokenMessagePattern));
		Assertions.assertTrue("+9 tokens! (End of the world)".matches(tokenMessagePattern));
	}

	@Test
	public void testSlumberTicketPattern() {
		String slumberTicketPattern = "\\+[0-9]+ Slumber Tickets! .*";
		Assertions.assertTrue("+50 Slumber Tickets! (Win)".matches(slumberTicketPattern));
		Assertions.assertTrue("+19 Slumber Tickets! (Final Kill)".matches(slumberTicketPattern));
	}
}
