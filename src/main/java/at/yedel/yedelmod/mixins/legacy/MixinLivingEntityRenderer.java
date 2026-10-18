/*? if legacy {*//*
package at.yedel.yedelmod.mixins.legacy;



import at.yedel.yedelmod.utils.NameLineEvent;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.entity.living.player.ClientPlayerEntity;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.entity.living.LivingEntity;
import org.polyfrost.oneconfig.api.event.v1.EventManager;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;



/^*
 * Implements the {@link at.yedel.yedelmod.utils.NameLineEvent} via name rendering method SNEAKING.
 ^/
@Mixin(LivingEntityRenderer.class)
public abstract class MixinLivingEntityRenderer extends EntityRenderer {
    @Shadow public abstract void renderNameTag(Entity par1, double par2, double par3, double par4);

    protected MixinLivingEntityRenderer(EntityRenderDispatcher entityRenderDispatcher) {
        super(entityRenderDispatcher);
    }

    // Might be wrong
    @Unique
    private static final double yedelmod$sneakHeightReduction = 0.25;

    /^*
     * Injects after the sneaking name rendering code is done, constructing an event and calling the normal nametag rendering code anyways.
     ^/
    @Inject(method = "renderNameTag(Lnet/minecraft/entity/living/LivingEntity;DDD)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/platform/GlStateManager;popMatrix()V", shift = At.Shift.AFTER))
    private void yedelmod$renderSneakingNameLines(LivingEntity entity, double x, double y, double z, CallbackInfo ci, @Local(name = "d") double distanceSq) {
        if (!(entity instanceof ClientPlayerEntity player)) {
            return;
        }
        NameLineEvent event = new NameLineEvent(NameLineEvent.NameRenderingMethod.SNEAKING, player, distanceSq);
        EventManager.INSTANCE.post(event);
        y -= yedelmod$sneakHeightReduction;
        y += event.getVerticalAdjustment();
        for (String nameLine : event.getNameLines()) {
            y += 9 * 1.15F * 0.02666667;
            super.renderNameTag(player, x, y, z, nameLine, 0.02666667F, distanceSq);
        }
    }
}*//*?}*/