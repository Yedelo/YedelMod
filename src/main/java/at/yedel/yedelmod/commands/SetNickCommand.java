package at.yedel.yedelmod.commands;



import java.util.List;

import at.yedel.yedelmod.config.YedelConfig;
import at.yedel.yedelmod.utils.Chat;
import at.yedel.yedelmod.utils.Constants.messages;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.BlockPos;



public class SetNickCommand extends CommandBase {
    @Override
    public String getCommandName() {
        return "setnick";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return null;
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) throws CommandException {
        if (args.length == 0) {
            Chat.display(messages.enterValidNick);
            return;
        }
        String nick = args[0];
        Chat.display("&6&l[BountyHunting] §eSet nick to " + nick + "§e!");
        YedelConfig.getInstance().nick = nick;
        YedelConfig.getInstance().save();
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
