package at.yedel.yedelmod.utils.update;



import java.awt.Desktop;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

import at.yedel.yedelmod.YedelMod;
import at.yedel.yedelmod.utils.Chat;
import at.yedel.yedelmod.utils.Requests;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import gg.essential.api.EssentialAPI;
import net.minecraft.util.ChatComponentText;

import static at.yedel.yedelmod.YedelMod.minecraft;
import static at.yedel.yedelmod.utils.Constants.logo;



public class UpdateManager {
	private UpdateManager() {}

	private static final UpdateManager instance = new UpdateManager();

	public static UpdateManager getInstance() {
		return instance;
	}

	private static final String currentVersion = YedelMod.version;
	public static final URL modrinthApiUrl;
	public static final URL githubApiUrl;

	static {
		try {
			modrinthApiUrl = new URL("https://api.modrinth.com/v2/project/yedelmod/version");
			githubApiUrl = new URL("https://api.github.com/repos/yedelo/yedelmod/releases/latest");
		}
		catch (MalformedURLException e) {
			throw new RuntimeException(e);
		}
	}

	public void checkForUpdates(UpdateSource updateSource, FeedbackMethod feedbackMethod) {
		new Thread(() -> threadedCheckForUpdates(updateSource, feedbackMethod), "YedelMod Update Checker").start();
	}

	private void threadedCheckForUpdates(UpdateSource updateSource, FeedbackMethod feedbackMethod) {
		try {
			if (updateSource == UpdateSource.MODRINTH) {
				JsonArray modrinthApiInfo = getModrinthApiInfo();
				String modrinthVersion = getModrinthVersion(modrinthApiInfo);
				if (Objects.equals(modrinthVersion, currentVersion)) {
					notifyUpToDate("Modrinth", feedbackMethod);
					return;
				}
				notifyNewVersion(modrinthVersion, updateSource, feedbackMethod);
			}
			else {
				JsonObject githubApiInfo = getGithubApiInfo();
				String githubVersion = getGithubVersion(githubApiInfo);
				if (Objects.equals(githubVersion, currentVersion)) {
					notifyUpToDate("GitHub", feedbackMethod);
					return;
				}
				notifyNewVersion(githubVersion, updateSource, feedbackMethod);
			}
		}
		catch (IOException e) {
			handleError(updateSource, feedbackMethod);
			e.printStackTrace();
		}
	}

	public JsonArray getModrinthApiInfo() throws IOException {
		// Modrinth uses an array of releases at the top instead of an object at the top so we can't use getJsonObject
		// And it took a while for me to figure this out because I thought everything had to be a JSON object
		return Requests.gson.fromJson(new InputStreamReader(Requests.openURLConnection(modrinthApiUrl).getInputStream(), StandardCharsets.UTF_8), JsonArray.class);
	}

	public String getModrinthVersion(JsonArray modrinthApiInfo) {
		return modrinthApiInfo.
			get(0). // First element in array of releases (latest one)
				getAsJsonObject().
			get("version_number").
			getAsString().
			replace("\"", "");
	}

	public String getModrinthChangelog(JsonArray modrinthApiInfo) {
		String changelog = modrinthApiInfo.get(0).getAsJsonObject().get("changelog").getAsString().replace("'", "");
		int indexOfChangelog = changelog.indexOf("The part below is for the in-game changelog, ignore this on the website!\n");
		if (indexOfChangelog == -1) {
			return "Changelog not found!";
		}
		else return changelog.substring(indexOfChangelog + 73);
	}

	public JsonObject getGithubApiInfo() throws IOException {
		return Requests.getJsonObject(githubApiUrl);
	}

	public String getGithubVersion(JsonObject githubApiInfo) {
		return githubApiInfo.
			getAsJsonObject().
			get("tag_name").
			getAsString().
			replace("\"", "");
	}

	public String getGithubChangelog(JsonObject githubApiInfo) {
		String body = githubApiInfo.get("body").getAsString().replace("'", "");
		int indexOfChangelog = body.indexOf("The part below is for the in-game changelog, ignore this on the website!\r\n");
		if (indexOfChangelog == -1) {
			return "Changelog not found!";
		}
		else return body.substring(indexOfChangelog + 74);
	}

	public void notifyUpToDate(String updateSource, FeedbackMethod feedbackMethod) {
		if (feedbackMethod == FeedbackMethod.CHAT) {
			Chat.logoDisplay("§cYou are up to date with the mod version on " + updateSource + "!");
		}
		else {
			if (minecraft.currentScreen != null) { // if this isn't at launch, for auto check updates
				EssentialAPI.getNotifications().push("YedelMod", "You are up to date with the mod version on " + updateSource + "!");
			}
		}
	}

	private void notifyNewVersion(String newVersion, UpdateSource updateSource, FeedbackMethod feedbackMethod) {
		if (feedbackMethod == FeedbackMethod.CHAT) {
			Chat.display(new ChatComponentText(logo + " §eVersion " + newVersion + " is avaliable on ").appendSibling(updateSource.chatComponent).appendText("§e!"));
		}
		else {
			EssentialAPI.getNotifications().push("YedelMod", "Version " + newVersion + " is avaliable on " + updateSource.name + "§7! Click to open.", () -> {
				try {
					Desktop.getDesktop().browse(updateSource.uri);
				}
				catch (IOException e) {
					EssentialAPI.getNotifications().push("YedelMod", "Couldn't open link for " + updateSource.seriousName + "!");
					e.printStackTrace();
				}
				return null;
			});
		}
	}

	private void handleError(UpdateSource updateSource, FeedbackMethod feedbackMethod) {
		if (feedbackMethod == FeedbackMethod.CHAT) {
			Chat.logoDisplay("§cCouldn't get update information from " + updateSource.seriousName + "!");
		}
		else {
			EssentialAPI.getNotifications().push("YedelMod", "Couldn't get update information from " + updateSource.seriousName + "!");
		}
	}

	public enum FeedbackMethod {
		CHAT,
		NOTIFICATIONS
	}
}
