package at.yedel.yedelmod.features;



import at.yedel.yedelmod.config.YedelConfig;
import at.yedel.yedelmod.utils.TextUtils;
//? if modern
 //import net.fabricmc.fabric.api.client.message.v1.ClientSendMessageEvents;


public class RandomPlaceholder {
    private static final RandomPlaceholder INSTANCE = new RandomPlaceholder();

    public static RandomPlaceholder getInstance() {
        return INSTANCE;
    }

    private RandomPlaceholder() {
        //? if modern {
        /*ClientSendMessageEvents.MODIFY_CHAT.register((message) -> should() ? replace(message) : message);
        ClientSendMessageEvents.MODIFY_COMMAND.register((message) -> should() ? replace(message) : message);
        *///?}
    }

    public boolean should() {
        return YedelConfig.getInstance().enabled && YedelConfig.getInstance().randomPlaceholder && !YedelConfig.getInstance().randomPlaceholderText.trim().isEmpty();
    }

     public String replace(String message) {
        return message.replace(YedelConfig.getInstance().randomPlaceholderText, "@" + TextUtils.randomUuid(8));
    }
}
