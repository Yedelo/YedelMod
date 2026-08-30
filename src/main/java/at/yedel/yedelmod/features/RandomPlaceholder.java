package at.yedel.yedelmod.features;



import at.yedel.yedelmod.config.YedelConfig;
import at.yedel.yedelmod.utils.TextUtils;
//? if v0
//import cc.polyfrost.oneconfig.events.event.ChatSendEvent;

import org.polyfrost.oneconfig.api.event.v1.invoke.impl.Subscribe;
//? if fabric
 import net.fabricmc.fabric.api.client.message.v1.ClientSendMessageEvents;



public class RandomPlaceholder {
    private static final RandomPlaceholder INSTANCE = new RandomPlaceholder();

    public static RandomPlaceholder getInstance() {
        return INSTANCE;
    }

    private RandomPlaceholder() {
        //? if v1 {
        ClientSendMessageEvents.MODIFY_CHAT.register((message) -> should() ? replace(message) : message);
        ClientSendMessageEvents.MODIFY_COMMAND.register((message) -> should() ? replace(message) : message);
        //?}
    }

    //? if v0 {
    /*@Subscribe
    public void modifyMessage(ChatSendEvent event) {
        if (should()) {
            event.message = replace(event.message);
        }
    }
    *///?}

    private boolean should() {
        return YedelConfig.getInstance().enabled && YedelConfig.getInstance().randomPlaceholder && !YedelConfig.getInstance().randomPlaceholderText.trim().isEmpty();
    }

    private String replace(String message) {
        return message.replace(YedelConfig.getInstance().randomPlaceholderText, "@" + TextUtils.randomUuid(8));
    }
}
