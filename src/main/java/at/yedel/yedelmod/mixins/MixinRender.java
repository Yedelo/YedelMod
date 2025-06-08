package at.yedel.yedelmod.mixins;



import at.yedel.yedelmod.utils.nameline.NameLineImpl;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;



@Mixin(Render.class)
public abstract class MixinRender<T extends Entity> {
    @Inject(method = "renderLivingLabel", at = @At("HEAD"))
    private void yedelmod$incrementRenderedLabels(T entityIn, String str, double x, double y, double z, int maxDistance, CallbackInfo ci) {
        NameLineImpl.incrementRenderedLabels(entityIn);
    }
}
