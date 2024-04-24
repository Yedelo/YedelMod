package at.yedel.yedelmod.commands;



import java.util.Collections;
import java.util.List;

import at.yedel.yedelmod.utils.Chat;
import at.yedel.yedelmod.utils.typeutils.TextUtils;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.BlockPos;



public class SimulateChatCommand extends CommandBase {
    @Override
    public String getCommandName() {
        return "simulatechat";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return null;
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) throws CommandException {
        String message = TextUtils.joinArgs(args);
        Chat.display(message);
    }

    @Override
    public List<String> getCommandAliases() {
        return Collections.singletonList("simc");
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
