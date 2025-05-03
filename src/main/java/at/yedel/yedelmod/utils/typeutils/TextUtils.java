package at.yedel.yedelmod.utils.typeutils;



import java.util.UUID;



public class TextUtils {
    public static String commafy(int number) {
        return String.format("%,d", number);
    }

    public static String randomUuid(int length) {
        return UUID.randomUUID().toString().substring(0, length);
    }
}
