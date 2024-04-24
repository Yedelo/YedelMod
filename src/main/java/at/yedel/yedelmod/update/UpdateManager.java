package at.yedel.yedelmod.update;



import java.awt.Desktop;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Objects;

import at.yedel.yedelmod.YedelMod;
import at.yedel.yedelmod.utils.Chat;
import at.yedel.yedelmod.utils.Constants;
import at.yedel.yedelmod.utils.Constants.Messages;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.minecraft.util.ChatComponentText;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static at.yedel.yedelmod.YedelMod.logo;
import static at.yedel.yedelmod.YedelMod.minecraft;

;



public class UpdateManager {
    public static final UpdateManager instance = new UpdateManager();
    public static final String modrinthLink = "https://modrinth.com/mod/yedelmod/versions";
    public static final String githubLink = "https://github.com/Yedelo/YedelMod/releases";
    private final Logger updateLogger = LogManager.getLogger("Update Manager");

    public void checkVersion(UpdateSource source, String type) {
        new Thread(() -> {
            try {
                checkVersionInThread(source, type);
            }
            catch (Exception e) {
                throw new RuntimeException(e);
            }
        }, "YedelMod").start();
    }

    private void checkVersionInThread(UpdateSource source, String type) throws Exception {
        try {
            CloseableHttpClient client = HttpClients.createDefault();
            HttpGet request = new HttpGet(source.apiLink);
            request.addHeader("User-Agent", HttpHeaders.USER_AGENT);

            HttpResponse response = null;
            response = client.execute(request);
            updateLogger.info("\nGetting latest version from " + source.apiLink + " (" + source.name + ")");
            updateLogger.info("Response: " + response.getStatusLine().getStatusCode());

            BufferedReader reader = null;
            reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
            StringBuffer result = new StringBuffer();
            String line = "";
            while ((line = reader.readLine()) != null) {
                result.append(line);
            }
            String version;
            if (source == UpdateSource.MODRINTH) {
                JsonArray jsonArray = new Gson().fromJson(String.valueOf(result), JsonArray.class);
                JsonObject jsonResult = jsonArray.get(0).getAsJsonObject();
                version = String.valueOf(jsonResult.get("version_number")).replaceAll("\"", "");
            }
            else {
                JsonObject jsonResult = new JsonParser().parse(String.valueOf(result)).getAsJsonObject();
                version = String.valueOf(jsonResult.get("tag_name")).replaceAll("\"", "");
                if (version.equals("null")) version = null;
            }
            if (type == "chat") {
                if (!version.equals(YedelMod.version)) sendUpdateMessage(source, version);
                else Chat.display(Messages.latestVersion);
            }
            else {
                if (!version.equals(YedelMod.version)) sendUpdateNotification(source, version);
                else Constants.notifications.push("YedelMod", "You are already on the latest version!");
            }
        }
        catch (Exception error) {
            handleUpdateError(error, type, source.name);
        }
    }

    private void sendUpdateMessage(UpdateSource source, String version) {
        minecraft.thePlayer.addChatMessage(
                new ChatComponentText(
                        logo + " §eVersion " + version + " is avaliable on ")
                        .appendSibling(source.msg)
                        .appendSibling(new ChatComponentText(" §e§nClick to update.")
                        )
        );
    }

    private void sendUpdateNotification(UpdateSource source, String version) {
        Constants.notifications.push("YedelMod", "Version " + version + " is avaliable on " + source.name + "! Click to update.", () -> {
            if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
                try {
                    Desktop.getDesktop().browse(new URI(source.link));
                }
                catch (IOException | URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            }
            return null;
        });
    }

    private void handleUpdateError(Exception error, String type, String sourceName) throws Exception {
        updateLogger.error("Couldn't check for updates");
        if (Objects.equals(type, "chat")) Chat.logoDisplay("&cCouldn't check for updates on " + sourceName + "!");
        else Constants.notifications.push("YedelMod", "Couldn't check for updates on " + sourceName + "!");
        throw error;
    }
}


