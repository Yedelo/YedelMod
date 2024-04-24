package at.yedel.yedelmod.commands;



import java.util.List;

import at.yedel.yedelmod.config.YedelConfig;
import at.yedel.yedelmod.features.CustomText;
import at.yedel.yedelmod.utils.Chat;
import at.yedel.yedelmod.utils.Constants.Messages;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.BlockPos;



public class ClearTextCommand extends CommandBase {
    @Override
    public String getCommandName() {
        return "cleartext";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return null;
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) throws CommandException {
        CustomText.instance.displayedText = "";
        YedelConfig.displayedText = "";
        YedelConfig.save();
        Chat.display(Messages.clearedDisplayText);
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
