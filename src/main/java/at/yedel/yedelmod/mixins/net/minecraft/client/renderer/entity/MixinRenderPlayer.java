package at.yedel.yedelmod.mixins.net.minecraft.client.renderer.entity;



import at.yedel.yedelmod.config.YedelConfig;
import at.yedel.yedelmod.events.RenderScoreEvent;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.model.ModelPlayer;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.item.EnumAction;
import net.minecraftforge.common.MinecraftForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;



@Mixin(RenderPlayer.class)
public abstract class MixinRenderPlayer {
    @Shadow
    public abstract ModelPlayer getMainModel();

    @Inject(method = "renderOffsetLivingLabel(Lnet/minecraft/client/entity/AbstractClientPlayer;DDDLjava/lang/String;FD)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/entity/RenderPlayer;renderLivingLabel(Lnet/minecraft/entity/Entity;Ljava/lang/String;DDDI)V"))
    private void yedelmod$postRenderScoreEvent(AbstractClientPlayer entityIn, double x, double y, double z, String str, float incrementIThink, double distanceSqToEntityIThink, CallbackInfo ci) {
        MinecraftForge.EVENT_BUS.post(new RenderScoreEvent((RenderPlayer) (Object) this, entityIn, x, y, z, str, incrementIThink, distanceSqToEntityIThink));
    }

    @Inject(method = "setModelVisibilities", at = @At("TAIL"))
    private void yedelmod$setAutoBlockModel(AbstractClientPlayer clientPlayer, CallbackInfo ci) {
        if (YedelConfig.getInstance().clientSideAutoBlock && clientPlayer.isUser() && clientPlayer.getHeldItem() != null && clientPlayer.getHeldItem().getItemUseAction() == EnumAction.BLOCK) {
            getMainModel().heldItemRight = 3;
        }
    }
}
