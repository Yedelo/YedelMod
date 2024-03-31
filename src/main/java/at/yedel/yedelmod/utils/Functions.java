package at.yedel.yedelmod.utils;



import at.yedel.yedelmod.utils.typeutils.TextUtils;

import static at.yedel.yedelmod.YedelMod.minecraft;



public class Functions {
    public static String getScoreboardName() {
        try {
            return TextUtils.removeFormatting(minecraft.theWorld.getScoreboard().getObjectiveInDisplaySlot(1).getDisplayName());
        }
        catch (Exception nameIsNull) {return "";}
    }
}

