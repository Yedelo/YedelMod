package at.yedel.yedelmod.mixins;



import at.yedel.yedelmod.api.config.YedelConfig;
import net.minecraft.client.gui.GuiNewChat;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.List;



@Mixin(GuiNewChat.class)
public abstract class MixinGuiNewChat {
    @Redirect(
        method = "clearChatMessages",
        at = @At(value = "INVOKE", target = "Ljava/util/List;clear()V", ordinal = 2)
    )
    private void yedelmod$keepChatHistory(List<String> sentMessages) {
        if (YedelConfig.getInstance().enabled && YedelConfig.getInstance().keepChatHistoryOnChatClear) {
            return;
        }
        sentMessages.clear();
    }
}
