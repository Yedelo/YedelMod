package at.yedel.yedelmod.utils.typeutils;



import java.util.UUID;
import java.util.regex.Pattern;



public class TextUtils {
    public static String commafy(int number) {
        return String.format("%,d", number);
    }

    public static String randomUuid(int length) {
        return UUID.randomUUID().toString().substring(0, length);
    }

    // https://github.com/Polyfrost/UniversalCraft/blob/new-main/src/main/kotlin/gg/essential/universal/UChat.kt
    private static final Pattern ampersandColorPattern = Pattern.compile("(?<!\\\\)&(?![^0-9a-fklmnor]|$)");

    public static String format(String string) {
        return ampersandColorPattern.matcher(string).replaceAll("§");
    }
}
