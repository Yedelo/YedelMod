package at.yedel.yedelmod.utils.update;



import at.yedel.yedelmod.utils.Requests;
import cc.polyfrost.oneconfig.libs.universal.UChat;
import cc.polyfrost.oneconfig.libs.universal.UDesktop;
import cc.polyfrost.oneconfig.libs.universal.UScreen;
import cc.polyfrost.oneconfig.libs.universal.wrappers.message.UTextComponent;
import cc.polyfrost.oneconfig.utils.Notifications;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.event.ClickEvent;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Objects;



public class UpdateManager {
	private final String modName;
	private final String modVersion;
	private final String modrinthId;
	private final String githubId;
	private final URI modrinthLink;
	private final URI githubLink;
	private final String logo;

	public UpdateManager(String modName, String modVersion, String modrinthId, String githubId, String logo) {
		this.modName = modName;
		this.modVersion = modVersion;
		this.modrinthId = modrinthId;
		this.githubId = githubId;
		this.modrinthLink = URI.create("https://modrinth.com/mod/" + modrinthId + "/versions");
		this.githubLink = URI.create("https://github.com/" + githubId + "/releases");
		this.logo = logo;
	}

	public void checkForUpdates(UpdateSource updateSource, FeedbackMethod feedbackMethod) {
		new Thread(() -> checkForUpdatesBlocking(updateSource, feedbackMethod), modName + " Update Checker").start();
	}

	public void checkForUpdatesBlocking(UpdateSource updateSource, FeedbackMethod feedbackMethod) {
		try {
			if (updateSource == UpdateSource.MODRINTH) {
				JsonArray modrinthApiInfo = getModrinthApiInfo();
				String modrinthVersion = getModrinthVersion(modrinthApiInfo);
				if (Objects.equals(modrinthVersion, modVersion)) {
					notifyUpToDate("Modrinth", feedbackMethod);
					return;
				}
				notifyNewVersion(modrinthVersion, updateSource, feedbackMethod);
			}
			else {
				JsonObject githubApiInfo = getGithubApiInfo();
				String githubVersion = getGithubVersion(githubApiInfo);
				if (Objects.equals(githubVersion, modVersion)) {
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

	private JsonArray getModrinthApiInfo() throws IOException {
		URL modrinthApiUrl = new URL("https://api.modrinth.com/v2/project/" + modrinthId + "/version");
		return Requests.GSON.fromJson(new InputStreamReader(Requests.openURLConnection(modrinthApiUrl).getInputStream(), StandardCharsets.UTF_8), JsonArray.class);
	}

	private String getModrinthVersion(JsonArray modrinthApiInfo) {
		return modrinthApiInfo.
			get(0).
			getAsJsonObject().
			get("version_number").
			getAsString().
			replace("\"", "");
	}

	private JsonObject getGithubApiInfo() throws IOException {
		URL githubApiUrl = new URL("https://api.github.com/repos/" + githubId + "/releases/latest");
		return Requests.getJsonObject(githubApiUrl);
	}

	private String getGithubVersion(JsonObject githubApiInfo) {
		return githubApiInfo.
			getAsJsonObject().
			get("tag_name").
			getAsString().
			replace("\"", "");
	}

	private void notifyUpToDate(String updateSource, FeedbackMethod feedbackMethod) {
		if (feedbackMethod == FeedbackMethod.CHAT) {
			UChat.chat(logo + " §cYou are up to date with the mod version on " + updateSource + "!");
		}
		else {
			if (UScreen.getCurrentScreen() != null) { // if this isn't at launch, for auto check updates
				Notifications.INSTANCE.send(modName, "You are up to date with the mod version on " + updateSource + "!");
			}
		}
	}

	private void notifyNewVersion(String newVersion, UpdateSource updateSource, FeedbackMethod feedbackMethod) {
		URI link = updateSource == UpdateSource.MODRINTH ? modrinthLink : githubLink;
		if (feedbackMethod == FeedbackMethod.CHAT) {
			UChat.chat(new UTextComponent(logo + " §eVersion " + newVersion + " is avaliable on " + updateSource.coloredName + "§e!").setClick(ClickEvent.Action.OPEN_URL, link.toString()));
		}
		else {
			Notifications.INSTANCE.send(modName, "Version " + newVersion + " is avaliable on " + updateSource.name + "!", () -> {
				if (!UDesktop.browse(link)) {
					Notifications.INSTANCE.send(modName, "Couldn't open link for " + updateSource.name + "!");
				}
			});
		}
	}

	private void handleError(UpdateSource updateSource, FeedbackMethod feedbackMethod) {
		if (feedbackMethod == FeedbackMethod.CHAT) {
			UChat.chat(logo + " §cCouldn't get update information from " + updateSource.name + "!");
		}
		else {
			Notifications.INSTANCE.send(modName, "Couldn't get update information from " + updateSource.name + "!");
		}
	}

	public URI getModrinthLink() {
		return modrinthLink;
	}

	public URI getGithubLink() {
		return githubLink;
	}

	public enum FeedbackMethod {
		CHAT,
		NOTIFICATIONS
	}
}
