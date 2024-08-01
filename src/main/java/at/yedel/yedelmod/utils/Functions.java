package at.yedel.yedelmod.utils;



import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import static at.yedel.yedelmod.YedelMod.minecraft;



public class Functions {
    private Functions() {}
    private static final Functions instance = new Functions();

    public static Functions getInstance() {
        return instance;
    }

    private Events events = new Events();

    public Events getEvents() {
        return events;
    }

    private static GuiScreen screenToOpen;

    public static void displayScreen(GuiScreen screen) {
        screenToOpen = screen;
    }

    public static void safelyPlaySound(String name, float volume, float pitch) {
        if (minecraft.thePlayer != null) minecraft.thePlayer.playSound(name, volume, pitch);
    }

    public static class Events {
        @SubscribeEvent
        public void onTickEnd(TickEvent event) {
            if (screenToOpen != null) {
                minecraft.displayGuiScreen(screenToOpen);
                screenToOpen = null;
            }
        }
    }
}

