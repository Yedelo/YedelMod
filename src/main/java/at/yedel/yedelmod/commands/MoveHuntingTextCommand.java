package at.yedel.yedelmod.commands;



import at.yedel.yedelmod.gui.MoveHuntingTextGui;
import gg.essential.api.commands.Command;
import gg.essential.api.commands.DefaultHandler;
import gg.essential.api.utils.GuiUtil;



public class MoveHuntingTextCommand extends Command {
    public MoveHuntingTextCommand(String name) {
        super(name);
    }

    @DefaultHandler
    public void handle() {
        GuiUtil.open(MoveHuntingTextGui.instance);
    }
}
