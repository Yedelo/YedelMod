package at.yedel.yedelmod.config;



import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map.Entry;

import gg.essential.vigilance.data.Category;
import gg.essential.vigilance.data.PropertyData;
import gg.essential.vigilance.data.SortingBehavior;
import org.jetbrains.annotations.NotNull;



public class YedelConfigSortingBehavior extends SortingBehavior {
    public @NotNull Comparator<? super Category> getCategoryComparator() {
        ArrayList<String> categories = new ArrayList<>();
        categories.add("General");
        categories.add("Features");
        categories.add("Commands");
        categories.add("Keybinds");
        categories.add("Modern Features");
        categories.add("TNT Tag");
        return (a, b) -> {
            String firstCategory = a.getName();
            String secondCategory = b.getName();
            return categories.indexOf(firstCategory) - categories.indexOf(secondCategory);
        };
    }

    public @NotNull Comparator<? super Entry<String, ? extends List<PropertyData>>> getSubcategoryComparator() {
        ArrayList<String> subcategories = new ArrayList<>();
        subcategories.add("Features");
        subcategories.add("Tweaks");
        subcategories.add("Commands");
        subcategories.add("Keybinds");
        subcategories.add("Customization");
        return (a, b) -> {
            String firstSubcategory = a.getValue().get(0).getAttributesExt().getSubcategory();
            String secondSubcategory = b.getValue().get(0).getAttributesExt().getSubcategory();
            return subcategories.indexOf(firstSubcategory) - subcategories.indexOf(secondSubcategory);
        };
    }
}
