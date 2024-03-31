package at.yedel.yedelmod.commands;



import java.util.concurrent.TimeUnit;

import gg.essential.api.commands.Command;
import gg.essential.api.commands.DefaultHandler;
import gg.essential.api.utils.Multithreading;
import gg.essential.universal.UChat;

import static at.yedel.yedelmod.YedelMod.minecraft;



public class LimboCommand extends Command {
    public LimboCommand(String name) {
        super(name);
    }

    @DefaultHandler
    public void handle() {
        UChat.say("§");
        Multithreading.schedule(() -> {
            if (minecraft.theWorld.getScoreboard().getScores().isEmpty() /* if no scoreboard */) UChat.say("§");
        }, 500, TimeUnit.MILLISECONDS);

    }
}