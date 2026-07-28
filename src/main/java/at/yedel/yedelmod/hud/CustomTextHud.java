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
        super("custom_text_hud", "Custom Text HUD", Category.getINFO(), "prefix", "");
    }

    @Text(
        title = "Display text"
    )
    public String displayText = "";

    @Override
    protected String getText() {
        if (example) {
            return "Example text";
        }
        else {
            return displayText;
        }
    }

    @Override public float getWidth() {
        return 0;
    }

    @Override public float getHeight() {
        return 0;
    }

    @Override public void render(@NonNull GuiGraphicsExtractor guiGraphicsExtractor) {

    }

    @Override public boolean update() {
        return false;
    }
}
