package at.yedel.yedelmod.commands;



import java.io.BufferedReader;
import java.io.InputStreamReader;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import gg.essential.api.commands.Command;
import gg.essential.api.commands.DefaultHandler;
import gg.essential.universal.UChat;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.logging.log4j.LogManager;

import static at.yedel.yedelmod.YedelMod.logo;



public class YedelMessageCommand extends Command {
    public YedelMessageCommand(String name) {
        super(name);
    }

    @DefaultHandler
    public void handle() {
        new Thread(() -> {
            try {
                CloseableHttpClient client = HttpClients.createDefault();
                HttpGet request = new HttpGet("https://yedelo.github.io/yedelmod.json");
                request.addHeader("User-Agent", HttpHeaders.USER_AGENT);

                HttpResponse response = null;
                response = client.execute(request);

                BufferedReader reader = null;
                reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
                StringBuffer result = new StringBuffer();
                String line = "";
                while ((line = reader.readLine()) != null) {
                    result.append(line);
                }
                JsonObject jsonResult = new JsonParser().parse(String.valueOf(result)).getAsJsonObject();
                String formattedMessage = String.valueOf(jsonResult.get("yedelmod-message-formatted")).replaceAll("\"", "");
                UChat.chat(logo + " &eMessage from Yedel:");
                UChat.chat(formattedMessage);
            }
            catch (Exception e) {
                LogManager.getLogger("Mod Message").error("Couldn't get mod message");
                UChat.chat(logo + " &cCouldn't get mod message");
            }
        }, "YedelMod").start();
    }
}
