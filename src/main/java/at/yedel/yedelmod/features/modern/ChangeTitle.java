package at.yedel.yedelmod.features.modern;



import java.util.Objects;

import at.yedel.yedelmod.config.YedelConfig;
import at.yedel.yedelmod.handlers.LogListenerFilter.Log4JEvent;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent.ClientDisconnectionFromServerEvent;
import org.lwjgl.opengl.Display;

import static at.yedel.yedelmod.YedelMod.minecraft;



public class ChangeTitle {
    private ChangeTitle() {}
    private static final ChangeTitle instance = new ChangeTitle();

    public static ChangeTitle getInstance() {
        return instance;
    }

    private boolean setDisplay = false;
    private boolean local;

    @SubscribeEvent
    public void onServerJoin(FMLNetworkEvent.ClientConnectedToServerEvent event) {
        if (YedelConfig.getInstance().changeTitle) {
            setDisplay = true;
            local = event.isLocal;
        }
    }

    @SubscribeEvent
    public void onRenderGame(RenderGameOverlayEvent event) {
        if (!setDisplay) return;
        setDisplay = false;
        if (local) {
            Display.setTitle("Minecraft 1.8.9 - Singleplayer");
            return;
        }
        ServerData serverData = minecraft.getCurrentServerData();
        if (Objects.equals(serverData.serverName, "Minecraft Server")) { // Direct connect
            Display.setTitle("Minecraft 1.8.9 - " + serverData.serverIP);
        }
        else {
            Display.setTitle("Minecraft 1.8.9 - " + serverData.serverName + " - " + serverData.serverIP);
        }
    }

    @SubscribeEvent
    public void onDisconnectFromServer(ClientDisconnectionFromServerEvent event) {
        minecraft.addScheduledTask(() -> {
            if (YedelConfig.getInstance().changeTitle || !Objects.equals(Display.getTitle(), "Minecraft 1.8.9")) {
                Display.setTitle("Minecraft 1.8.9");
            }
        });
    }

    @SubscribeEvent
    public void onLanHost(Log4JEvent event) {
        if (!YedelConfig.getInstance().changeTitle) return;
        String message = event.getLogEvent().getMessage().getFormattedMessage();
        if (message.startsWith("Started on ")) {
            String port = message.substring(11);
            Display.setTitle("Minecraft 1.8.9 - LAN Singleplayer - " + port);
        }
    }
}
