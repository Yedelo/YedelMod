package at.yedel.yedelmod.commands;



import at.yedel.yedelmod.gui.MoveTextGui;
import gg.essential.api.commands.Command;
import gg.essential.api.commands.DefaultHandler;
import gg.essential.api.utils.GuiUtil;



public class MoveTextCommand extends Command {
    public MoveTextCommand(String name) {
        super(name);
    }

    @DefaultHandler
    public void handle() {
        GuiUtil.open(MoveTextGui.instance);
    }
}
