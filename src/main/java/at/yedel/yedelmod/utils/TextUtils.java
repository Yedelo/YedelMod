package at.yedel.yedelmod.utils;



import java.util.UUID;
import java.util.regex.Pattern;



public class TextUtils {
    private static final Pattern ampersandFormattingPattern = Pattern.compile("&([0123456789abcdefklnor])");

    public static String replaceAmpersand(String string) {
        return ampersandFormattingPattern.matcher(string).replaceAll("§$1");
    }

    public static String randomUuid(int length) {
        return UUID.randomUUID().toString().substring(0, length);
    }
}
