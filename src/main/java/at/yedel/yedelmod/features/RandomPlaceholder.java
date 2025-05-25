package at.yedel.yedelmod.features;



import at.yedel.yedelmod.config.YedelConfig;
import at.yedel.yedelmod.utils.typeutils.TextUtils;
import cc.polyfrost.oneconfig.events.event.ChatSendEvent;
import cc.polyfrost.oneconfig.libs.eventbus.Subscribe;



public class RandomPlaceholder {
    private static final RandomPlaceholder INSTANCE = new RandomPlaceholder();

    public static RandomPlaceholder getInstance() {
        return INSTANCE;
    }

    private RandomPlaceholder() {}

    @Subscribe
    public void replaceRandomString(ChatSendEvent event) {
        if (YedelConfig.getInstance().enabled && YedelConfig.getInstance().randomPlaceholder && !YedelConfig.getInstance().randomPlaceholderText.trim().isEmpty()) {
            event.message =
                event.message.replace(YedelConfig.getInstance().randomPlaceholderText, "@" + TextUtils.randomUuid(8));
        }
    }
}
