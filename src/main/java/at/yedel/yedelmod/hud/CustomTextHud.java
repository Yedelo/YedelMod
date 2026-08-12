package at.yedel.yedelmod.hud;



import org.polyfrost.oneconfig.api.config.v1.annotations.Text;
import org.polyfrost.oneconfig.api.hud.v1.HudManager;
import org.polyfrost.oneconfig.api.hud.v1.TextHud;



//@TODO color doesn't work
//@TODO hide when shouldRender reimplemented
public class CustomTextHud extends TextHud {
    public CustomTextHud() {
        super("custom_text_hud", "Custom Text HUD", Category.getINFO(), "", "");
    }

    @Text(
        title = "Display text"
    )
    public String displayText = "";

    @Override
    public String getText() {
        if (!isReal() || HudManager.INSTANCE.isEditing()) {
            return "Custom §6display §a§ltext!";
        }
        return displayText;
    }

    @Override
    public boolean hasBackground() {
        return false;
    }

    //    @Override
    //    public boolean shouldRender() {
    //        return YedelConfig.getInstance().enabled && !displayText.trim().isEmpty();
    //    }
}
