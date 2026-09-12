//? if legacy {
package at.yedel.yedelmod.mixins.legacy;



import at.yedel.yedelmod.features.CustomHitParticles;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;



@Mixin(EntityPlayer.class)
public abstract class MixinEntityPlayer {
    @Inject(method = "attackTargetEntityWithCurrentItem", at = @At("HEAD"))
    private void yedelmod$onAttackEntity(Entity entity, CallbackInfo ci) {
        CustomHitParticles.getInstance().handleAttack(entity);
    }
}
//?}
