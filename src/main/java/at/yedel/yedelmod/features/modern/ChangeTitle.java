package at.yedel.yedelmod.features.modern;



import at.yedel.yedelmod.config.YedelConfig;
import dev.deftu.omnicore.client.OmniClient;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent.ClientDisconnectionFromServerEvent;
import org.lwjgl.opengl.Display;

import java.util.Objects;



public class ChangeTitle {
    private ChangeTitle() {}

    private static final ChangeTitle instance = new ChangeTitle();

    public static ChangeTitle getInstance() {
        return instance;
    }

    @SubscribeEvent
    public void onServerJoin(FMLNetworkEvent.ClientConnectedToServerEvent event) {
        if (YedelConfig.getInstance().changeWindowTitle) {
            OmniClient.getInstance().addScheduledTask(() -> {
                if (event.isLocal) {
                    Display.setTitle("Minecraft 1.8.9 - Singleplayer");
                    return;
                }
                ServerData serverData = OmniClient.getInstance().getCurrentServerData();
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
        OmniClient.getInstance().addScheduledTask(() -> {
            if (YedelConfig.getInstance().changeWindowTitle || !Objects.equals(Display.getTitle(), "Minecraft 1.8.9")) {
                Display.setTitle("Minecraft 1.8.9");
            }
        });
    }
}
