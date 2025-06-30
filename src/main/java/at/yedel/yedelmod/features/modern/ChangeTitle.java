package at.yedel.yedelmod.features.modern;



import at.yedel.yedelmod.api.config.YedelConfig;
import cc.polyfrost.oneconfig.libs.universal.UMinecraft;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent.ClientDisconnectionFromServerEvent;
import org.lwjgl.opengl.Display;

import java.util.Objects;



public class ChangeTitle {
    private static final ChangeTitle INSTANCE = new ChangeTitle();

    public static ChangeTitle getInstance() {
        return INSTANCE;
    }

    private ChangeTitle() {}

    @SubscribeEvent
    public void onServerJoin(FMLNetworkEvent.ClientConnectedToServerEvent event) {
        if (YedelConfig.getInstance().enabled && YedelConfig.getInstance().changeWindowTitle) {
            UMinecraft.getMinecraft().addScheduledTask(() -> {
                if (event.isLocal) {
                    Display.setTitle("Minecraft 1.8.9 - Singleplayer");
                    return;
                }
                ServerData serverData = UMinecraft.getMinecraft().getCurrentServerData();
                if (Objects.equals(serverData.serverName, "Minecraft Server")) { // Direct connect
                    Display.setTitle("Minecraft 1.8.9 - " + serverData.serverIP);
                }
                else {
                    Display.setTitle("Minecraft 1.8.9 - " + serverData.serverName + " - " + serverData.serverIP);
                }
            });
        }
    }

    @SubscribeEvent
    public void onDisconnectFromServer(ClientDisconnectionFromServerEvent event) {
        UMinecraft.getMinecraft().addScheduledTask(() -> {
            if ((YedelConfig.getInstance().enabled && YedelConfig.getInstance().changeWindowTitle) || !Objects.equals(Display.getTitle(), "Minecraft 1.8.9")) {
                Display.setTitle("Minecraft 1.8.9");
            }
        });
    }
}
