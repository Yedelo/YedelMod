package at.yedel.yedelmod.commands;



import at.yedel.yedelmod.features.major.ping.PingSender;
import gg.essential.api.commands.Command;
import gg.essential.api.commands.DefaultHandler;
import gg.essential.api.commands.SubCommand;
import gg.essential.universal.UChat;



public class PingCommand extends Command {
    public PingCommand(String name) {
        super(name);
    }

    @DefaultHandler
    public void handle() {
        PingSender.instance.commandSendPing(); // change this later
    }

    @SubCommand(value = "ping", aliases = {"p"}, description = "Try to get ping from /ping command")
    public void getPingFromPingCommand() {
        UChat.say("/ping");
    }

    @SubCommand(value = "command", aliases = {"c"}, description = "Get ping from an unknown command response")
    public void getPingFromCommand() {
        PingSender.instance.commandPing();
    }

    @SubCommand(value = "tab", aliases = {"t"}, description = "Get ping from a tab autofill packet")
    public void getPingFromTab() {
        PingSender.instance.tabPing();
    }

    @SubCommand(value = "stats", aliases = {"s"}, description = "Get ping from a stats request packet")
    public void getPingFromStats() {
        PingSender.instance.statsPing();
    }

    @SubCommand(value = "list", aliases = {"l"}, description = "Get ping from server list")
    public void getPingFromServerList() {
        PingSender.instance.serverListPing();
    }
}
