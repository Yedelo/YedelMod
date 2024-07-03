package at.yedel.yedelmod.features.major;



import java.awt.Color;

import at.yedel.yedelmod.config.YedelConfig;
import at.yedel.yedelmod.events.DrawSlotEvent;
import at.yedel.yedelmod.events.PacketEvent;
import at.yedel.yedelmod.utils.typeutils.RenderUtils;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.init.Items;
import net.minecraft.inventory.Slot;
import net.minecraft.network.play.server.S2DPacketOpenWindow;
import net.minecraft.network.play.server.S2FPacketSetSlot;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import static at.yedel.yedelmod.YedelMod.minecraft;



public class DefusalHelper {
    private static final DefusalHelper instance = new DefusalHelper();

    public static DefusalHelper getInstance() {
        return instance;
    }

    public static boolean inDefusal;
    private final int red = new Color(246, 94, 94, 255).getRGB();

    @SubscribeEvent
    public void onOpenDefusalWindow(PacketEvent.ReceiveEvent event) {
        if (YedelConfig.getInstance().defusalHelper) {
            if (event.getPacket() instanceof S2DPacketOpenWindow) {
                if (((S2DPacketOpenWindow) (event.getPacket())).getWindowTitle().getUnformattedText().equals("C4 (Click REDSTONE)")) {
                    inDefusal = true;
                }
            }
        }
    }

    @SubscribeEvent
    public void onResetItems(PacketEvent.ReceiveEvent event) {
        if (!YedelConfig.getInstance().defusalHelper) return;
        if (event.getPacket() instanceof S2FPacketSetSlot) {
            if (minecraft.currentScreen instanceof GuiContainer) {
                if (((GuiContainer) minecraft.currentScreen).inventorySlots.getSlot(0).inventory.getName().contains("REDSTONE")) {
                    inDefusal = true;
                }
            }
        }
    }

    @SubscribeEvent
    public void onRenderRedstones(DrawSlotEvent event) {
        if (!YedelConfig.getInstance().defusalHelper) return;
        Slot slot = event.getSlot();
        if (slot.getStack() == null) return;
        if (slot.getStack().getItem() == Items.redstone && inDefusal) {
            RenderUtils.highlightItem(slot, red);
        }
    }

    @SubscribeEvent
    public void onGuiClosed(GuiOpenEvent event) {
        if (event.gui == null) inDefusal = false;
    }
}
