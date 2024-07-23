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

		String msg2 = "rankedperson joined the guild!";
		Matcher guildJoinMatcher2 = guildJoinPattern.matcher(msg2);
		while (guildJoinMatcher.find()) {
			String newMember = guildJoinMatcher2.group("newMember");
			Assertions.assertEquals(newMember, "rankedperson");
		}
	}
}
