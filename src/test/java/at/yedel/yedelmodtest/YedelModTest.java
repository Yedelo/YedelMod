package at.yedel.yedelmodtest;



import java.io.IOException;

import at.yedel.yedelmod.utils.update.UpdateManager;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;



public class YedelModTest {
	@Test
	public void testUpdateManager() {
		try {
			JsonArray modrinthApiInfo = UpdateManager.getInstance().getModrinthApiInfo();
			String modrinthVersion = UpdateManager.getInstance().getModrinthVersion(modrinthApiInfo);
			System.out.println("Modrinth version: " + modrinthVersion);
		}
		catch (IOException e) {
			Assertions.fail(e);
		}
		try {
			JsonObject githubApiInfo = UpdateManager.getInstance().getGithubApiInfo();
			String githubVersion = UpdateManager.getInstance().getGithubVersion(githubApiInfo);
			System.out.println("GitHub version: " + githubVersion);
		}
		catch (IOException e) {
			Assertions.fail(e);
		}
	}
}
