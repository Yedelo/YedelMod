package at.yedel.yedelmod.commands;



import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;

import at.yedel.yedelmod.utils.Chat;
import at.yedel.yedelmod.utils.Constants.Messages;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.BlockPos;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.logging.log4j.LogManager;



public class YedelMessageCommand extends CommandBase {
    @Override
    public String getCommandName() {
        return "yedelmessage";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return null;
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) throws CommandException {
        new Thread(() -> {
            try {
                CloseableHttpClient client = HttpClients.createDefault();
                HttpGet request = new HttpGet("https://yedelo.github.io/yedelmod.json");
                request.addHeader("User-Agent", HttpHeaders.USER_AGENT);

                HttpResponse response;
                response = client.execute(request);

                BufferedReader reader;
                reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
                StringBuffer result = new StringBuffer();
                String line;
                while ((line = reader.readLine()) != null) {
                    result.append(line);
                }
                JsonObject jsonResult = new JsonParser().parse(String.valueOf(result)).getAsJsonObject();
                String formattedMessage = String.valueOf(jsonResult.get("yedelmod-message-formatted")).replaceAll("\"", "");
                Chat.display(Messages.messageFromYedel);
                Chat.display(formattedMessage);
            }
            catch (Exception e) {
                LogManager.getLogger("Mod Message").error("Couldn't get mod message");
                Chat.display(Messages.couldntGetMessage);
            }
        }, "YedelMod").start();
    }

    @Override
    public boolean canCommandSenderUseCommand(ICommandSender sender) {
        return true;
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos) {
        return null;
    }
}
