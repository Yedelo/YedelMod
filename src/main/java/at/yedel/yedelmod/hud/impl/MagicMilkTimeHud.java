package at.yedel.yedelmod.hud.impl;



import at.yedel.yedelmod.config.YedelConfig;
import at.yedel.yedelmod.features.major.BedwarsFeatures;
import at.yedel.yedelmod.handlers.HypixelManager;
import at.yedel.yedelmod.hud.Hud;



public class MagicMilkTimeHud extends Hud {
	private MagicMilkTimeHud(int x, int y, int defaultX, int defaultY) {
		super(x, y, defaultX, defaultY);
		height = 8;
	}

	private static final MagicMilkTimeHud instance = new MagicMilkTimeHud(YedelConfig.getInstance().magicMilkDisplayX, YedelConfig.getInstance().magicMilkDisplayY, 5, 25);

	public static MagicMilkTimeHud getInstance() {
		return instance;
	}

	@Override
	public boolean shouldRender() {
		return YedelConfig.getInstance().magicMilkDisplay && HypixelManager.getInstance().isInBedwars();
	}

	@Override
	public void render() {
		if (BedwarsFeatures.getInstance().getMagicMilkTime() > -1) {
			fontRenderer.drawStringWithShadow(BedwarsFeatures.getInstance().getMagicMilkTimeText(), x, y, WHITE);
		}
	}

	@Override
	public void renderSample(boolean selected) {
		String text = "Magic Milk: §b25§as";
		width = fontRenderer.getStringWidth(text);
		if (selected) drawOutline(3);
		fontRenderer.drawStringWithShadow(text, x, y, WHITE);
	}

	@Override
	public void update() {
		YedelConfig.getInstance().magicMilkDisplayX = x;
		YedelConfig.getInstance().magicMilkDisplayY = y;
	}
}
