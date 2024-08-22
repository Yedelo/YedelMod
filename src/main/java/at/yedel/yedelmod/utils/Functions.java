package at.yedel.yedelmod.utils;



import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.ResourceLocation;
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

    public static void playSound(String name, float pitch) {
        minecraft.getSoundHandler().playSound(PositionedSoundRecord.create(new ResourceLocation(name), pitch));
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

