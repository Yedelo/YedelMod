package at.yedel.yedelmod.hud;



import at.yedel.yedelmod.handlers.HypixelManager;
import cc.polyfrost.oneconfig.hud.TextHud;

import java.util.ArrayList;
import java.util.List;



public class BountyHuntingHud extends TextHud {
	private BountyHuntingHud() {
		super(true, 5, 35);
	}

	private static final BountyHuntingHud instance = new BountyHuntingHud();

	public static BountyHuntingHud getInstance() {
		return instance;
	}

	private final List<String> lines = new ArrayList<String>();

	public List<String> getLines() {
		return lines;
	}

	@Override
	public boolean shouldShow() {
		return super.shouldShow() && HypixelManager.getInstance().isInTNTTag();
	}

	@Override
	protected void getLines(List<String> lines, boolean example) {
		if (example) {
			lines.add("§c§lBounty §f§lHunting");
			lines.add("§a83 points");
			lines.add("§a15 kills");
			lines.add("§cYour next target is §aYedelos§c.");
		} else {
			lines.clear();
			lines.addAll(getLines());
		}
	}
}
