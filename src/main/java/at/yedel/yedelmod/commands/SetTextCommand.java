package at.yedel.yedelmod.commands;



import at.yedel.yedelmod.config.YedelConfig;
import at.yedel.yedelmod.features.CustomText;
import gg.essential.api.commands.Command;
import gg.essential.api.commands.DefaultHandler;
import gg.essential.api.commands.Greedy;
import gg.essential.universal.UChat;

import static at.yedel.yedelmod.YedelMod.logo;



public class SetTextCommand extends Command {
    public SetTextCommand(String name) {
        super(name);
    }

    @DefaultHandler
    public void handle(@Greedy String displayText) {
        displayText = displayText.replaceAll("&", "§");
        CustomText.instance.displayedText = displayText;
        YedelConfig.displayedText = displayText;
        YedelConfig.save();
        UChat.chat(logo + " &eSet displayed text to \"&r" + displayText + "&e\"!");
    }
}
