package at.yedel.yedelmod.features;



import at.yedel.yedelmod.config.YedelConfig;
import at.yedel.yedelmod.gui.MoveTextGui;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import static at.yedel.yedelmod.YedelMod.minecraft;



public class CustomText {
    private static final CustomText instance = new CustomText();

    public static CustomText getInstance() {
        return instance;
    }

    private String displayedText = YedelConfig.getInstance().displayedText;

    public String getDisplayedText() {
        return displayedText;
    }

    public void setDisplayedText(String displayedText) {
        this.displayedText = displayedText;
    }

    private int x = YedelConfig.getInstance().displayX;

    public void setX(int x) {
        this.x = x;
    }

    private int y = YedelConfig.getInstance().displayY;

    public void setY(int y) {
        this.y = y;
    }

    private boolean shouldDisplay = true;

    public void setShouldDisplay(boolean shouldDisplay) {
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
