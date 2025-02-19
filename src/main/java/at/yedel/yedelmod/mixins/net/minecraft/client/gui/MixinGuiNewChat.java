package at.yedel.yedelmod.mixins.net.minecraft.client.gui;


import at.yedel.yedelmod.config.YedelConfig;
import at.yedel.yedelmod.utils.typeutils.TextUtils;
import net.minecraft.client.gui.GuiNewChat;
import net.minecraft.util.IChatComponent;
import org.lwjgl.opengl.Display;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;



@Mixin(GuiNewChat.class)
public abstract class MixinGuiNewChat {
    // Lan message is printed to the chat GUI and skips ClientChatReceivedEvent. Listening to logs may be possible here
    @Inject(method = "printChatMessage", at = @At("HEAD"))
    private void yedelmod$onLanHost(IChatComponent chatComponent, CallbackInfo ci) {
        if (chatComponent.getUnformattedText().startsWith("Local game hosted on port")) {
            String port = TextUtils.removeSection(chatComponent.getFormattedText().substring(26));
            if (YedelConfig.getInstance().changeTitle) Display.setTitle("Minecraft 1.8.9 - LAN Singleplayer - " + port);
        }
    }

    @Redirect(method = "clearChatMessages",
        at = @At(value = "INVOKE", target = "Ljava/util/List;clear()V", ordinal = 2)
    )
    private void yedelmod$keepChatHistory(List<String> sentMessages) {
        if (YedelConfig.getInstance().keepChatHistory) {
            return;
        }
        sentMessages.clear();
    }

    @ModifyArg(method = "printChatMessageWithOptionalDeletion", at = @At(value = "INVOKE", target = "Lorg/apache/logging/log4j/Logger;info(Ljava/lang/String;)V", remap = false))
    private String yedelmod$unformatChatLogs(String message) {
        if (YedelConfig.getInstance().unformatChatLogs) return TextUtils.removeSection(message);
        else return message;
    }
}
