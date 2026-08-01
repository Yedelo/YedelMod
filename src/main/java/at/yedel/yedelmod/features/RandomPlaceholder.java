package at.yedel.yedelmod.features;



import at.yedel.yedelmod.config.YedelConfig;
import org.polyfrost.oneconfig.api.event.v1.events.ChatEvent;
import org.polyfrost.oneconfig.api.event.v1.invoke.impl.Subscribe;



public class RandomPlaceholder {
    private static final RandomPlaceholder INSTANCE = new RandomPlaceholder();

    public static RandomPlaceholder getInstance() {
        return INSTANCE;
    }

    private RandomPlaceholder() {}

    @Subscribe
    public void replaceRandomString(ChatEvent.Send event) {
        if (YedelConfig.getInstance().enabled && YedelConfig.getInstance().randomPlaceholder && !YedelConfig.getInstance().randomPlaceholderText.trim().isEmpty()) {
            //@TODO cancel it for real
        }
    }
}
