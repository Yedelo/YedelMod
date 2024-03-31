package at.yedel.yedelmod.mixins.client.gui;



import at.yedel.yedelmod.config.YedelConfig;
import at.yedel.yedelmod.utils.typeutils.TextUtils;
import net.minecraft.client.gui.GuiNewChat;
import net.minecraft.util.IChatComponent;
import org.lwjgl.opengl.Display;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;



@Mixin(GuiNewChat.class)
public abstract class MixinGuiNewChat {
    // Lan message is printed to the chat GUI, skips ClientChatReceivedEvent
    @Inject(method = "printChatMessage", at = @At("HEAD"))
    private void yedelmod$onLanHost(IChatComponent chatComponent, CallbackInfo ci) {
        if (chatComponent.getUnformattedText().startsWith("Local game hosted on port")) {
            String port = TextUtils.removeSection(chatComponent.getFormattedText().substring(26));
            if (YedelConfig.changeTitle) Display.setTitle("Minecraft 1.8.9 - LAN Singleplayer - " + port);
        }
    }
}
