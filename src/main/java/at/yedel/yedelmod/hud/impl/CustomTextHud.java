package at.yedel.yedelmod.hud.impl;



import java.awt.Color;

import at.yedel.yedelmod.config.YedelConfig;
import at.yedel.yedelmod.hud.Hud;



public class CustomTextHud extends Hud {
	private CustomTextHud(int x, int y, int defaultX, int defaultY) {
		super(x, y, defaultX, defaultY);
	}

	private static final CustomTextHud instance = new CustomTextHud(YedelConfig.getInstance().displayX, YedelConfig.getInstance().displayY, 5, 5);

	public static CustomTextHud getInstance() {
		return instance;
	}

	private static final int WHITE = Color.WHITE.getRGB();
	private static final int CYAN = new Color(62, 94, 112).getRGB();

	@Override
	public void render() {
		fontRenderer.drawStringWithShadow(YedelConfig.getInstance().displayedText, x, y, WHITE);
	}

	@Override
	public boolean shouldRender() {
		return YedelConfig.getInstance().displayTextToggled;
	}

	@Override
	public void renderSample(boolean beingDragged) {
		width = fontRenderer.getStringWidth("Example text");
		height = fontRenderer.FONT_HEIGHT;
		if (beingDragged) drawRect(x, y, x + width, y + height, CYAN);
		fontRenderer.drawStringWithShadow("Example text", x, y, WHITE);
	}

	@Override
	public void onUpdate() {
		YedelConfig.getInstance().displayX = x;
		YedelConfig.getInstance().displayY = y;
		YedelConfig.getInstance().save();
	}
}
