/*? if legacy {*//*
package at.yedel.yedelmod.mixins.legacy;



import at.yedel.yedelmod.config.YedelConfig;
import at.yedel.yedelmod.utils.NameLineEvent;
import com.llamalad7.mixinextras.sugar.Local;
import org.polyfrost.oneconfig.api.event.v1.EventManager;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RendererLivingEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;



/^*
 * Implements the {@link at.yedel.yedelmod.utils.NameLineEvent} via name rendering method SNEAKING.
 ^/
@Mixin(RendererLivingEntity.class)
public abstract class MixinRendererLivingEntity extends Render {
    protected MixinRendererLivingEntity(RenderManager renderManager) {
        super(renderManager);
    }

    // Might be wrong
    @Unique
    private static final double yedelmod$sneakHeightReduction = 0.25;


    /^*
     * Injects after the sneaking name rendering code is done, constructing an event and calling {@link net.minecraft.client.renderer.entity.Render#renderOffsetLivingLabel} anyways.
     ^/
    @Inject(method = "renderName(Lnet/minecraft/entity/EntityLivingBase;DDD)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/GlStateManager;popMatrix()V", shift = At.Shift.AFTER), locals = LocalCapture.CAPTURE_FAILHARD)
    private void yedelmod$renderSneakingNameLines(EntityLivingBase entity, double x, double y, double z, CallbackInfo ci, @Local(name = "d") double distanceSq) {
        if (!(entity instanceof AbstractClientPlayer)) {
            return;
        }
        AbstractClientPlayer player = (AbstractClientPlayer) entity;
        NameLineEvent event = new NameLineEvent(NameLineEvent.NameRenderingMethod.SNEAKING, player, distanceSq);
        EventManager.INSTANCE.post(event);
        y -= yedelmod$sneakHeightReduction;
        y += event.getVerticalAdjustment();
        for (String nameLine : event.getNameLines()) {
            y += 9 * 1.15F * 0.02666667;
            super.renderOffsetLivingLabel(player, x, y, z, nameLine, 0.02666667F, distanceSq);
        }
    }
}*//*?}*/