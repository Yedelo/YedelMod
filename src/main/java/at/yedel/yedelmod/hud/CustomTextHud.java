package at.yedel.yedelmod.hud;




import at.yedel.yedelmod.config.YedelConfig;
import org.polyfrost.oneconfig.api.config.v1.annotations.Text;
import org.polyfrost.oneconfig.api.hud.v1.HudManager;
import org.polyfrost.oneconfig.api.hud.v1.TextHud;


//~ texthud_bridge
public class CustomTextHud extends TextHud {
    public CustomTextHud() {
        super("custom_text_hud", "Custom Text HUD", Category.getINFO(), "", "");
    }

    @Text(
        //~ if v1 'name' -> 'title'
        title = "Display text"
    )
    public String displayText = "";

    @Override
    protected String getText() {
        if (!isReal() || HudManager.INSTANCE.isEditing()) {
            return "Custom §6display §a§ltext!";
        }
        return displayText;
    }

    @Override
    public boolean shouldShow() {
        return YedelConfig.getInstance().enabled && !displayText.trim().isEmpty();
    }
}
