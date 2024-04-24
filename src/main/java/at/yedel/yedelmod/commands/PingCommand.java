package at.yedel.yedelmod.commands;



import java.util.Collections;
import java.util.List;
import java.util.Objects;

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
            PingSender.instance.defaultMethodPing();
            return;
        }
        String arg = args[0];
        if (Objects.equals(arg, "ping") || Objects.equals(arg, "p")) {
            Chat.command("ping");
        }
        else if (Objects.equals(arg, "command") || Objects.equals(arg, "c")) {
            PingSender.instance.commandPing();
        }
        else if (Objects.equals(arg, "tab") || Objects.equals(arg, "t")) {
            PingSender.instance.tabPing();
        }
        else if (Objects.equals(arg, "stats") || Objects.equals(arg, "s")) {
            PingSender.instance.statsPing();
        }
        else if (Objects.equals(arg, "list") || Objects.equals(arg, "l")) {
            PingSender.instance.serverListPing();
        }
        else {
            PingSender.instance.defaultMethodPing();
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
