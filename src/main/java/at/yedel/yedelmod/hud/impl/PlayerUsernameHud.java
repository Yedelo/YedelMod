package at.yedel.yedelmod.hud.impl;



import java.awt.Color;

import at.yedel.yedelmod.hud.Hud;



public class PlayerUsernameHud extends Hud {
	private PlayerUsernameHud(int x, int y, int defaultX, int defaultY) {
		super(x, y, defaultX, defaultY);
	}

	private static final PlayerUsernameHud instance = new PlayerUsernameHud(5, 5, 5, 5);

	public static PlayerUsernameHud getInstance() {
		return instance;
	}

	private static final int WHITE = Color.WHITE.getRGB();
	private static final int CYAN = new Color(62, 94, 112).getRGB();

	@Override
	public void render() {
		fontRenderer.drawStringWithShadow(minecraft.getSession().getUsername(), x, y, WHITE);
	}

	@Override
	public boolean shouldRender() {
		return true;
	}

	@Override
	public void renderSample(boolean beingDragged) {
		width = fontRenderer.getStringWidth("Username");
		height = fontRenderer.FONT_HEIGHT;
		if (beingDragged) drawRect(x, y, x + width, y + height, CYAN);
		fontRenderer.drawStringWithShadow("Username", x, y, WHITE);
	}

	@Override
	public void onUpdate() {
		System.out.println("Updated, do some config stuff here");
		System.out.println("- new x: " + x);
		System.out.println("- new y: " + y);
	}
}
