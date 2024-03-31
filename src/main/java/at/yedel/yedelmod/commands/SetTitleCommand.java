package at.yedel.yedelmod.commands;



import gg.essential.api.commands.Command;
import gg.essential.api.commands.DefaultHandler;
import gg.essential.api.commands.Greedy;
import gg.essential.universal.UChat;
import org.lwjgl.opengl.Display;

import static at.yedel.yedelmod.YedelMod.logo;



public class SetTitleCommand extends Command {
    public SetTitleCommand(String name) {
        super(name);
    }

    @DefaultHandler
    public void handle(@Greedy String args) {
        UChat.chat(logo + " &eSet display title to \"" + args + "&e\"!");
        Display.setTitle(args);
    }
}
