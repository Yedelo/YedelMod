package at.yedel.yedelmod.features.major;



import java.awt.Color;
import java.util.ArrayList;
import java.util.Objects;

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
    public static DefusalHelper instance = new DefusalHelper();
    public static boolean inDefusal;
    private final int red = new Color(246, 94, 94, 255).getRGB();
    private final ArrayList<Slot> clickedSlots = new ArrayList<>();

    @SubscribeEvent
    public void onOpenDefusalWindow(PacketEvent.ReceiveEvent event) {
        if (YedelConfig.defusalHelper) {
            if (event.packet instanceof S2DPacketOpenWindow) {
                if (Objects.equals(((S2DPacketOpenWindow) (event.packet)).getWindowTitle().getUnformattedText(), "C4 (Click REDSTONE)")) {
                    inDefusal = true;
                    clickedSlots.clear();
                }
            }
        }
    }

    @SubscribeEvent
    public void onResetItems(PacketEvent.ReceiveEvent event) {
        if (!YedelConfig.defusalHelper) return;
        if (event.packet instanceof S2FPacketSetSlot) {
            if (minecraft.currentScreen instanceof GuiContainer) {
                if (((GuiContainer) minecraft.currentScreen).inventorySlots.getSlot(0).inventory.getName().contains("REDSTONE")) {
                    inDefusal = true;
                    clickedSlots.clear();
                }
            }
        }
    }

    @SubscribeEvent
    public void onRenderRedstones(DrawSlotEvent event) {
        if (!YedelConfig.defusalHelper) return;
        Slot slot = event.slotIn;
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
