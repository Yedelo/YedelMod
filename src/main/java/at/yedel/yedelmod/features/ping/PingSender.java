package at.yedel.yedelmod.features.ping;



import at.yedel.yedelmod.config.YedelConfig;
import at.yedel.yedelmod.utils.typeutils.TextUtils;
import cc.polyfrost.oneconfig.libs.universal.UChat;
import cc.polyfrost.oneconfig.libs.universal.UMinecraft;
import cc.polyfrost.oneconfig.libs.universal.USound;
import cc.polyfrost.oneconfig.utils.hypixel.HypixelUtils;
import net.hypixel.modapi.HypixelModAPI;
import net.hypixel.modapi.packet.impl.serverbound.ServerboundPingPacket;
import net.minecraft.network.play.client.C14PacketTabComplete;
import net.minecraft.network.play.client.C16PacketClientStatus;
import net.minecraft.util.ResourceLocation;

import static at.yedel.yedelmod.launch.YedelModConstants.LOGO;



public class PingSender {
    private PingSender() {}

    private static final PingSender INSTANCE = new PingSender();

    public static PingSender getInstance() {
        return INSTANCE;
    }

    public boolean commandCheck = false;
    public boolean statsCheck = false;
    public boolean tabCheck = false;
    public boolean hypixelCheck = false;
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
            case 5:
                hypixelPing();
                break;
            case 4:
            default:
                serverListPing();
                break;
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
        UMinecraft.getNetHandler().addToSendQueue(new C14PacketTabComplete("#"));
        tabCheck = true;
    }

    public void statsPing() {
        lastTime = System.nanoTime();
        UMinecraft.getNetHandler().addToSendQueue(new C16PacketClientStatus(C16PacketClientStatus.EnumState.REQUEST_STATS));
        statsCheck = true;
    }

    public void hypixelPing() {
        // Yedel no longer uses essential features
        if (HypixelUtils.INSTANCE.isHypixel()) {
            lastTime = System.nanoTime();
            HypixelModAPI.getInstance().sendPacket(new ServerboundPingPacket());
            hypixelCheck = true;
        }
        else UChat.chat(LOGO + " §cYou must be on Hypixel to use this!");
    }

    public void serverListPing() {
        if (UMinecraft.getMinecraft().isSingleplayer()) {
            UChat.chat(LOGO + " §cThis method does not work in singleplayer!");
        }
        float ping = (float) UMinecraft.getMinecraft().getCurrentServerData().pingToServer;
        if (ping == 0) {
            UChat.chat(LOGO + " §cPing is 0! This might have occured if you used Direct Connect or the favorite server button.");
        }
        else {
            UChat.chat(LOGO + " &ePing: " + TextUtils.color(ping) + (int) ping + " &ems &7(server list)");
            USound.INSTANCE.playSoundStatic(new ResourceLocation("random.successful_hit"), 1, (float) (ping * -0.006 + 2));
        }
    }
}
