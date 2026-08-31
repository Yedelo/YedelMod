package at.yedel.yedelmod.hud;



import at.yedel.yedelmod.config.YedelConfig;
import at.yedel.yedelmod.features.major.TNTTagFeatures;
//? if v1 {
import org.polyfrost.oneconfig.api.hud.v1.Hud;
import org.polyfrost.oneconfig.api.hud.v1.HudManager;
import org.polyfrost.oneconfig.api.hud.v1.TextHud;
//?} else {
/*import at.yedel.yedelmod.utils.Constants;
import cc.polyfrost.oneconfig.hud.TextHud;
import java.util.List;
*///?}

import java.util.ArrayList;


public class BountyHuntingHud extends TextHud {
    public BountyHuntingHud() {
        //? if v0 {
        /*super(
            true, // enabled obviously
            5, // x
            35, // y
            1, // normal size
            false, // no background it's ugly
            false, // no rounded corners it's also ugly
            0, // NO rounded corners
            0, // no x padding why would i want it
            0, // no y padding for the same reason
            Constants.EMPTY_COLOR, // no background color
            false, // no border
            0, // NO border
            Constants.EMPTY_COLOR // no border color
        );
        textType = 1;
        *///?} else v1 {
        super("bounty_hunting_hud", "Bounty Hunting HUD", Category.getINFO(), "", "");
        //?}
    }

    //? if v0 {
    /*
        @Override
    protected void getLines(List<String> lines, boolean example) {
        if (example) {
            lines.add("§c§lBounty §f§lHunting");
            lines.add("§a83 points");
            lines.add("§a15 kills");
            lines.add("§cYour next target is §aYedelos§c.");
        }
        else {
            lines.clear();
            lines.addAll(TNTTagFeatures.getInstance().getDisplayLines());
        }
    }
    
    *///?} else {
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
    //?}

    //? if v0 {
    /*@Override
    public boolean shouldShow() {
        return YedelConfig.getInstance().bountyHunting && TNTTagFeatures.getInstance().isInTNTTag();
    }
    *///?} else {
    @Override
    public boolean hasBackground() {
        return false;
    }
    //?}
}

