package at.yedel.yedelmod.utils.update;



import at.yedel.yedelmod.launch.YedelModConstants;
import at.yedel.yedelmod.utils.Requests;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import dev.deftu.omnicore.client.OmniChat;
import dev.deftu.omnicore.client.OmniDesktop;
import dev.deftu.omnicore.client.OmniScreen;
import dev.deftu.textile.minecraft.MCClickEvent;
import dev.deftu.textile.minecraft.MCSimpleTextHolder;
import org.polyfrost.oneconfig.api.ui.v1.Notifications;
import org.polyfrost.polyui.component.extensions.EventsKt;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

import static at.yedel.yedelmod.launch.YedelModConstants.logo;



public class UpdateManager {
	private UpdateManager() {}

	private static final UpdateManager instance = new UpdateManager();

	public static UpdateManager getInstance() {
		return instance;
	}

	private static final String currentVersion = YedelModConstants.MOD_VERSION;
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
		return Requests.gson.fromJson(new InputStreamReader(Requests.openURLConnection(modrinthApiUrl).getInputStream(), StandardCharsets.UTF_8), JsonArray.class);
	}

	public String getModrinthVersion(JsonArray modrinthApiInfo) {
		return modrinthApiInfo.
			get(0).
			getAsJsonObject().
			get("version_number").
			getAsString().
			replace("\"", "");
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

	public void notifyUpToDate(String updateSource, FeedbackMethod feedbackMethod) {
		if (feedbackMethod == FeedbackMethod.CHAT) {
			OmniChat.displayClientMessage(logo + " §cYou are up to date with the mod version on " + updateSource + "!");
		}
		else {
			if (OmniScreen.getCurrentScreen() != null) { // if this isn't at launch, for auto check updates
				Notifications.enqueue(Notifications.Type.Info, "YedelMod", "You are up to date with the mod version on " + updateSource + "!");
			}
		}
	}

	private void notifyNewVersion(String newVersion, UpdateSource updateSource, FeedbackMethod feedbackMethod) {
		if (feedbackMethod == FeedbackMethod.CHAT) {
			OmniChat.displayClientMessage(new MCSimpleTextHolder(logo + " §eVersion " + newVersion + " is avaliable on " + updateSource.coloredName + "§e!").withClickEvent(MCClickEvent.openUrl(updateSource.uri.toString())));
		}
		else {
			EventsKt.onClick(Notifications.enqueue(Notifications.Type.Info, "YedelMod", "Version " + newVersion + " is avaliable on " + updateSource.name + "! Press %k to open."), (block, event) -> {
					if (!OmniDesktop.browse(updateSource.uri)) {
						Notifications.enqueue(Notifications.Type.Error, "YedelMod", "Couldn't open link for " + updateSource.name + "!");
				}
					return null;
				}
			);
		}
	}

	private void handleError(UpdateSource updateSource, FeedbackMethod feedbackMethod) {
		if (feedbackMethod == FeedbackMethod.CHAT) {
			OmniChat.displayClientMessage(logo + " §cCouldn't get update information from " + updateSource.name + "!");
		}
		else {
			Notifications.enqueue(Notifications.Type.Error, "YedelMod", "Couldn't get update information from " + updateSource.name + "!");
		}
	}

	public enum FeedbackMethod {
		CHAT,
		NOTIFICATIONS
	}
}
