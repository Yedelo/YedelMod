package at.yedel.yedelmod.hud;



import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.polyfrost.oneconfig.api.hud.v1.TextHud;
import org.polyfrost.polyui.unit.Units;



public class MagicMilkTimeHud extends TextHud {
    private MagicMilkTimeHud() {
        super("Magic Milk", "");
    }

    private static final MagicMilkTimeHud instance = new MagicMilkTimeHud();

    public static MagicMilkTimeHud getInstance() {
        return instance;
    }

    @Override public @NotNull String title() {
        return "Magic Milk Time HUD";
    }

    @Override public @NotNull Category category() {
        return Category.getPLAYER();
    }

    @Override
    public @NotNull String id() {
        return "magic-milk-time-hud.json";
    }

    public StringBuilder string = getStringBuilder();

    public void relogic() {
        super.updateAndRecalculate();
    }

    @Override
    protected @Nullable String getText() {
        return null;
    }

    @Override
    public long updateFrequency() {
        return Units.seconds(1);
    }
}
