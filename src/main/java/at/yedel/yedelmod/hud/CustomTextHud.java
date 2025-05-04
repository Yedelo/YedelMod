package at.yedel.yedelmod.hud;



import at.yedel.yedelmod.utils.Constants;
import cc.polyfrost.oneconfig.config.annotations.Text;
import cc.polyfrost.oneconfig.config.migration.VigilanceName;
import cc.polyfrost.oneconfig.hud.SingleTextHud;



public class CustomTextHud extends SingleTextHud {
    @Text(
        name = "Display text",
        size = 2
    )
    @VigilanceName(name = "displayedText", category = "storage", subcategory = "")
    public String displayText = "";

    public CustomTextHud() {
        super(
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
    }

    @Override
    protected String getText(boolean example) {
        if (example) {
            return "Example text";
        }
        else {
            return displayText;
        }
    }
}
