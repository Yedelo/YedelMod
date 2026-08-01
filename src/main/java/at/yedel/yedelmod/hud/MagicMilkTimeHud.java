package at.yedel.yedelmod.hud;



import at.yedel.yedelmod.features.major.BedwarsFeatures;
import org.polyfrost.oneconfig.api.hud.v1.TextHud;



public class MagicMilkTimeHud extends TextHud {
    private static final MagicMilkTimeHud INSTANCE = new MagicMilkTimeHud();

    public static MagicMilkTimeHud getInstance() {
        return INSTANCE;
    }

    private MagicMilkTimeHud() {
        super("magic_milk_time_hud", "Magic Milk Time HUD", Category.getINFO(), "Magic Milk:", "");
    }

    @Override
    public boolean getHidden() {
        return super.getHidden() || !BedwarsFeatures.getInstance().isInBedwars() || BedwarsFeatures.getInstance().getMagicMilkTime() <= -1;
    }

    @Override
    protected String getText() {
        return BedwarsFeatures.getInstance().getMagicMilkTimeText();
    }
}
