package at.yedel.yedelmod.gui;



import java.io.IOException;

import at.yedel.yedelmod.config.YedelConfig;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import org.lwjgl.input.Keyboard;

import static at.yedel.yedelmod.YedelMod.minecraft;



public class MoveHuntingTextGui extends GuiScreen {
    private final GuiScreen parentScreen;

    private final String[] lines = {
            "§c§lBounty §f§lHunting",
            "§a73 points",
            "§a22 kills",
            "§cYour next target is §aYedelos."
    };
    private GuiButton reset;
    private GuiButton close;
    private GuiButton centerVertically;

    public MoveHuntingTextGui(GuiScreen parentScreen) {
        this.parentScreen = parentScreen;
    }

    @Override
    public void initGui() {
        this.buttonList.add(reset = new GuiButton(0, (width / 2) - 50, 40, 100, 20, "Reset (R)"));
        this.buttonList.add(close = new GuiButton(0, width - 30, 10, 20, 20, "X"));
        this.buttonList.add(centerVertically = new GuiButton(0, width - 140, height - 29, 131, 20, "Center Vertically (V)"));
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        int partialMouseX = (int) (mouseX + partialTicks);
        int partialMouseY = (int) (mouseY + partialTicks);
        String mouseText = "(" + mouseX + ", " + mouseY + ")";

        drawDefaultBackground();
        drawString(fontRendererObj, "Move your mouse to where you want to place the text, then click to set it.", (int) (width / 2 - 185.5), 25, 0xffffffff);
        drawString(fontRendererObj, mouseText, partialMouseX - (fontRendererObj.getStringWidth(mouseText)) / 2, partialMouseY + 15, 0xffffffff);

        int y = YedelConfig.getInstance().bhDisplayY;
        for (String line: lines) {
            fontRendererObj.drawStringWithShadow(line, YedelConfig.getInstance().bhDisplayX, y, 16777215);
            y += fontRendererObj.FONT_HEIGHT + 2;
        }

        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        if (mouseButton == 0) {
            YedelConfig.getInstance().bhDisplayX = mouseX;
            YedelConfig.getInstance().bhDisplayY = mouseY;
        }
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        if (button == reset) {
            YedelConfig.getInstance().bhDisplayX = 5;
            YedelConfig.getInstance().bhDisplayY = 5;
        }
        else if (button == close) {
            minecraft.displayGuiScreen(parentScreen);
        }
        else if (button == centerVertically) {
            int textHeight = 4 * (fontRendererObj.FONT_HEIGHT + 2);
            YedelConfig.getInstance().bhDisplayY = (height / 2) - textHeight;
        }
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) {
        if (typedChar == 'r') {
            YedelConfig.getInstance().bhDisplayX = 5;
            YedelConfig.getInstance().bhDisplayY = 5;
        }
        else if (typedChar == 'v') {
            int textHeight = 4 * (fontRendererObj.FONT_HEIGHT + 2);
            YedelConfig.getInstance().bhDisplayY = (height / 2) - textHeight;
        }
        else if (keyCode == Keyboard.KEY_ESCAPE) {
            minecraft.displayGuiScreen(parentScreen);
        }
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

    @Override
    public void onGuiClosed() {
        YedelConfig.getInstance().save();
    }
}
