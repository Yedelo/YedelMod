package at.yedel.yedelmod.commands;



import at.yedel.yedelmod.config.YedelConfig;
import gg.essential.api.commands.Command;
import gg.essential.api.commands.DefaultHandler;
import gg.essential.universal.UChat;

import static at.yedel.yedelmod.YedelMod.logo;



public class PlaytimeCommand extends Command {
    public PlaytimeCommand(String name) {
        super(name);
    }

    @DefaultHandler
    public void handle() {
        UChat.chat(logo + " &ePlaytime: &6" + YedelConfig.playtimeMinutes / 60 + " hours &eand &6" + YedelConfig.playtimeMinutes % 60 + " minutes");
    }
}