package at.yedel.yedelmod.commands;



import java.util.List;

import at.yedel.yedelmod.gui.MoveTextGui;
import at.yedel.yedelmod.utils.Functions;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.BlockPos;



public class MoveTextCommand extends CommandBase {
    @Override
    public String getCommandName() {
        return "movetext";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return null;
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) throws CommandException {
        Functions.displayScreen(MoveTextGui.instance);
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
