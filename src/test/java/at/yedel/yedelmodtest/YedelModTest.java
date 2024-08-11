package at.yedel.yedelmodtest;



import java.io.IOException;

import at.yedel.yedelmod.utils.update.UpdateManager;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;



public class YedelModTest {
	private static final Logger logger = LogManager.getLogger("YedelMod Test");

	@Test
	public void testUpdateManager() {
		try {
			JsonArray modrinthApiInfo = UpdateManager.getInstance().getModrinthApiInfo();
			String modrinthVersion = UpdateManager.getInstance().getModrinthVersion(modrinthApiInfo);
			logger.info("Modrinth version: {}", modrinthVersion);
		}
		catch (IOException e) {
			Assertions.fail(e);
		}
		try {
			JsonObject githubApiInfo = UpdateManager.getInstance().getGithubApiInfo();
			String githubVersion = UpdateManager.getInstance().getGithubVersion(githubApiInfo);
			logger.info("GitHub version: {}", githubVersion);
		}
		catch (IOException e) {
			Assertions.fail(e);
		}
	}
}
