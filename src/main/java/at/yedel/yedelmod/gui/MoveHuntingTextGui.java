package at.yedel.yedelmod.gui;



import at.yedel.yedelmod.config.YedelConfig;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;

import static at.yedel.yedelmod.YedelMod.minecraft;



public class MoveHuntingTextGui extends GuiScreen {
    public static final MoveHuntingTextGui instance = new MoveHuntingTextGui();
    private final String[] lines = {
            "§c§lBounty §f§lHunting",
            "§a73 points",
            "§a22 kills",
            "§cYour next target is §aYedelos."
    };
    private GuiButton reset;
    private GuiButton close;
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
        centerVertically = new GuiButton(0, width - 140, height - 29, 131, 20, "Center Vertically (V)");

        this.buttonList.add(reset);
        this.buttonList.add(close);
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

        int y = YedelConfig.bhDisplayY;
        for (String line: lines) {
            fontRendererObj.drawStringWithShadow(line, YedelConfig.bhDisplayX, y, 16777215);
            y += fontRendererObj.FONT_HEIGHT + 2;
        }

        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (reset.isMouseOver()) {
            YedelConfig.bhDisplayX = 5;
            YedelConfig.bhDisplayY = 5;
        }
        else if (close.isMouseOver()) {
            minecraft.displayGuiScreen(null);
        }
        else if (centerVertically.isMouseOver()) {
            int textHeight = 4 * (fontRendererObj.FONT_HEIGHT + 2);
            YedelConfig.bhDisplayY = (height / 2) - textHeight;
        }
        else if (mouseButton == 0) {
            YedelConfig.bhDisplayX = mouseX;
            YedelConfig.bhDisplayY = mouseY;
        }
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) {
        if (typedChar == 'r') {
            YedelConfig.bhDisplayX = 5;
            YedelConfig.bhDisplayY = 5;
        }
        else if (typedChar == 'v') {
            int textHeight = 4 * (fontRendererObj.FONT_HEIGHT + 2);
            YedelConfig.bhDisplayY = (height / 2) - textHeight;
        }
        else if (keyCode == 1) {
            minecraft.displayGuiScreen(null);
        }
    }

    @Override
    public void onGuiClosed() {
        YedelConfig.save();
    }
}
