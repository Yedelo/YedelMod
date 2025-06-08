package at.yedel.yedelmod.utils;



import cc.polyfrost.oneconfig.libs.universal.UGraphics;
import cc.polyfrost.oneconfig.libs.universal.UMinecraft;
import cc.polyfrost.oneconfig.platform.Platform;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;



public class RenderUtils {
    public static boolean shouldRenderSubinfo(EntityPlayer player) {
        return player.getDistanceSqToEntity(UMinecraft.getMinecraft().getRenderViewEntity()) < 100 && player.getWorldScoreboard().getObjectiveInDisplaySlot(2) != null;
    }

	public static void highlightItem(Slot slot, int color) {
        UGraphics.GL.translate(0, 0, 1);
        Platform.getGLPlatform().drawRect(slot.xDisplayPosition, slot.yDisplayPosition, slot.xDisplayPosition + 16, slot.yDisplayPosition + 16, color);
	}
}