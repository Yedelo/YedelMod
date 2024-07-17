package at.yedel.yedelmod.utils.typeutils;



import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.inventory.Slot;



public class RenderUtils extends Gui {
	public static void highlightItem(Slot slot, int color) {
		GlStateManager.translate(0, 0, 1);
		drawRect(slot.xDisplayPosition, slot.yDisplayPosition, slot.xDisplayPosition + 16, slot.yDisplayPosition + 16, color);
	}
}