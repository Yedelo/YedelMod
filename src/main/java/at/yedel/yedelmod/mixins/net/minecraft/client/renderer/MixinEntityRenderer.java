package at.yedel.yedelmod.mixins.net.minecraft.client.renderer;



import at.yedel.yedelmod.config.YedelConfig;
import net.minecraft.client.renderer.EntityRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;



@Mixin(EntityRenderer.class)
public abstract class MixinEntityRenderer {
    @ModifyConstant(method = "hurtCameraEffect", constant = @Constant(floatValue = 14.0f))
    private float yedelmod$hurtCameraEffect(float constant) {
        return constant * YedelConfig.getInstance().damageTiltStrength;
    }
}
