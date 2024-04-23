package at.yedel.yedelmod.commands;



import at.yedel.yedelmod.config.YedelConfig;
import at.yedel.yedelmod.features.CustomText;
import gg.essential.api.commands.Command;
import gg.essential.api.commands.DefaultHandler;
import gg.essential.universal.UChat;

import static at.yedel.yedelmod.YedelMod.logo;



public class ClearTextCommand extends Command {
    public ClearTextCommand(String name) {
        super(name);
    }

    @DefaultHandler
    public void handle() throws Exception {
        CustomText.instance.displayedText = "";
        YedelConfig.displayedText = "";
        YedelConfig.save();
        UChat.chat(logo + " &eCleared display text!");
    }
}
