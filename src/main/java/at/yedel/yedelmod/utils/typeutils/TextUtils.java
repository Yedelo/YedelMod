package at.yedel.yedelmod.utils.typeutils;



import java.util.UUID;



public class TextUtils {
    public static String commafy(int number) {
        return String.format("%,d", number);
    }

    public static String removeFormatting(String string) {
        return string.replaceAll("[&§][0123456789abcdefklnor]", "");
    }

    public static String removeSection(String string) {
        return string.replaceAll("§[0123456789abcdefklnor]", "");
    }

    public static String removeAmpersand(String string) {
        return string.replaceAll("&[0123456789abcdefklnor]", "");
    }

    public static String randomUuid(int length) {
        return UUID.randomUUID().toString().substring(0, length);
    }

    public static String color(Float ping) {
        if (ping < 50) return "§a";
        else if (ping < 100) return "§2";
        else if (ping < 150) return "§e";
        else if (ping < 200) return "§6";
        else if (ping < 250) return "§c";
        else if (ping < 300) return "§4";
        else if (ping < 350) return "§5"; // wtf?
        else if (ping < 400) return "§d";
        else if (ping < 450) return "§f";
        else if (ping < 500) return "§b";
        else if (ping < 550) return "§3";
        else if (ping < 600) return "§9";
        else if (ping < 650) return "§1";
        else if (ping < 700) return "§7";
        else if (ping < 740) return "§8";
        else return "§0";
    }

    public static String joinArgs(String[] array) {
        return String.join(" ", array);
    }
}
