package at.yedel.yedelmod.commands;



import gg.essential.api.commands.Command;
import gg.essential.api.commands.DefaultHandler;



// this is just a command for me to do functions with hotswap, it's for no real feature
public class HotSwapCommand extends Command {
    public HotSwapCommand(String name) {
        super(name);
    }

    @DefaultHandler
    public void handle(String arg) {

    }
}
