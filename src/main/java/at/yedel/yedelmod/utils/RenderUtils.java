package at.yedel.yedelmod.utils;



import cc.polyfrost.oneconfig.libs.universal.UMinecraft;
import net.minecraft.entity.player.EntityPlayer;



public class RenderUtils {
    public static boolean shouldRenderSubinfo(EntityPlayer player) {
        return player.getDistanceSqToEntity(UMinecraft.getMinecraft().getRenderViewEntity()) < 100 && player.getWorldScoreboard().getObjectiveInDisplaySlot(2) != null;
    }
}