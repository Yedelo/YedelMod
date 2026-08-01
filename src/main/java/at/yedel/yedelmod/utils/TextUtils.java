package at.yedel.yedelmod.utils;



import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;

import java.util.UUID;



public class TextUtils {
    // temporary. hopefully oneconfig gets their stuff fixed
    public static void chat(Object object) {
        Minecraft.getInstance().gui.getChat().addClientSystemMessage(Component.literal(object.toString()));
    }

    public static String commafy(int number) {
        return String.format("%,d", number);
    }

    public static String randomUuid(int length) {
        return UUID.randomUUID().toString().substring(0, length);
    }
}
