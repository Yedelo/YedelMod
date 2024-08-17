package at.yedel.yedelmod.mixins.net.minecraft.client.gui;



import java.util.List;

import at.yedel.yedelmod.config.YedelConfig;
import at.yedel.yedelmod.utils.typeutils.TextUtils;
import gg.essential.lib.mixinextras.injector.WrapWithCondition;
import net.minecraft.client.gui.GuiNewChat;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;



@Mixin(GuiNewChat.class)
public abstract class MixinGuiNewChat {
    @WrapWithCondition(method = "clearChatMessages", at = @At(value = "INVOKE", target = "Ljava/util/List;clear()V", ordinal = 2))
    private boolean yedelmod$keepChatHistory(List<String> sentMessages) {
        return !YedelConfig.getInstance().keepChatHistory;
    }

    @ModifyArg(method = "printChatMessageWithOptionalDeletion", at = @At(value = "INVOKE", target = "Lorg/apache/logging/log4j/Logger;info(Ljava/lang/String;)V", remap = false))
    private String yedelmod$unformatChatLogs(String message) {
        if (YedelConfig.getInstance().unformatChatLogs) return TextUtils.removeSection(message);
        else return message;
    }
}
