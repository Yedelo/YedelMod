package at.yedel.yedelmod.commands;



import java.util.Arrays;
import java.util.List;

import at.yedel.yedelmod.config.YedelConfig;
import at.yedel.yedelmod.utils.Chat;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.BlockPos;



public class PlaytimeCommand extends CommandBase {
    @Override
    public String getCommandName() {
        return "yedelplaytime";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return null;
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) throws CommandException {
        Chat.logoDisplay("&ePlaytime: &6" + YedelConfig.playtimeMinutes / 60 + " hours &eand &6" + YedelConfig.playtimeMinutes % 60 + " minutes");
    }

    @Override
    public List<String> getCommandAliases() {
        return Arrays.asList("yedelpt", "ypt");
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
