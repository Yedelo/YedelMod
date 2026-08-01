package at.yedel.yedelmod.features.ping;



import at.yedel.yedelmod.utils.Constants;
import cc.polyfrost.oneconfig.libs.universal.UChat;
import cc.polyfrost.oneconfig.libs.universal.USound;

import static at.yedel.yedelmod.launch.YedelModConstants.yedelogo;



public class PingChatInterface {
    private void showPingMessage(String method, float ping) {
        UChat.chat(yedelogo + " &ePing: " + color(ping) + (int) ping + " &ems &7(" + method + ")");
    }

    private void playPingSound(float ping) {
        USound.INSTANCE.playSoundStatic(Constants.PLING_SOUND_LOCATION, 1, (float) (ping * -0.006 + 2));
    }

    private String color(Float ping) {
        if (ping < 50) {
            return "§a";
        }
        else if (ping < 100) {
            return "§2";
        }
        else if (ping < 150) {
            return "§e";
        }
        else if (ping < 200) {
            return "§6";
        }
        else if (ping < 250) {
            return "§c";
        }
        else if (ping < 300) {
            return "§4";
        }
        else if (ping < 350) {
            return "§5"; // wtf?
        }
        else if (ping < 400) {
            return "§d";
        }
        else if (ping < 450) {
            return "§f";
        }
        else if (ping < 500) {
            return "§b";
        }
        else if (ping < 550) {
            return "§3";
        }
        else if (ping < 600) {
            return "§9";
        }
        else if (ping < 650) {
            return "§1";
        }
        else if (ping < 700) {
            return "§7";
        }
        else if (ping < 750) {
            return "§8";
        }
        else {
            return "§0";
        }
    }
}
