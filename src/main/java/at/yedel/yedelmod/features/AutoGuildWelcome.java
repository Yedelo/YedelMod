package at.yedel.yedelmod.features;



import at.yedel.yedelmod.config.YedelConfig;
import at.yedel.yedelmod.utils.Chat;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

;



public class AutoGuildWelcome {
    private static final AutoGuildWelcome instance = new AutoGuildWelcome();

    public static AutoGuildWelcome getInstance() {
        return instance;
    }

    @SubscribeEvent
    public void onGuildJoinMessage(ClientChatReceivedEvent event) {
        if (!YedelConfig.getInstance().guildWelcome) return;
        String msg = event.message.getUnformattedText();
        if (!msg.endsWith("joined the guild!")) return;
        if (msg.contains("[")) { // ranked
            if (!msg.startsWith("[")) return; // fake messages ending in "joined the guild!"
        }

        String guildName;

        int indexOfSpace = msg.indexOf(" ");
        if (msg.contains("[")) {
            int indexOfGuildJoin = msg.indexOf(" joined the guild!");
            guildName = msg.substring(indexOfSpace + 1, indexOfGuildJoin);
        }
        else {
            guildName = msg.substring(0, indexOfSpace);
        }
        Chat.command("gc " + YedelConfig.getInstance().guildWelcomeMessage.replace("[player]", guildName));
    }
}
