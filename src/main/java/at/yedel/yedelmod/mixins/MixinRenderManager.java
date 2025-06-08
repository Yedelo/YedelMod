package at.yedel.yedelmod.mixins;



import at.yedel.yedelmod.utils.nameline.NameLineImpl;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;



@Mixin(RenderManager.class)
public abstract class MixinRenderManager {
    @Shadow
    public abstract <T extends Entity> Render<T> getEntityRenderObject(Entity entityIn);

    @Inject(method = "doRenderEntity", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/entity/Render;doRender(Lnet/minecraft/entity/Entity;DDDFF)V", shift = At.Shift.AFTER))
    private void yedelmod$renderNameLines(Entity entity, double x, double y, double z, float entityYaw, float partialTicks, boolean p_147939_10_, CallbackInfoReturnable<Boolean> cir) {
        NameLineImpl.renderNameLines(entity, x, y, z, getEntityRenderObject(entity));
    }
}
