package at.yedel.yedelmod.mixins.net.minecraft.client.renderer;



import at.yedel.yedelmod.config.YedelConfig;
import net.minecraft.client.renderer.EntityRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;



@Mixin(EntityRenderer.class)
public abstract class MixinEntityRenderer {
    @ModifyArg(method = "hurtCameraEffect", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/GlStateManager;rotate(FFFF)V", ordinal = 2), index = 0)
    private float antimations$changeHurtcamIntensity(float original) {
        return original * YedelConfig.getInstance().damageTiltStrength;
    }
}
