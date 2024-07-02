package at.yedel.yedelmod.commands;



import java.util.List;

import at.yedel.yedelmod.utils.Chat;
import at.yedel.yedelmod.utils.Constants.messages;
import at.yedel.yedelmod.utils.typeutils.TextUtils;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.BlockPos;
import org.lwjgl.opengl.Display;



public class SetTitleCommand extends CommandBase {
    @Override
    public String getCommandName() {
        return "settitle";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return null;
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) throws CommandException {
        if (args.length == 0) {
            Chat.display(messages.enterValidTitle);
            return;
        }
        String title = TextUtils.joinArgs(args);
        Chat.logoDisplay("&eSet display title to \"&f" + title + "&e\"!");
        Display.setTitle(title);
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
