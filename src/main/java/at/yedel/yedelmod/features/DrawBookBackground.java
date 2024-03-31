package at.yedel.yedelmod.features;



import at.yedel.yedelmod.config.YedelConfig;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreenBook;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;



public class DrawBookBackground {
    @SubscribeEvent
    public void onRenderBook(GuiScreenEvent.DrawScreenEvent.Pre event) {
        Gui gui = event.gui;
        if (YedelConfig.drawBookBackground && gui instanceof GuiScreenBook)
            ((GuiScreenBook) gui).drawWorldBackground(1);
    }
}
