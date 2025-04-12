package at.yedel.yedelmod.features;



import at.yedel.yedelmod.config.YedelConfig;
import at.yedel.yedelmod.utils.typeutils.TextUtils;
import dev.deftu.omnicore.client.OmniChat;
import org.polyfrost.oneconfig.api.event.v1.events.ChatEvent;
import org.polyfrost.oneconfig.api.event.v1.invoke.impl.Subscribe;



public class RandomPlaceholder {
    private RandomPlaceholder() {}

    private static final RandomPlaceholder instance = new RandomPlaceholder();

    public static RandomPlaceholder getInstance() {
        return instance;
    }

    @Subscribe
    public void replaceRandomString(ChatEvent.Send event) {
        if (YedelConfig.getInstance().randomPlaceholder) {
            event.cancelled = true;
            OmniChat.sendPlayerMessage(event.message.replace(YedelConfig.getInstance().randomPlaceholderText, "@" + TextUtils.randomUuid(8)));
        }
    }
}
