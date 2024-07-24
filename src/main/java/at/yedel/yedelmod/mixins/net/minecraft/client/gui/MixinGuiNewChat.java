package at.yedel.yedelmod.mixins.net.minecraft.client.gui;



import java.util.List;

import at.yedel.yedelmod.config.YedelConfig;
import at.yedel.yedelmod.utils.typeutils.TextUtils;
import gg.essential.lib.mixinextras.injector.WrapWithCondition;
import net.minecraft.client.gui.GuiNewChat;
import net.minecraft.util.IChatComponent;
import org.lwjgl.opengl.Display;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;



@Mixin(GuiNewChat.class)
public abstract class MixinGuiNewChat {
    // Lan message is printed to the chat GUI, skips ClientChatReceivedEvent
    @Inject(method = "printChatMessage", at = @At("HEAD"))
    private void yedelmod$onLanHost(IChatComponent chatComponent, CallbackInfo ci) {
        if (chatComponent.getUnformattedText().startsWith("Local game hosted on port")) {
            String port = TextUtils.removeSection(chatComponent.getFormattedText().substring(26));
            if (YedelConfig.getInstance().changeTitle) Display.setTitle("Minecraft 1.8.9 - LAN Singleplayer - " + port);
        }
    }

    @WrapWithCondition(method = "clearChatMessages", at = @At(value = "INVOKE", target = "Ljava/util/List;clear()V", ordinal = 2))
    private boolean yedelmod$keepChatHistory(List sentMessages) {
        return !YedelConfig.getInstance().keepChatHistory;
    }

    @ModifyArg(method = "printChatMessageWithOptionalDeletion", at = @At(value = "INVOKE", target = "Lorg/apache/logging/log4j/Logger;info(Ljava/lang/String;)V"))
    private String yedelmod$unformatChatLogs(String message) {
        if (YedelConfig.getInstance().unformatChatLogs) return TextUtils.removeSection(message);
        return message;
    }
}
