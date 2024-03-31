package at.yedel.yedelmod.features.major;



import java.awt.Color;
import java.util.ArrayList;

import at.yedel.yedelmod.config.YedelConfig;
import at.yedel.yedelmod.events.DrawSlotEvent;
import at.yedel.yedelmod.events.GuiContainerClickEvent;
import at.yedel.yedelmod.events.GuiContainerKeyEvent;
import at.yedel.yedelmod.events.PacketEvent;
import at.yedel.yedelmod.utils.typeutils.RenderUtils;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.init.Items;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.server.S2FPacketSetSlot;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.input.Keyboard;

import static at.yedel.yedelmod.YedelMod.minecraft;



public class DefusalHelper {
    public static boolean inDefusal;
    private final int red = new Color(246, 94, 94, 255).getRGB();
    private final ArrayList<Slot> clickedSlots = new ArrayList<>();
    private GuiContainer container;

    @SubscribeEvent
    public void onOpenDefusalWindow(PacketEvent.ReceiveEvent event) {
        if (YedelConfig.defusalHelper) {
            if (event.packet instanceof net.minecraft.network.play.server.S2DPacketOpenWindow) {
                if (java.util.Objects.equals(((net.minecraft.network.play.server.S2DPacketOpenWindow) (event.packet)).getWindowTitle().getUnformattedText(), "C4 (Click REDSTONE)")) {
                    inDefusal = true;
                    clickedSlots.clear();
                    container = (net.minecraft.client.gui.inventory.GuiContainer) minecraft.currentScreen;
                }
            }
        }
        else {
        }
    }

    @SubscribeEvent
    public void onResetItems(PacketEvent.ReceiveEvent event) {
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
    public void onHotkeyNonRedstone(GuiContainerKeyEvent event) {
        if (!YedelConfig.defusalHelper) return;
        if (Keyboard.isKeyDown(Keyboard.KEY_ESCAPE) || container == null) return;
        Slot slotUnderMouse = container.getSlotUnderMouse();
        if (slotUnderMouse == null) return;
        ItemStack stackUnderMouse = slotUnderMouse.getStack();
        if (stackUnderMouse == null) return;
        if ((stackUnderMouse.getItem() != Items.redstone && inDefusal) || clickedSlots.contains(slotUnderMouse)) {
            event.ci.cancel();
        }
        clickedSlots.add(slotUnderMouse);
    }

    @SubscribeEvent
    public void onClickNonRedstone(GuiContainerClickEvent event) {
        if (!YedelConfig.defusalHelper) return;
        if (event.slotIn == null) return;
        ItemStack stackUnderMouse = event.slotIn.getStack();
        if (stackUnderMouse == null) return;
        if ((stackUnderMouse.getItem() != Items.redstone && inDefusal) || clickedSlots.contains(event.slotIn)) {
            event.ci.cancel();
            clickedSlots.add(event.slotIn);
        }
    }

    @SubscribeEvent
    public void onGuiClosed(GuiOpenEvent event) {
        if (event.gui == null) inDefusal = false;
    }
}
