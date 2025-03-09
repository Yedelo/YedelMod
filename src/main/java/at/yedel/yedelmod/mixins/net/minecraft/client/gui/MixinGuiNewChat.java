package at.yedel.yedelmod.mixins.net.minecraft.client.gui;



import at.yedel.yedelmod.config.YedelConfig;
import cc.polyfrost.oneconfig.libs.universal.wrappers.message.UTextComponent;
import net.minecraft.client.gui.GuiNewChat;
import net.minecraft.util.IChatComponent;
import org.lwjgl.opengl.Display;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;



@Mixin(GuiNewChat.class)
public abstract class MixinGuiNewChat {
    // Lan message is printed to the chat GUI and skips ClientChatReceivedEvent. Listening to logs may be possible here
    @Inject(method = "printChatMessage", at = @At("HEAD"))
    private void yedelmod$onLanHost(IChatComponent chatComponent, CallbackInfo ci) {
        if (chatComponent.getUnformattedText().startsWith("Local game hosted on port")) {
            String port = UTextComponent.Companion.stripFormatting(chatComponent.getFormattedText().substring(26));
            if (YedelConfig.getInstance().changeWindowTitle)
                Display.setTitle("Minecraft 1.8.9 - LAN Singleplayer - " + port);
        }
    }

    @Redirect(
        method = "clearChatMessages",
        at = @At(value = "INVOKE", target = "Ljava/util/List;clear()V", ordinal = 2)
    )
    private void yedelmod$keepChatHistory(List<String> sentMessages) {
        if (YedelConfig.getInstance().keepChatHistoryOnChatClear) {
            return;
        }
        sentMessages.clear();
    }
}
