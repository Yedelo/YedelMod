package at.yedel.yedelmod.mixins.net.minecraft.client.renderer.entity;



import at.yedel.yedelmod.events.RenderScoreEvent;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraftforge.common.MinecraftForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;



@Mixin(RenderPlayer.class)
public abstract class MixinRenderPlayer {
    @Inject(method = "renderOffsetLivingLabel(Lnet/minecraft/client/entity/AbstractClientPlayer;DDDLjava/lang/String;FD)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/entity/RenderPlayer;renderLivingLabel(Lnet/minecraft/entity/Entity;Ljava/lang/String;DDDI)V"))
    private void yedelmod$postRenderScoreEvent(AbstractClientPlayer entityIn, double x, double y, double z, String str, float p_177069_9_, double p_177069_10_, CallbackInfo ci) {
        MinecraftForge.EVENT_BUS.post(new RenderScoreEvent((RenderPlayer) (Object) this, entityIn, x, y, z, str, p_177069_9_, p_177069_10_));
    }
}
