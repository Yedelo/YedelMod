package at.yedel.yedelmod.hud.impl;



import at.yedel.yedelmod.config.YedelConfig;
import at.yedel.yedelmod.handlers.HypixelManager;
import at.yedel.yedelmod.hud.Hud;
import at.yedel.yedelmod.utils.typeutils.TextUtils;
import net.minecraft.client.entity.EntityPlayerSP;

import static at.yedel.yedelmod.YedelMod.minecraft;



public class BedwarsXPHud extends Hud {
	private BedwarsXPHud(int x, int y, int defaultX, int defaultY) {
		super(x, y, defaultX, defaultY);
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
		EntityPlayerSP player = minecraft.thePlayer;
		if (player != null && player.experience != 0) {
			fontRenderer.drawStringWithShadow("XP: §b" + TextUtils.commafy((int) (player.experience * 5000)) + "§7/§a5,000", x, y, WHITE);
		}
	}

	@Override
	public void renderSample(boolean selected) {
		width = fontRenderer.getStringWidth("XP: 3,550/5,000");
		height = fontRenderer.FONT_HEIGHT;
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
