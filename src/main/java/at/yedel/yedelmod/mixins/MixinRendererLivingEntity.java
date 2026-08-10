package at.yedel.yedelmod.mixins;



import at.yedel.yedelmod.utils.NameLineEvent;
import cc.polyfrost.oneconfig.events.EventManager;
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



/**
 * Implements the {@link at.yedel.yedelmod.utils.NameLineEvent} via name rendering method SNEAKING.
 */
@Mixin(RendererLivingEntity.class)
public abstract class MixinRendererLivingEntity extends Render {
    protected MixinRendererLivingEntity(RenderManager renderManager) {
        super(renderManager);
    }

    // Might be wrong
    @Unique
    private static final double yedelmod$sneakHeightReduction = 0.25;

    @Unique
    private double yedelmod$distanceSquared;

    /**
     * Unfortunate for compatibility
     */
    @Redirect(method = "renderName(Lnet/minecraft/entity/EntityLivingBase;DDD)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/EntityLivingBase;getDistanceSqToEntity(Lnet/minecraft/entity/Entity;)D"))
    private double yedelmod$setDistanceSquared(EntityLivingBase instance, Entity entity) {
        return yedelmod$distanceSquared = instance.getDistanceSqToEntity(entity);
    }

    /**
     * Injects after the sneaking name rendering code is done, constructing an event and calling {@link net.minecraft.client.renderer.entity.Render#renderOffsetLivingLabel} anyways.
     */
    @Inject(method = "renderName(Lnet/minecraft/entity/EntityLivingBase;DDD)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/GlStateManager;popMatrix()V", shift = At.Shift.AFTER), locals = LocalCapture.CAPTURE_FAILHARD)
    private void yedelmod$renderSneakingNameLines(EntityLivingBase entity, double x, double y, double z, CallbackInfo ci) {
        if (!(entity instanceof AbstractClientPlayer)) {
            return;
        }
        AbstractClientPlayer player = (AbstractClientPlayer) entity;
        NameLineEvent event = new NameLineEvent(NameLineEvent.NameRenderingMethod.SNEAKING, player, yedelmod$distanceSquared);
        EventManager.INSTANCE.post(event);
        y -= yedelmod$sneakHeightReduction;
        y += event.getVerticalAdjustment();
        for (String nameLine : event.getNameLines()) {
            y += 9 * 1.15F * 0.02666667;
            super.renderOffsetLivingLabel(player, x, y, z, nameLine, 0.02666667F, yedelmod$distanceSquared);
        }
    }
}
