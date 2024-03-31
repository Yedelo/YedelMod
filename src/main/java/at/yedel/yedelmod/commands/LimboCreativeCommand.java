package at.yedel.yedelmod.commands;



import at.yedel.yedelmod.features.LimboCreativeCheck;
import gg.essential.api.commands.Command;
import gg.essential.api.commands.DefaultHandler;



public class LimboCreativeCommand extends Command {
    public LimboCreativeCommand(String name) {super(name);}

    @DefaultHandler
    public void handle() {
        LimboCreativeCheck.checkLimbo();
    }
}
