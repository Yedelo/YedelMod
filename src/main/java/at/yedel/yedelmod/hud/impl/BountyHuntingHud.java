package at.yedel.yedelmod.hud.impl;



import java.util.ArrayList;
import java.util.List;

import at.yedel.yedelmod.config.YedelConfig;
import at.yedel.yedelmod.features.major.TNTTagFeatures;
import at.yedel.yedelmod.hud.Hud;



public class BountyHuntingHud extends Hud {
	private BountyHuntingHud(int x, int y, int defaultX, int defaultY) {
		super(x, y, defaultX, defaultY);
	}

	private static final BountyHuntingHud instance = new BountyHuntingHud(YedelConfig.getInstance().bhDisplayX, YedelConfig.getInstance().bhDisplayY, 5, 25);

	public static BountyHuntingHud getInstance() {
		return instance;
	}

	private final List<String> lines = new ArrayList<String>();

	public List<String> getLines() {
		return lines;
	}

	@Override
	public void render() {
		fontRenderer.drawStringWithShadow(lines.get(0), x, y, WHITE);
		fontRenderer.drawStringWithShadow(lines.get(1), x, y + 11, WHITE);
		fontRenderer.drawStringWithShadow(lines.get(2), x, y + 22, WHITE);
		fontRenderer.drawStringWithShadow(lines.get(3), x, y + 33, WHITE);
	}

	@Override
	public boolean shouldRender() {
		return YedelConfig.getInstance().bountyHunting && TNTTagFeatures.getInstance().getPlayingTag();
	}

	@Override
	public void renderSample(boolean selected) {
		width = fontRenderer.getStringWidth("Your next target is Yedelos.");
		height = 42;
		if (selected) drawOutline(3);
		fontRenderer.drawStringWithShadow("§c§lBounty §f§lHunting", x, y, WHITE);
		fontRenderer.drawStringWithShadow("§a83 points", x, y + 11, WHITE);
		fontRenderer.drawStringWithShadow("§a15 kills", x, y + 22, WHITE);
		fontRenderer.drawStringWithShadow("§cYour next target is §aYedelos§c.", x, y + 33, WHITE);
	}

	@Override
	public void update() {
		YedelConfig.getInstance().bhDisplayX = x;
		YedelConfig.getInstance().bhDisplayY = y;
		YedelConfig.getInstance().save();
	}
}
