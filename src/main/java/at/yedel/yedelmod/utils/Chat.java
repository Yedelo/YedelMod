package at.yedel.yedelmod.utils;



import at.yedel.yedelmod.utils.typeutils.TextUtils;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;

import static at.yedel.yedelmod.YedelMod.minecraft;
import static at.yedel.yedelmod.utils.Constants.logo;



public class Chat {
    public static void display(Object message) {
        String string = TextUtils.replaceAmpersand(message.toString());
        if (minecraft.thePlayer != null) minecraft.thePlayer.addChatMessage(new ChatComponentText(string));
    }

    public static void logoDisplay(Object message) {
        String string = TextUtils.replaceAmpersand(message.toString());
        if (minecraft.thePlayer != null) minecraft.thePlayer.addChatMessage(new ChatComponentText(logo + " " + string));
    }

    public static void display(IChatComponent message) {
        if (minecraft.thePlayer != null) minecraft.thePlayer.addChatMessage(message);
    }

    public static void say(String message) {
        if (minecraft.thePlayer != null) minecraft.thePlayer.sendChatMessage(message);
    }

    public static void command(String command) {
        if (minecraft.thePlayer != null) minecraft.thePlayer.sendChatMessage('/' + command);
    }
}
