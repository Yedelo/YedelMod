package at.yedel.yedelmod.config;



import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import gg.essential.vigilance.data.PropertyData;
import gg.essential.vigilance.data.SortingBehavior;



public class YedelConfigSortingBehavior extends SortingBehavior {
    public Comparator<? super Map.Entry<String, ? extends List<PropertyData>>> getSubcategoryComparator() {
        ArrayList<String> subcategories = new ArrayList<>();
        subcategories.add("Features");
        subcategories.add("Customization");
        return (a, b) -> {
            String firstCategory = a.getValue().get(0).getAttributesExt().getSubcategory();
            String secondCategory = b.getValue().get(0).getAttributesExt().getSubcategory();
            return subcategories.indexOf(firstCategory) - subcategories.indexOf(secondCategory);
        };
    }
}
