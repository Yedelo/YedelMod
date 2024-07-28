package at.yedel.yedelmod.utils.update;



import java.awt.Desktop;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Objects;

import at.yedel.yedelmod.YedelMod;
import at.yedel.yedelmod.utils.Chat;
import at.yedel.yedelmod.utils.Constants;
import at.yedel.yedelmod.utils.Requests;
import com.google.gson.JsonArray;
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

	public void checkForUpdates(UpdateSource updateSource, FeedbackMethod feedbackMethod) {
		new Thread(() -> threadedCheckForUpdates(updateSource, feedbackMethod), "YedelMod Update Checker").start();
	}

	private void threadedCheckForUpdates(UpdateSource updateSource, FeedbackMethod feedbackMethod) {
		try {
			if (updateSource == UpdateSource.MODRINTH) {
				String modrinthVersion = getModrinthVersion();
				if (Objects.equals(modrinthVersion, currentVersion)) {
					notifyUpToDate("Modrinth", feedbackMethod);
					return;
				}
				notifyNewVersion(modrinthVersion, updateSource, feedbackMethod);
			}
			else {
				String githubVersion = getGithubVersion();
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

	private String getModrinthVersion() throws IOException {
		// Modrinth uses an array of releases at the top instead of an object at the top so we can't use getJsonObject
		// And it took a while for me to figure this out because I thought everything had to be a JSON object
		return Requests.gson.fromJson(new InputStreamReader(Requests.openURLConnection(Constants.modrinthApiUrl).getInputStream()), JsonArray.class).
			get(0). // First element in array of releases (latest one)
				getAsJsonObject().
			get("version_number").
			getAsString().
			replace("\"", "");
	}

	private String getGithubVersion() throws IOException {
		return Requests.getJsonObject(Constants.githubApiUrl).
			getAsJsonObject().
			get("tag_name").
			getAsString().
			replace("\"", "");
	}

	private void notifyUpToDate(String updateSource, FeedbackMethod feedbackMethod) {
		if (feedbackMethod == FeedbackMethod.CHAT) {
			Chat.logoDisplay("§cYou are up to date with the mod version on " + updateSource + "!");
		}
		else {
			if (minecraft.currentScreen != null) { // if this isn't at launch, for auto check updates
				Constants.notifications.push("YedelMod", "You are up to date with the mod version on " + updateSource + "!");
			}
		}
	}

	private void notifyNewVersion(String newVersion, UpdateSource updateSource, FeedbackMethod feedbackMethod) {
		if (feedbackMethod == FeedbackMethod.CHAT) {
			Chat.display(new ChatComponentText(logo + " §eVersion " + newVersion + " is avaliable on ").appendSibling(updateSource.chatComponent).appendText("§e!"));
		}
		else {
			Constants.notifications.push("YedelMod", "Version " + newVersion + " is avaliable on " + updateSource.name + "§7! Click to open.", () -> {
				try {
					Desktop.getDesktop().browse(updateSource.uri);
				}
				catch (IOException e) {
					Constants.notifications.push("YedelMod", "Couldn't open link for " + updateSource.seriousName + "!");
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
			Constants.notifications.push("YedelMod", "Couldn't get update information from " + updateSource.seriousName + "!");
		}
	}

	public enum FeedbackMethod {
		CHAT,
		NOTIFICATIONS
	}
}
