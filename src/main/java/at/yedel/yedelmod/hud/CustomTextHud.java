package at.yedel.yedelmod.hud;



import net.minecraft.client.gui.GuiGraphicsExtractor;
import org.jspecify.annotations.NonNull;
import org.polyfrost.oneconfig.api.config.v1.annotations.Text;
import org.polyfrost.oneconfig.api.hud.v1.LegacyHud;



public class CustomTextHud extends LegacyHud {
    private static final CustomTextHud INSTANCE = new CustomTextHud();

    public static CustomTextHud getInstance() {
        return INSTANCE;
    }

    private CustomTextHud() {
        super("custom_text_hud", "Custom Text HUD", Category.getINFO());
    }

    @Text(
        title = "Display text"
    )
    public String displayText = "";

    // @TODO make this real
    @Override
    public float getWidth() {
        return 20;
    }

    @Override
    public float getHeight() {
        return 20;
    }

    @Override
    public void render(@NonNull GuiGraphicsExtractor guiGraphicsExtractor) {
        // @TODO uhhhhhh
    }

    @Override
    public boolean update() {
        return false;
    }
}
