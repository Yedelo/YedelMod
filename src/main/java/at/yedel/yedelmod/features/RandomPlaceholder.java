package at.yedel.yedelmod.features;



import at.yedel.yedelmod.config.YedelConfig;
import at.yedel.yedelmod.utils.TextUtils;
import net.fabricmc.fabric.api.client.message.v1.ClientSendMessageEvents;



public class RandomPlaceholder {
    private static final RandomPlaceholder INSTANCE = new RandomPlaceholder();

    public static RandomPlaceholder getInstance() {
        return INSTANCE;
    }

    private RandomPlaceholder() {
        ClientSendMessageEvents.MODIFY_CHAT.register(this::modifyMessage);
        ClientSendMessageEvents.MODIFY_COMMAND.register(this::modifyMessage);
    }

    private String modifyMessage(String message) {
        if (YedelConfig.getInstance().enabled && YedelConfig.getInstance().randomPlaceholder && !YedelConfig.getInstance().randomPlaceholderText.trim().isEmpty()) {
            return message.replace(YedelConfig.getInstance().randomPlaceholderText, "@" + TextUtils.randomUuid(8));
        }
        return message;
    }
}
