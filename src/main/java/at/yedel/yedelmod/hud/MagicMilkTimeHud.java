package at.yedel.yedelmod.hud;



import at.yedel.yedelmod.features.major.BedwarsFeatures;
import at.yedel.yedelmod.utils.Constants;
import cc.polyfrost.oneconfig.hud.SingleTextHud;



public class MagicMilkTimeHud extends SingleTextHud {
    public MagicMilkTimeHud() {
        super(
            "Magic Milk", // title is actually useful now
            true, // enabled obviously
            5, // x
            25, // y
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
    }

    @Override
    public boolean shouldShow() {
        return super.shouldShow() && BedwarsFeatures.getInstance().isInBedwars() && BedwarsFeatures.getInstance().getMagicMilkTime() > -1;
    }

    @Override
    protected String getText(boolean example) {
        if (example) return "§b25§as";
        else {
            return BedwarsFeatures.getInstance().getMagicMilkTimeText();
        }
    }
}
