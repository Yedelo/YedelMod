package at.yedel.yedelmod.features.modern;



import java.util.Objects;

import at.yedel.yedelmod.config.YedelConfig;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;
import org.lwjgl.opengl.Display;

import static at.yedel.yedelmod.YedelMod.minecraft;



public class ChangeTitle {
    private boolean setDisplay = false;
    private boolean local;

    @SubscribeEvent
    public void onServerJoin(FMLNetworkEvent.ClientConnectedToServerEvent event) {
        if (!YedelConfig.changeTitle) return;
        setDisplay = true;
        local = event.isLocal;
    }

    @SubscribeEvent
    public void onRenderGame(RenderGameOverlayEvent event) {
        if (!setDisplay) return;
        setDisplay = false;
        if (local) Display.setTitle("Minecraft 1.8.9 - Singleplayer");
        else {
            ServerData serverData = minecraft.getCurrentServerData();
            if (Objects.equals(serverData.serverName, "Minecraft Server")) { // Direct connect
                Display.setTitle("Minecraft 1.8.9 - " + serverData.serverIP);
            }
            else {
                Display.setTitle("Minecraft 1.8.9 - " + serverData.serverName + " - " + serverData.serverIP);
            }
        }
    }
}
