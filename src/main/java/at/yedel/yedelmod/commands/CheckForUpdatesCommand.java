package at.yedel.yedelmod.commands;



import java.util.List;
import java.util.Objects;

import at.yedel.yedelmod.config.YedelConfig;
import at.yedel.yedelmod.update.UpdateManager;
import at.yedel.yedelmod.update.UpdateSource;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.BlockPos;



public class CheckForUpdatesCommand extends CommandBase {
    @Override
    public String getCommandName() {
        return "yedelupdate";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return null;
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) throws CommandException {
        if (args.length == 0) {
            UpdateManager.instance.checkVersion(YedelConfig.getUpdateSource(), "chat");
            return;
        }
        String arg = args[0];
        if (Objects.equals(arg, "modrinth")) {
            UpdateManager.instance.checkVersion(UpdateSource.MODRINTH, "chat");
        }
        else if (Objects.equals(arg, "github")) {
            UpdateManager.instance.checkVersion(UpdateSource.GITHUB, "chat");
        }
    }

    @Override
    public boolean canCommandSenderUseCommand(ICommandSender sender) {
        return true;
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos) {
        if (args.length == 1) return getListOfStringsMatchingLastWord(args, "modrinth", "github");
        return null;
    }
}
