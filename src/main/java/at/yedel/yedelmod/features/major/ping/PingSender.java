package at.yedel.yedelmod.features.major.ping;



import at.yedel.yedelmod.config.YedelConfig;
import at.yedel.yedelmod.utils.Chat;
import at.yedel.yedelmod.utils.Constants.messages;
import at.yedel.yedelmod.utils.typeutils.TextUtils;
import net.minecraft.network.play.client.C14PacketTabComplete;
import net.minecraft.network.play.client.C16PacketClientStatus;

import static at.yedel.yedelmod.YedelMod.minecraft;



public class PingSender {
    private static final PingSender instance = new PingSender();

    public static PingSender getInstance() {
        return instance;
    }

    private final C16PacketClientStatus.EnumState EnumRequestStats = C16PacketClientStatus.EnumState.REQUEST_STATS;
    public boolean commandCheck = false;
    public boolean statsCheck = false;
    public boolean tabCheck = false;
    public long lastTime;

    public void defaultMethodPing() {
        switch (YedelConfig.getInstance().pingMethod) {
            case 0:
                pingPing();
                break;
            case 1:
                commandPing();
                break;
            case 2:
                tabPing();
                break;
            case 3:
                statsPing();
                break;
            default:
                serverListPing();
                break;
        }
    }

    public void pingPing() {
        Chat.command("ping");
    }

    public void commandPing() {
        lastTime = System.nanoTime();
        Chat.command(TextUtils.randomUuid(8));
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
        if (minecraft.isSingleplayer()) Chat.display(messages.listPingInSingleplayer);
        float ping = (float) minecraft.getCurrentServerData().pingToServer;
        if (ping == 0)
            Chat.display(messages.pingIs0);
        else {
            Chat.logoDisplay("&ePing: " + TextUtils.color(ping) + (int) ping + " &ems &7(server list)");
            minecraft.thePlayer.playSound("random.successful_hit", 10, (float) (ping * -0.006 + 2));
        }
    }
}
