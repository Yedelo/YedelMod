package at.yedel.yedelmod.commands;



import java.util.Arrays;
import java.util.List;

import at.yedel.yedelmod.features.LimboCreativeCheck;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.BlockPos;



public class LimboCreativeCommand extends CommandBase {
    @Override
    public String getCommandName() {
        return "yedellimbocreative";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return null;
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) throws CommandException {
        LimboCreativeCheck.checkLimbo();
    }

    @Override
    public List<String> getCommandAliases() {
        return Arrays.asList("limbogmc", "lgmc");
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
