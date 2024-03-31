package at.yedel.yedelmod.commands;



import gg.essential.api.commands.Command;
import gg.essential.api.commands.DefaultHandler;
import gg.essential.api.commands.Greedy;
import gg.essential.universal.UChat;



public class SimulateChatCommand extends Command {
    public SimulateChatCommand(String name) {
        super(name);
    }

    @DefaultHandler
    public void handle(@Greedy String message) {
        UChat.chat(message);
    }
}
