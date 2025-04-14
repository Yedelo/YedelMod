package at.yedel.yedelmod.hud;



import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.polyfrost.oneconfig.api.hud.v1.TextHud;
import org.polyfrost.polyui.unit.Units;



public class BountyHuntingHud extends TextHud {
    private BountyHuntingHud() {
        super("", "");
    }

    private static final BountyHuntingHud instance = new BountyHuntingHud();

    public static BountyHuntingHud getInstance() {
        return instance;
    }

    @Override
    public @NotNull String title() {
        return "Bounty Hunting HUD";
    }

    @Override
    public @NotNull Category category() {
        return Category.getCOMBAT();
    }

    @Override
    public @NotNull String id() {
        return "bounty-hunting-hud.json";
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
        return Units.seconds(0.05);
    }
}
