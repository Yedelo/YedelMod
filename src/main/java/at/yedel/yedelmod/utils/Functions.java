package at.yedel.yedelmod.utils;



import at.yedel.yedelmod.utils.typeutils.TextUtils;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import static at.yedel.yedelmod.YedelMod.minecraft;



public class Functions {
    public static Functions instance = new Functions();
    private static GuiScreen screenToOpen;
    public static String getScoreboardName() {
        try {
            return TextUtils.removeFormatting(minecraft.theWorld.getScoreboard().getObjectiveInDisplaySlot(1).getDisplayName());
        }
        catch (Exception nameIsNull) {return "";}
    }

    public static void displayScreen(GuiScreen screen) {
        screenToOpen = screen;
    }

    @SubscribeEvent
    public void onTickEnd(TickEvent event) {
        if (screenToOpen != null) {
            minecraft.displayGuiScreen(screenToOpen);
            screenToOpen = null;
        }
    }
}

