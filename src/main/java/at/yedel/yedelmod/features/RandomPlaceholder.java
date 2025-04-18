package at.yedel.yedelmod.features;



import at.yedel.yedelmod.config.YedelConfig;
import at.yedel.yedelmod.utils.typeutils.TextUtils;



public class RandomPlaceholder {
    private RandomPlaceholder() {}

    private static final RandomPlaceholder instance = new RandomPlaceholder();

    public static RandomPlaceholder getInstance() {
        return instance;
    }

    public String replaceRandomString(String message) {
        if (YedelConfig.getInstance().randomPlaceholder) {
            return message.replace(YedelConfig.getInstance().randomPlaceholderText, "@" + TextUtils.randomUuid(8));
        }
        else {
            return message;
        }
    }
}
