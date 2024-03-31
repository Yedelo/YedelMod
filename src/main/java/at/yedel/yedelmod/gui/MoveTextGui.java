package at.yedel.yedelmod.gui;



import java.io.IOException;

import at.yedel.yedelmod.config.YedelConfig;
import at.yedel.yedelmod.features.CustomText;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;

import static at.yedel.yedelmod.YedelMod.minecraft;



public class MoveTextGui extends GuiScreen {
    public static final MoveTextGui instance = new MoveTextGui();
    private int currentX = YedelConfig.displayX;
    private int currentY = YedelConfig.displayY;

    private GuiButton reset;
    private GuiButton close;
    private GuiButton centerHorizontally;
    private GuiButton centerVertically;

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

    @Override
    public void initGui() {
        super.initGui();
        reset = new GuiButton(0, (width / 2) - 50, 40, 100, 20, "Reset (R)");
        close = new GuiButton(0, width - 30, 10, 20, 20, "X");
        centerHorizontally = new GuiButton(0, width - 140, height - 29, 131, 20, "Center Horizontally (H)");
        centerVertically = new GuiButton(0, width - 140, height - 53, 131, 20, "Center Vertically (V)");

        this.buttonList.add(reset);
        this.buttonList.add(close);
        this.buttonList.add(centerHorizontally);
        this.buttonList.add(centerVertically);
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
        drawString(fontRendererObj, YedelConfig.displayedText, currentX, currentY, 0xffffffff);
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (reset.isMouseOver()) {
            currentX = 5;
            currentY = 5;
        }
        else if (close.isMouseOver()) {
            minecraft.displayGuiScreen(null);
        }
        else if (centerHorizontally.isMouseOver()) {
            currentX = (width - fontRendererObj.getStringWidth(CustomText.instance.displayedText)) / 2;
        }
        else if (centerVertically.isMouseOver()) {
            currentY = height / 2;
        }
        else if (mouseButton == 0) {
            currentX = mouseX;
            currentY = mouseY;
        }
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        super.keyTyped(typedChar, keyCode);
        if (typedChar == 'r') {
            currentX = 5;
            currentY = 5;
        }
        else if (typedChar == 'v') {
            currentY = height / 2;
        }
        else if (typedChar == 'h') {
            currentX = (width - fontRendererObj.getStringWidth(CustomText.instance.displayedText)) / 2;
        }
    }

    @Override
    public void onGuiClosed() {
        CustomText.instance.setShouldDisplay(true);
        CustomText.instance.x = currentX;
        CustomText.instance.y = currentY;
        YedelConfig.displayX = currentX;
        YedelConfig.displayY = currentY;
        YedelConfig.save();
    }
}
