package at.yedel.yedelmod.utils;



import java.util.UUID;



public class TextUtils {
    public static String randomUuid(int length) {
        return UUID.randomUUID().toString().substring(0, length);
    }
}
