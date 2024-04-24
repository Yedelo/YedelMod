package at.yedel.yedelmod.utils;



import at.yedel.yedelmod.utils.typeutils.TextUtils;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;

import static at.yedel.yedelmod.YedelMod.logo;
import static at.yedel.yedelmod.YedelMod.minecraft;



public class Chat {
    public static void display(Object message) {
        String string = TextUtils.replaceAmpersand(message.toString());
        minecraft.thePlayer.addChatMessage(new ChatComponentText(string));
    }

    public static void logoDisplay(Object message) {
        String string = TextUtils.replaceAmpersand(message.toString());
        minecraft.thePlayer.addChatMessage(new ChatComponentText(logo + " " + string));
    }

    public static void display(IChatComponent message) {
        minecraft.thePlayer.addChatMessage(message);
    }

    public static void say(String message) {
        minecraft.thePlayer.sendChatMessage(message);
    }

    public static void command(String command) {
        minecraft.thePlayer.sendChatMessage('/' + command);
    }
}
