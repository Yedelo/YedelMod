package at.yedel.yedelmod.utils;



import at.yedel.yedelmod.utils.typeutils.TextUtils;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import static at.yedel.yedelmod.YedelMod.minecraft;



public class Functions {
    private static final Functions instance = new Functions();

    public static Functions getInstance() {
        return instance;
    }

    private Events events = new Events();

    public Events getEvents() {
        return events;
    }

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

    private class Events {
        @SubscribeEvent
        public void onTickEnd(TickEvent event) {
            if (screenToOpen != null) {
                minecraft.displayGuiScreen(screenToOpen);
                screenToOpen = null;
            }
        }
    }
}

