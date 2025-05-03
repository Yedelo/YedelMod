package at.yedel.yedelmod.mixins;



import at.yedel.yedelmod.config.YedelConfig;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.model.ModelPlayer;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.item.EnumAction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;



@Mixin(RenderPlayer.class)
public abstract class MixinRenderPlayer {
    @Shadow
    public abstract ModelPlayer getMainModel();

    @Inject(method = "setModelVisibilities", at = @At("TAIL"))
    private void yedelmod$setAutoBlockModel(AbstractClientPlayer clientPlayer, CallbackInfo ci) {
        if (YedelConfig.getInstance().clientSideAutoBlock && clientPlayer.isUser() && clientPlayer.getHeldItem() != null && clientPlayer.getHeldItem().getItemUseAction() == EnumAction.BLOCK) {
            getMainModel().heldItemRight = 3;
        }
    }
}
