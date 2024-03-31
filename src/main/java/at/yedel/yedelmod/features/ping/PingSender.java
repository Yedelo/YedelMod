package at.yedel.yedelmod.features.ping;



import at.yedel.yedelmod.utils.typeutils.TextUtils;
import gg.essential.universal.UChat;
import net.minecraft.network.play.client.C14PacketTabComplete;
import net.minecraft.network.play.client.C16PacketClientStatus;

import static at.yedel.yedelmod.YedelMod.logo;
import static at.yedel.yedelmod.YedelMod.minecraft;
import static at.yedel.yedelmod.config.YedelConfig.pingMethod;



public class PingSender {
    public static PingSender instance = new PingSender();
    private final C16PacketClientStatus.EnumState EnumRequestStats = C16PacketClientStatus.EnumState.REQUEST_STATS;
    public boolean commandCheck = false;
    public boolean statsCheck = false;
    public boolean tabCheck = false;
    public long lastTime;

    public void commandSendPing() {
        switch (pingMethod) {
            case 0:
                pingPing();
            case 1:
                commandPing();
            case 2:
                tabPing();
            case 3:
                statsPing();
            case 4:
                serverListPing();
        }
    }

    public void pingPing() {
        UChat.say("/ping");
    }

    public void commandPing() {
        lastTime = System.nanoTime();
        UChat.say("/" + TextUtils.randomUuid(8));
        commandCheck = true;
    }

    public void tabPing() {
        lastTime = System.nanoTime();
        minecraft.getNetHandler().addToSendQueue(new C14PacketTabComplete("YedelMod"));
        tabCheck = true;
    }

    public void statsPing() {
        lastTime = System.nanoTime();
        minecraft.getNetHandler().addToSendQueue(new C16PacketClientStatus(EnumRequestStats));
        statsCheck = true;
    }

    public void serverListPing() {
        if (minecraft.isSingleplayer()) UChat.chat(logo + " &cThis method does not work in singleplayer!");
        float ping = (float) minecraft.getCurrentServerData().pingToServer;
        if (ping == 0)
            UChat.chat(logo + " &cPing is 0! This might have occured if you used Direct Connect or the favorite server button..");
        else {
            UChat.chat(logo + " &ePing: " + TextUtils.color(ping) + (int) ping + " &ems &7(server list)");
            minecraft.thePlayer.playSound("random.successful_hit", 10, (float) (ping * -0.006 + 2));
        }
    }
}
