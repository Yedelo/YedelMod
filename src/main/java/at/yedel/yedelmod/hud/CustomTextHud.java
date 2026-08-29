package at.yedel.yedelmod.hud;




//? if v1 {
import org.polyfrost.oneconfig.api.config.v1.annotations.Text;
import org.polyfrost.oneconfig.api.hud.v1.HudManager;
import org.polyfrost.oneconfig.api.hud.v1.TextHud;
//?} else {
/*import at.yedel.yedelmod.config.YedelConfig;
import at.yedel.yedelmod.utils.Constants;
import cc.polyfrost.oneconfig.hud.SingleTextHud;
import cc.polyfrost.oneconfig.config.annotations.Text;
*///?}



public class CustomTextHud extends SingleTextHud {
    public CustomTextHud() {
        //? if v0 {
        /*super(
            "", // no title
            true, // enabled obviously
            5, // x
            5, // y
            1, // normal size
            false, // no background it's ugly
            false, // no rounded corners it's also ugly
            0, // NO rounded corners
            0, // no x padding why would i want it
            0, // no y padding for the same reason
            Constants.EMPTY_COLOR, // no background color
            false, // no border
            0, // NO border
            Constants.EMPTY_COLOR // no border color
        );
        textType = 1;
        *///?} else {
         super("custom_text_hud", "Custom Text HUD", Category.getINFO(), "", "");
        //?}
    }

    @Text(
        //~ if v1 'name' -> 'title'
        title = "Display text"
    )
    public String displayText = "";

    @Override
    public String getText(boolean example) {
        if (example) {
            return "Custom §6display §a§ltext!";
        }
        return displayText;
    }

        //? if v0 {
        /*@Override
        public boolean shouldShow() {
            return YedelConfig.getInstance().enabled && !displayText.trim().isEmpty();
        }
        *///?}
}
