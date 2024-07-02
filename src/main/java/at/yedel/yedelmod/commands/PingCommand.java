package at.yedel.yedelmod.commands;



import java.util.Collections;
import java.util.List;

import at.yedel.yedelmod.features.major.ping.PingSender;
import at.yedel.yedelmod.utils.Chat;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.BlockPos;



public class PingCommand extends CommandBase {
    @Override
    public String getCommandName() {
        return "yping";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return null;
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) throws CommandException {
        if (args.length == 0) {
            PingSender.getInstance().defaultMethodPing();
            return;
        }
        String arg = args[0];
        if (arg.equals("ping") || arg.equals("p")) {
            Chat.command("ping");
        }
        else if (arg.equals("command") || arg.equals("c")) {
            PingSender.getInstance().commandPing();
        }
        else if (arg.equals("tab") || arg.equals("t")) {
            PingSender.getInstance().tabPing();
        }
        else if (arg.equals("stats") || arg.equals("s")) {
            PingSender.getInstance().statsPing();
        }
        else if (arg.equals("list") || arg.equals("l")) {
            PingSender.getInstance().serverListPing();
        }
        else {
            PingSender.getInstance().defaultMethodPing();
        }
    }

    @Override
    public List<String> getCommandAliases() {
        return Collections.singletonList("yp");
    }

    @Override
    public boolean canCommandSenderUseCommand(ICommandSender sender) {
        return true;
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos) {
        if (args.length == 1) return getListOfStringsMatchingLastWord(args, "ping", "command", "tab", "stats", "list");
        return null;
    }
}
