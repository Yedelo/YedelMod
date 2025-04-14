package at.yedel.yedelmod.features.modern;



import at.yedel.yedelmod.config.YedelConfig;
import dev.deftu.omnicore.client.OmniClient;
import dev.deftu.omnicore.client.OmniClientMultiplayer;
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
            OmniClient.execute(() -> {
                if (event.isLocal) {
                    Display.setTitle("Minecraft 1.8.9 - Singleplayer");
                    return;
                }
                if (Objects.equals(OmniClientMultiplayer.getCurrentServerName(), "Minecraft Server")) { // Direct connect
                    Display.setTitle("Minecraft 1.8.9 - " + OmniClientMultiplayer.getCurrentServerAddress());
                }
                else {
                    Display.setTitle("Minecraft 1.8.9 - " + OmniClientMultiplayer.getCurrentServerName() + " - " + OmniClientMultiplayer.getCurrentServerAddress());
                }
            });
        }
    }

    @SubscribeEvent
    public void onDisconnectFromServer(ClientDisconnectionFromServerEvent event) {
        OmniClient.execute(() -> {
            if (YedelConfig.getInstance().changeWindowTitle || !Objects.equals(Display.getTitle(), "Minecraft 1.8.9")) {
                Display.setTitle("Minecraft 1.8.9");
            }
        });
    }
}
