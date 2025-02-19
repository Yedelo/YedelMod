package at.yedel.yedelmod.config;



import cc.polyfrost.oneconfig.config.elements.BasicOption;
import cc.polyfrost.oneconfig.utils.InputHandler;

import java.lang.reflect.Field;



public class EmptyOption extends BasicOption {
    /**
     * Initialize option
     *
     * @param field       variable attached to option (null for category)
     * @param parent      the parent object of the field, used for getting and setting the variable
     * @param name        name of option
     * @param description The description
     * @param category    The category
     * @param subcategory The subcategory
     * @param size        size of option, 0 for single column, 1 for double.
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

    }
}
