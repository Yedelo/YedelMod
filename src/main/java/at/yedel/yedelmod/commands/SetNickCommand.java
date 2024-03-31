package at.yedel.yedelmod.commands;



import at.yedel.yedelmod.config.YedelConfig;
import gg.essential.api.commands.Command;
import gg.essential.api.commands.DefaultHandler;
import gg.essential.universal.UChat;



public class SetNickCommand extends Command {
    public SetNickCommand(String name) {
        super(name);
    }

    @DefaultHandler
    public void handle(String nick) {
        UChat.chat("[§c§lBounty§f§lHunting] §eSet nick to " + nick + "§e!");
        YedelConfig.nick = nick;
        YedelConfig.save();
    }
}
