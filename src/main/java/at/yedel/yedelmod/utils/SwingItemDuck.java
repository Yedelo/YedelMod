package at.yedel.yedelmod.utils;


//#if MC == 1.12.2
//$$import net.minecraft.util.EnumHand;
//#endif

public interface SwingItemDuck {
    //#if MC == 1.8.9
    void yedelmod$swingHandLocally();
    //#else
    //$$void yedelmod$swingHandLocally(EnumHand hand);
    //#endif
}
