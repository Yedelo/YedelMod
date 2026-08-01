package at.yedel.yedelmod.hud;



import at.yedel.yedelmod.config.YedelConfig;
import at.yedel.yedelmod.features.major.TNTTagFeatures;
import org.polyfrost.oneconfig.api.hud.v1.Hud;
import org.polyfrost.oneconfig.api.hud.v1.TextHud;

import java.util.ArrayList;



public class BountyHuntingHud extends TextHud {
    private static final BountyHuntingHud INSTANCE = new BountyHuntingHud();

    public static BountyHuntingHud getInstance() {
        return INSTANCE;
    }

    private BountyHuntingHud() {
        super("bounty_hunting_hud", "Bounty Hunting HUD", Hud.Category.getINFO(), "", "");
    }

    @Override
    public boolean getHidden() {
        return super.getHidden() || !YedelConfig.getInstance().bountyHunting || !TNTTagFeatures.getInstance().isInTNTTag();
    }

    @Override
    protected String getText() {
        ArrayList<String> lines = new ArrayList<>();
        if (!isReal()) {
            lines.add("§c§lBounty §f§lHunting");
            lines.add("§a83 points");
            lines.add("§a15 kills");
            lines.add("§cYour next target is §aYedelos§c.");
        }
        else {
            lines.clear();
            lines.addAll(TNTTagFeatures.getInstance().getDisplayLines());
        }
        return String.join("\n", lines);
    }
}
