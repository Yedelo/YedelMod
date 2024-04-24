package at.yedel.yedelmod.commands;



import java.util.Arrays;
import java.util.List;

import at.yedel.yedelmod.utils.Chat;
import at.yedel.yedelmod.utils.ThreadManager;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.BlockPos;

import static at.yedel.yedelmod.YedelMod.minecraft;



public class LimboCommand extends CommandBase {
    @Override
    public String getCommandName() {
        return "yedelli";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return null;
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) throws CommandException {
        Chat.say("§");
        ThreadManager.scheduleOnce(() -> {
            if (!minecraft.theWorld.getScoreboard().getScores().isEmpty() /* if no scoreboard */) Chat.say("§");
        }, 500);
    }

    @Override
    public List<String> getCommandAliases() {
        return Arrays.asList("yli", "li"); // long island
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
