package at.yedel.yedelmod.hud;



import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.polyfrost.oneconfig.api.hud.v1.TextHud;



public class BedwarsXPHud extends TextHud {
    private BedwarsXPHud() {
        super("", "XP");
    }

    private static final BedwarsXPHud instance = new BedwarsXPHud();

    public static BedwarsXPHud getInstance() {
        return instance;
    }

    @Override
    public @NotNull String title() {
        return "Bedwars XP Hud";
    }

    @Override
    public @NotNull Category category() {
        return Category.getINFO();
    }

    @Override
    public @NotNull String id() {
        return "bedwars-xp-hud.json";
    }

    public StringBuilder string = getStringBuilder();

    public void relogic() {
        super.updateAndRecalculate();
    }

    @Override
    protected @Nullable String getText() {
        return null;
    }
}
