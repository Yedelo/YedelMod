package at.yedel.yedelmod.utils.typeutils;



import dev.deftu.omnicore.client.OmniScreen;
import dev.deftu.omnicore.client.render.OmniMatrixStack;
import net.minecraft.inventory.Slot;



public class RenderUtils {
	public static void highlightItem(Slot slot, int color) {
        OmniMatrixStack.vanilla().translate(0, 0, 1);
        OmniScreen.drawRect(slot.xDisplayPosition, slot.yDisplayPosition, slot.xDisplayPosition + 16, slot.yDisplayPosition + 16, color);
	}
}