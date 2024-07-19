package at.yedel.yedelmod.utils;



import at.yedel.yedelmod.utils.typeutils.NumberUtils;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import static at.yedel.yedelmod.YedelMod.minecraft;



public class InventoryClicker {
    private static final InventoryClicker instance = new InventoryClicker();

    public static InventoryClicker getInstance() {
        return instance;
    }

    private int slot;

    public void setSlot(int slot) {
        this.slot = slot;
    }

    @SubscribeEvent
    public void onOpenAtlas(GuiOpenEvent event) {
        if (event.gui instanceof GuiContainer) {
            EntityPlayerSP player = minecraft.thePlayer;
            ThreadManager.scheduleOnce(() -> {
                minecraft.playerController.windowClick(player.openContainer.windowId, slot, 0, 0, player);
            }, (int) NumberUtils.randomRange(300, 400));
            MinecraftForge.EVENT_BUS.unregister(this);
        }
    }

    public void setupTimeout() { // Unregisters this after 1.5s so that in case of error, it doesn't randomly click the next inventory
        ThreadManager.scheduleOnce(() -> MinecraftForge.EVENT_BUS.unregister(this), 1500);
    }
}
