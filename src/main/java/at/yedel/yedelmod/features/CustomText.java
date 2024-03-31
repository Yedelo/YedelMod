package at.yedel.yedelmod.features;



import at.yedel.yedelmod.config.YedelConfig;
import at.yedel.yedelmod.gui.MoveTextGui;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import static at.yedel.yedelmod.YedelMod.minecraft;



public class CustomText {
    public static final CustomText instance = new CustomText();
    public String displayedText = YedelConfig.displayedText;
    public int x = YedelConfig.displayX;
    public int y = YedelConfig.displayY;
    public boolean shouldDisplay = true;

    public final void setShouldDisplay(boolean shouldDisplay) {
        this.shouldDisplay = shouldDisplay;
    }

    @SubscribeEvent
    public void onRenderGame(RenderGameOverlayEvent.Text event) {
        if (shouldDisplay) {
            minecraft.fontRendererObj.drawStringWithShadow(displayedText, x, y, 0xffffffff);
        }
    }

    @SubscribeEvent
    public void onOpenMoveTextGUI(GuiOpenEvent event) {
        if (event.gui instanceof MoveTextGui) {
            instance.setShouldDisplay(false);
        }
    }
}
