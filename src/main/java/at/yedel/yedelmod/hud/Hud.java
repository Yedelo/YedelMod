package at.yedel.yedelmod.hud;



import java.awt.Color;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;

import static at.yedel.yedelmod.YedelMod.minecraft;



public abstract class Hud extends Gui {
	public Hud(int x, int y, int defaultX, int defaultY) {
		this.x = x;
		this.y = y;
		this.defaultX = defaultX;
		this.defaultY = defaultY;
	}

	protected int x;

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	protected int y;

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	private final int defaultX;
	private final int defaultY;
	protected int width;

	/**
	 * Returns the width of this HUD. Used for keeping the HUD inside the screen in the move hud gui.
	 *
	 * @return the width of this HUD
	 */
	public int getWidth() {
		return width;
	}

	protected int height;

	/**
	 * Returns the height of this HUD. Used for keeping the HUD inside the screen in the move hud gui.
	 *
	 * @return the height of this HUD
	 */
	public int getHeight() {
		return height;
	}

	protected FontRenderer fontRenderer = minecraft.fontRendererObj;
	protected final int WHITE = Color.WHITE.getRGB();

	/**
	 * Used to determine whether the HUD should render. Called in HudManager, do not use this in render().
	 * This should link to a config field.
	 *
	 * @return whether the HUD should render
	 */
	public abstract boolean shouldRender();

	/**
	 * Called when the HUD is rendered in-game.
	 */
	public abstract void render();

	/**
	 * Renders a sample of this HUD for the move hud gui.
	 * This should have a background or another indicator showing if it's selected.
	 *
	 * @param selected whether the HUD is currently being selected
	 */
	public abstract void renderSample(boolean selected);

	/**
	 * Used to determine if the HUD is being hovered over in the move hud gui.
	 *
	 * @param mouseX the current mouse X position
	 * @param mouseY the current mouse Y position
	 *
	 * @return whether the HUD is being hovered over
	 */
	public boolean isHovered(int mouseX, int mouseY) {
		return mouseX >= x && mouseY >= y && mouseX <= x + width && mouseY <= y + height;
	}

	/**
	 * Draws a simple white outline.
	 * Call this after setting the width and height in render / renderSample.
	 *
	 * @param distance the distance of the outline from the main content
	 */
	protected void drawOutline(int distance) {
		drawHorizontalLine(x - distance, x + width + distance, y - distance, WHITE);
		drawVerticalLine(x + width + distance, y - distance, y + height + distance, WHITE);
		drawHorizontalLine(x + width + distance, x - distance, y + height + distance, WHITE);
		drawVerticalLine(x - distance, y + height + distance, y - distance, WHITE);
	}

	/**
	 * Called when the HUD is reset (with the R key in move hud gui)
	 */
	public void onReset() {
		x = defaultX;
		y = defaultY;
	}

	/**
	 * Called when the HUD is moved in the move hud gui.
	 * Should be used to save config values.
	 */
	public abstract void onUpdate();
}
