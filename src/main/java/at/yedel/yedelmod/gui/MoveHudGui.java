package at.yedel.yedelmod.gui;



import java.awt.Color;

import at.yedel.yedelmod.hud.Hud;
import at.yedel.yedelmod.hud.HudManager;
import net.minecraft.client.gui.GuiScreen;
import org.lwjgl.input.Keyboard;

import static at.yedel.yedelmod.YedelMod.minecraft;



public class MoveHudGui extends GuiScreen {
	private final GuiScreen parentScreen;

	public MoveHudGui(GuiScreen parentScreen) {
		this.parentScreen = parentScreen;
	}

	private Hud selectedHud;
	private boolean dragging;
	private boolean keyGuideToggled;
	private static final int WHITE = Color.WHITE.getRGB();

	@Override
	public void initGui() {
		Keyboard.enableRepeatEvents(true);
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		drawDefaultBackground();
		if (selectedHud != null && dragging) {
			selectedHud.setX(
				Math.min(
					(int) (mouseX + partialTicks),
					width - selectedHud.getWidth()
				)
			);
			selectedHud.setY(
				Math.min(
					(int) (mouseY + partialTicks),
					height - selectedHud.getHeight()
				)
			);
		}
		fontRendererObj.drawStringWithShadow("§lQ §r- Toggle Key Guide", 5, height - 15, WHITE);
		if (keyGuideToggled) {
			fontRendererObj.drawStringWithShadow("§lW §r- Move 5 units up", 5, height - 95, WHITE);
			fontRendererObj.drawStringWithShadow("§lA §r- Move 5 units left", 5, height - 85, WHITE);
			fontRendererObj.drawStringWithShadow("§lS §r- Move 5 units down", 5, height - 75, WHITE);
			fontRendererObj.drawStringWithShadow("§lD §r- Move 5 units right", 5, height - 65, WHITE);
			fontRendererObj.drawStringWithShadow("§l↑ §r- Move 1 unit up", 5, height - 55, WHITE);
			fontRendererObj.drawStringWithShadow("§l← §r- Move 1 unit left", 5, height - 45, WHITE);
			fontRendererObj.drawStringWithShadow("§l↓ §r- Move 1 unit down", 5, height - 35, WHITE);
			fontRendererObj.drawStringWithShadow("§l→ §r- Move 1 unit right", 5, height - 25, WHITE);
		}
		// Render the other HUDs first, then the selected one (for layering)
		for (Hud hud: HudManager.getInstance().getHuds()) {
			if (hud != selectedHud) hud.renderSample(false);
		}
		if (selectedHud != null) selectedHud.renderSample(true);
	}

	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) {
		boolean clickedOnHud = false;
		for (Hud hud: HudManager.getInstance().getHuds()) {
			if (hud.isHovered(mouseX, mouseY)) {
				dragging = true;
				selectedHud = hud;
				clickedOnHud = true;
			}
		}
		if (!clickedOnHud) selectedHud = null;
	}

	@Override
	protected void mouseReleased(int mouseX, int mouseY, int state) {
		dragging = false;
	}

	@Override
	protected void keyTyped(char typedChar, int keyCode) {
		if (keyCode == Keyboard.KEY_ESCAPE) {
			minecraft.displayGuiScreen(parentScreen);
		}
		else if (keyCode == Keyboard.KEY_Q) {
			keyGuideToggled = !keyGuideToggled;
		}
		if (selectedHud == null) return;
		switch (keyCode) {
			case Keyboard.KEY_R:
				selectedHud.onReset();
				break;
			case Keyboard.KEY_W:
				selectedHud.setY(
					Math.max(
						selectedHud.getY() - 5,
						0
					)
				);
				break;
			case Keyboard.KEY_UP:
				selectedHud.setY(
					Math.max(
						selectedHud.getY() - 1,
						0
					)
				);
				break;
			case Keyboard.KEY_S:
				selectedHud.setY(
					Math.min(
						selectedHud.getY() + 5,
						height - selectedHud.getHeight()
					)
				);
				break;
			case Keyboard.KEY_DOWN:
				selectedHud.setY(
					Math.min(
						selectedHud.getY() + 1,
						height - selectedHud.getHeight()
					)
				);
				break;
			case Keyboard.KEY_A:
				selectedHud.setX(
					Math.max(
						selectedHud.getX() - 5,
						0
					)
				);
				break;
			case Keyboard.KEY_LEFT:
				selectedHud.setX(
					Math.max(
						selectedHud.getX() - 1,
						0
					)
				);
				break;
			case Keyboard.KEY_D:
				selectedHud.setX(
					Math.min(
						selectedHud.getX() + 5,
						width - selectedHud.getWidth()
					)
				);
				break;
			case Keyboard.KEY_RIGHT:
				selectedHud.setX(
					Math.min(
						selectedHud.getX() + 1,
						width - selectedHud.getWidth()
					)
				);
				break;
		}
	}

	@Override
	public void onGuiClosed() {
		Keyboard.enableRepeatEvents(false);
		if (selectedHud != null) selectedHud.onUpdate();
	}
}
