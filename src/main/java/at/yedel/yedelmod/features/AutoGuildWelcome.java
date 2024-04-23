package at.yedel.yedelmod.features;



import at.yedel.yedelmod.config.YedelConfig;
import gg.essential.universal.UChat;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;



public class AutoGuildWelcome {
    public static AutoGuildWelcome instance = new AutoGuildWelcome();
    @SubscribeEvent
    public void onGuildJoinMessage(ClientChatReceivedEvent event) {
        if (!YedelConfig.guildWelcome) return;
        String msg = event.message.getUnformattedText();
        if (!msg.endsWith("joined the guild!")) return;
        if (msg.contains("[")) { // ranked
            if (!msg.startsWith("[")) return; // fake messages ending in "joined the guild!"
        }
        UChat.say("/gc " + YedelConfig.guildWelcomeMessage.replace("[player]", getGuildName(msg)));
    }

    private String getGuildName(String joinString) {
        int indexOfSpace = joinString.indexOf(" ");
        if (joinString.contains("[")) {
            int indexOfGuildJoin = joinString.indexOf(" joined the guild!");
            return joinString.substring(indexOfSpace + 1, indexOfGuildJoin);
        }
        else {
            return joinString.substring(0, indexOfSpace);
        }
    }
}
