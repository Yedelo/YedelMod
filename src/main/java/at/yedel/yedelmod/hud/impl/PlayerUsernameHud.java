package at.yedel.yedelmod.hud.impl;



import java.awt.Color;

import at.yedel.yedelmod.hud.Hud;

import static at.yedel.yedelmod.YedelMod.minecraft;



public class PlayerUsernameHud extends Hud {
	private static final int WHITE = Color.WHITE.getRGB();
	private static final int CYAN = new Color(62, 94, 112).getRGB();

	public PlayerUsernameHud(int x, int y, int defaultX, int defaultY) {
		super(x, y, defaultX, defaultY);
	}

	@Override
	public void render() {
		minecraft.fontRendererObj.drawStringWithShadow(minecraft.getSession().getUsername(), x, y, WHITE);
	}

	@Override
	public boolean shouldRender() {
		return true;
	}

	@Override
	public void renderSample(boolean beingDragged) {
		width = minecraft.fontRendererObj.getStringWidth("Username");
		height = minecraft.fontRendererObj.FONT_HEIGHT;
		if (beingDragged) drawRect(x, y, x + width, y + height, CYAN);
		minecraft.fontRendererObj.drawStringWithShadow("Username", x, y, WHITE);
	}

	@Override
	public void onUpdate() {
		System.out.println("Updated, do some config stuff here");
		System.out.println("- new x: " + x);
		System.out.println("- new y: " + y);
	}
}
