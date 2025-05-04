package at.yedel.yedelmod.features.modern;



import at.yedel.yedelmod.config.YedelConfig;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreenBook;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;



public class DrawBookBackground {
    private static final DrawBookBackground INSTANCE = new DrawBookBackground();

    public static DrawBookBackground getInstance() {
        return INSTANCE;
    }

    private DrawBookBackground() {}

    @SubscribeEvent
    public void renderBookBackground(GuiScreenEvent.DrawScreenEvent.Pre event) {
        Gui gui = event.gui;
        if (YedelConfig.getInstance().bookBackground && gui instanceof GuiScreenBook) {
            ((GuiScreenBook) gui).drawWorldBackground(1);
        }
    }
}
