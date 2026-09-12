package at.yedel.yedelmod.utils;



import net.minecraft.client.Minecraft;

import java.util.UUID;
import java.util.regex.Pattern;



public class TextUtils {
    private static final Pattern ampersandFormattingPattern = Pattern.compile("&([0123456789abcdefklnor])");

    public static void sendChat(String message) {
        //? if legacy {
        Minecraft.getMinecraft().thePlayer.sendChatMessage(message);
        //?} else {
        //Minecraft.getInstance().player.connection.sendChat(message);
        //?}
    }

    public static String replaceAmpersand(String string) {
        return ampersandFormattingPattern.matcher(string).replaceAll("§$1");
    }

    public static String randomUuid(int length) {
        return UUID.randomUUID().toString().substring(0, length);
    }
}
