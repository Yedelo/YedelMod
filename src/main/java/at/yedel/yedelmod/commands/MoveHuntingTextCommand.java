package at.yedel.yedelmod.commands;



import java.util.List;

import at.yedel.yedelmod.gui.MoveHuntingTextGui;
import at.yedel.yedelmod.utils.Functions;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.BlockPos;

import static at.yedel.yedelmod.YedelMod.minecraft;



public class MoveHuntingTextCommand extends CommandBase {
    @Override
    public String getCommandName() {
        return "movehuntingtext";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return null;
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) throws CommandException {
        Functions.displayScreen(new MoveHuntingTextGui(minecraft.currentScreen));
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
