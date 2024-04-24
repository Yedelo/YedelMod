package at.yedel.yedelmod.commands;



import java.util.List;

import at.yedel.yedelmod.config.YedelConfig;
import at.yedel.yedelmod.features.CustomText;
import at.yedel.yedelmod.utils.Chat;
import at.yedel.yedelmod.utils.Constants.Messages;
import at.yedel.yedelmod.utils.typeutils.TextUtils;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.BlockPos;



public class SetTextCommand extends CommandBase {
    @Override
    public String getCommandName() {
        return "settext";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return null;
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) throws CommandException {
        if (args.length == 0) {
            Chat.display(Messages.enterValidText);
            return;
        }
        String displayText = TextUtils.joinArgs(args);
        displayText = displayText.replaceAll("&", "§");
        CustomText.instance.displayedText = displayText;
        YedelConfig.displayedText = displayText;
        YedelConfig.save();
        Chat.logoDisplay("&eSet displayed text to \"&r" + displayText + "&e\"!");
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
