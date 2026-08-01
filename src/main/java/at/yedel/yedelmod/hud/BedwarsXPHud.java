package at.yedel.yedelmod.hud;



import at.yedel.yedelmod.features.major.BedwarsFeatures;
import org.polyfrost.oneconfig.api.hud.v1.TextHud;



public class BedwarsXPHud extends TextHud {
    private static final BedwarsXPHud INSTANCE = new BedwarsXPHud();

    public static BedwarsXPHud getInstance() {
        return INSTANCE;
    }

    private BedwarsXPHud() {
        super("bedwars_xp_hud", "Bedwars XP Hud", Category.getINFO(), "XP:", "");
    }

    @Override
    public boolean getHidden() {
        return super.getHidden() || !BedwarsFeatures.getInstance().isInBedwars() || !BedwarsFeatures.getInstance().hasExperience();
    }

    @Override
    protected String getText() {
        if (!isReal()) {
            return "§b3,550§7/§a5,000";
        }
        else {
            return BedwarsFeatures.getInstance().getHudXPText();
        }
    }


}
