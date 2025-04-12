package at.yedel.yedelmod.hud;



import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.polyfrost.oneconfig.api.config.v1.annotations.Text;
import org.polyfrost.oneconfig.api.hud.v1.TextHud;



public class CustomTextHud extends TextHud {
    private CustomTextHud() {
        super("", "");
    }

    private static final CustomTextHud instance = new CustomTextHud();

    public static CustomTextHud getInstance() {
        return instance;
    }

    @Text(
        title = "Display text"
    )
    public String displayText = "";

    @Override
    public @NotNull String title() {
        return "Custom Text HUD";
    }

    @Override
    public @NotNull Category category() {
        return Category.getINFO();
    }

    @Override
    public @NotNull String id() {
        return "custom-text-hud.json";
    }

    @Override
    protected @Nullable String getText() {
        return displayText;
    }
}
