package at.yedel.yedelmod.gui;



import at.yedel.yedelmod.hud.Hud;
import at.yedel.yedelmod.hud.HudManager;
import net.minecraft.client.gui.GuiScreen;
import org.lwjgl.input.Keyboard;

import static at.yedel.yedelmod.YedelMod.minecraft;



public class MoveHudGui extends GuiScreen {
	private final GuiScreen parentScreen;
	private Hud selectedHud;
	private boolean dragging;

	public MoveHudGui(GuiScreen parentScreen) {
		this.parentScreen = parentScreen;
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
		for (Hud hud: HudManager.getInstance().getHuds()) {
			hud.renderSample(hud == selectedHud);
		}
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
		if (selectedHud != null) selectedHud.onUpdate();
	}
}
