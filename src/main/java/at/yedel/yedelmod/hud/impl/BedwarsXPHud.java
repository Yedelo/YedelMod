package at.yedel.yedelmod.hud.impl;



import at.yedel.yedelmod.config.YedelConfig;
import at.yedel.yedelmod.features.major.BedwarsFeatures;
import at.yedel.yedelmod.handlers.HypixelManager;
import at.yedel.yedelmod.hud.Hud;



public class BedwarsXPHud extends Hud {
	private BedwarsXPHud(int x, int y, int defaultX, int defaultY) {
		super(x, y, defaultX, defaultY);
		height = fontRenderer.FONT_HEIGHT;
	}

	private static final BedwarsXPHud instance = new BedwarsXPHud(YedelConfig.getInstance().xpDisplayX, YedelConfig.getInstance().xpDisplayY, 5, 15);

	public static BedwarsXPHud getInstance() {
		return instance;
	}

	@Override
	public boolean shouldRender() {
		return YedelConfig.getInstance().xpDisplay && HypixelManager.getInstance().getInBedwars();
	}

	@Override
	public void render() {
		if (BedwarsFeatures.getInstance().hasExperience()) {
			fontRenderer.drawStringWithShadow(BedwarsFeatures.getInstance().getHudXPText(), x, y, WHITE);
		}
	}

	@Override
	public void renderSample(boolean selected) {
		width = fontRenderer.getStringWidth("XP: 3,550/5,000");
		if (selected) drawOutline(3);
		fontRenderer.drawStringWithShadow("XP: §b3,550§7/§a5,000", x, y, WHITE);
	}

	@Override
	public void update() {
		YedelConfig.getInstance().xpDisplayX = x;
		YedelConfig.getInstance().xpDisplayY = y;
		YedelConfig.getInstance().save();
	}
}
