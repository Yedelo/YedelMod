package at.yedel.yedelmod.hud;


import at.yedel.yedelmod.features.major.BedwarsFeatures;
import at.yedel.yedelmod.handlers.HypixelManager;
import cc.polyfrost.oneconfig.hud.SingleTextHud;



public class MagicMilkTimeHud extends SingleTextHud {
    private MagicMilkTimeHud() {
        super("Magic Milk Time", true, 5, 25);
	}

    private static final MagicMilkTimeHud instance = new MagicMilkTimeHud();

	public static MagicMilkTimeHud getInstance() {
		return instance;
	}

	@Override
    public boolean shouldShow() {
        return super.shouldShow() && HypixelManager.getInstance().isInBedwars() && BedwarsFeatures.getInstance().getMagicMilkTime() > -1;
	}

	@Override
    protected String getText(boolean example) {
        if (example) return "Magic Milk: §b25§as";
        else return BedwarsFeatures.getInstance().getMagicMilkTimeText();
	}
}
