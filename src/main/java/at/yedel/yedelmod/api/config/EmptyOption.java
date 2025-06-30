package at.yedel.yedelmod.api.config;



import cc.polyfrost.oneconfig.config.data.InfoType;
import cc.polyfrost.oneconfig.config.elements.BasicOption;
import cc.polyfrost.oneconfig.renderer.NanoVGHelper;
import cc.polyfrost.oneconfig.renderer.font.Fonts;
import cc.polyfrost.oneconfig.utils.InputHandler;

import java.lang.reflect.Field;



public class EmptyOption extends BasicOption {
    /**
     * Initialize option
     *
     * @param field       variable attached to option (null for category)
     * @param parent      the parent object of the field, used for getting and setting the variable
     * @param name        coloredName of option
     * @param description The description
     * @param category    The category
     * @param subcategory The subcategory
     * @param size        size of option, 0 for single column, 1 for double.
     *                    ↑ lie 😂😂😂 it's 1 for single column 2 for double
     */
    public EmptyOption(Field field, Object parent, String name, String description, String category, String subcategory, int size) {
        super(field, parent, name, description, category, subcategory, size);
    }

    @Override
    public int getHeight() {
        return 32;
    }

    @Override
    public void draw(long vg, int x, int y, InputHandler inputHandler) {
        NanoVGHelper.INSTANCE.drawInfo(vg, InfoType.INFO, x, y + 4, 24);
        NanoVGHelper.INSTANCE.drawText(vg, name, x + 32, y + 18, nameColor, 14, Fonts.MEDIUM);
    }
}
