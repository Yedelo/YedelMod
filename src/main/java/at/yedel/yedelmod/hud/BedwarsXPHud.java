package at.yedel.yedelmod.hud;


import at.yedel.yedelmod.features.major.BedwarsFeatures;
import at.yedel.yedelmod.handlers.HypixelManager;
import cc.polyfrost.oneconfig.hud.SingleTextHud;



public class BedwarsXPHud extends SingleTextHud {
    private BedwarsXPHud() {
        super("Bedwars XP Hud", true, 5, 15);
	}

    private static final BedwarsXPHud instance = new BedwarsXPHud();

	public static BedwarsXPHud getInstance() {
		return instance;
	}

	@Override
    protected boolean shouldShow() {
        return super.shouldShow() && HypixelManager.getInstance().isInBedwars() && BedwarsFeatures.getInstance().hasExperience();
	}

	@Override
    protected String getText(boolean example) {
        if (example) return "XP: §b3,550§7/§a5,000";
        else return BedwarsFeatures.getInstance().getHudXPText();
	}
}
