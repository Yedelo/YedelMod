package at.yedel.yedelmod.hud;



import at.yedel.yedelmod.features.major.TNTTagFeatures;
import org.polyfrost.oneconfig.api.hud.v1.Hud;
import org.polyfrost.oneconfig.api.hud.v1.HudManager;
import org.polyfrost.oneconfig.api.hud.v1.TextHud;

import java.util.ArrayList;



//@TODO color doesn't work
//@TODO hide when shouldRender reimplemented
public class BountyHuntingHud extends TextHud {
    public BountyHuntingHud() {
        super("bounty_hunting_hud", "Bounty Hunting HUD", Hud.Category.getINFO(), "", "");
    }

    @Override
    protected String getText() {
        ArrayList<String> lines = new ArrayList<>();
        if (!isReal() || HudManager.INSTANCE.isEditing()) {
            lines.add("§c§lBounty §f§lHunting");
            lines.add("§a83 points");
            lines.add("§a15 kills");
            lines.add("§cYour next target is §aYedelos§c.");
        }
        else {
            lines.addAll(TNTTagFeatures.getInstance().getDisplayLines());
        }
        return String.join("\n", lines);
    }

    @Override
    public boolean hasBackground() {
        return false;
    }

    //    @Override
    //    public boolean shouldRender() {
    //        return YedelConfig.getInstance().bountyHunting && TNTTagFeatures.getInstance().isInTNTTag();
    //    }
}
