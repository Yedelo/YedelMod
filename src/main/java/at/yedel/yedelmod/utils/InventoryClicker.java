package at.yedel.yedelmod.utils;



import java.util.concurrent.TimeUnit;

import at.yedel.yedelmod.utils.typeutils.NumberUtils;
import gg.essential.api.utils.Multithreading;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import static at.yedel.yedelmod.YedelMod.minecraft;



public class InventoryClicker {
    public static InventoryClicker instance = new InventoryClicker();
    public int slot;

    @SubscribeEvent
    public void onOpenAtlas(GuiOpenEvent event) {
        if (event.gui instanceof GuiContainer) {
            EntityPlayerSP player = minecraft.thePlayer;
            Multithreading.schedule(() -> {
                minecraft.playerController.windowClick(player.openContainer.windowId, slot, 0, 0, player);
            }, (long) NumberUtils.randomRange(300, 400), TimeUnit.MILLISECONDS);
            MinecraftForge.EVENT_BUS.unregister(this);
        }
    }

    public void setupTimeout() { // Unregisters this after 1.5s so that in case of error, it doesn't randomly click the next inventory
        Multithreading.schedule(() -> {
            MinecraftForge.EVENT_BUS.unregister(this);
        }, 1500, TimeUnit.MILLISECONDS);
    }
}
