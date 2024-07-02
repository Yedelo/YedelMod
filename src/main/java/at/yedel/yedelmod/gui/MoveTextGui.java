package at.yedel.yedelmod.gui;



import java.io.IOException;

import at.yedel.yedelmod.config.YedelConfig;
import at.yedel.yedelmod.features.CustomText;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import org.lwjgl.input.Keyboard;

import static at.yedel.yedelmod.YedelMod.minecraft;



public class MoveTextGui extends GuiScreen {
    private final GuiScreen parentScreen;

    private int currentX = YedelConfig.getInstance().displayX;
    private int currentY = YedelConfig.getInstance().displayY;

    private GuiButton reset;
    private GuiButton close;
    private GuiButton centerHorizontally;
    private GuiButton centerVertically;

    public MoveTextGui(GuiScreen parentScreen) {
        this.parentScreen = parentScreen;
    }

    @Override
    public void initGui() {
        this.buttonList.add(reset = new GuiButton(0, (width / 2) - 50, 40, 100, 20, "Reset (R)"));
        this.buttonList.add(close = new GuiButton(1, width - 30, 10, 20, 20, "X"));
        this.buttonList.add(centerHorizontally = new GuiButton(2, width - 140, height - 29, 131, 20, "Center Horizontally (H)"));
        this.buttonList.add(centerVertically = new GuiButton(3, width - 140, height - 53, 131, 20, "Center Vertically (V)"));
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        int partialMouseX = (int) (mouseX + partialTicks);
        int partialMouseY = (int) (mouseY + partialTicks);
        String mouseText = "(" + mouseX + ", " + mouseY + ")";

        drawDefaultBackground();
        drawString(fontRendererObj, "Move your mouse to where you want to place the text, then click to set it.", (int) (width / 2 - 185.5), 25, 0xffffffff);
        drawString(fontRendererObj, mouseText, partialMouseX - (fontRendererObj.getStringWidth(mouseText)) / 2, partialMouseY + 15, 0xffffffff);
        drawString(fontRendererObj, "Width of screen: " + width, 10, height - 32, 0xffffffff);
        drawString(fontRendererObj, "Height of screen: " + height, 10, height - 16, 0xffffffff);
        drawString(fontRendererObj, YedelConfig.getInstance().displayedText, currentX, currentY, 0xffffffff);
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        if (mouseButton == 0) {
            currentX = mouseX;
            currentY = mouseY;
        }
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        if (button == reset) {
            currentX = 5;
            currentY = 5;
        }
        else if (button == close) {
            minecraft.displayGuiScreen(parentScreen);
        }
        else if (button == centerHorizontally) {
            currentX = (width - fontRendererObj.getStringWidth(CustomText.getInstance().getDisplayedText())) / 2;
        }
        else if (button == centerVertically) {
            currentY = height / 2;
        }
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) {
        if (keyCode == Keyboard.KEY_ESCAPE) mc.displayGuiScreen(parentScreen);
        else if (typedChar == 'r') {
            currentX = 5;
            currentY = 5;
        }
        else if (typedChar == 'v') {
            currentY = height / 2;
        }
        else if (typedChar == 'h') {
            currentX = (width - fontRendererObj.getStringWidth(CustomText.getInstance().getDisplayedText())) / 2;
        }
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

    @Override
    public void onGuiClosed() {
        CustomText.getInstance().setShouldDisplay(true);
        CustomText.getInstance().setX(currentX);
        CustomText.getInstance().setY(currentY);
        YedelConfig.getInstance().displayX = currentX;
        YedelConfig.getInstance().displayY = currentY;
        YedelConfig.getInstance().save();
    }
}
