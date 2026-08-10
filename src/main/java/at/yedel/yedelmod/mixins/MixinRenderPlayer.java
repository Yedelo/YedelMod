package at.yedel.yedelmod.mixins;



import at.yedel.yedelmod.utils.NameLineEvent;
import cc.polyfrost.oneconfig.events.EventManager;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.client.renderer.entity.RendererLivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;



/**
 * Implements the {@link at.yedel.yedelmod.utils.NameLineEvent} via name rendering method STANDARD.
 */
@Mixin(RenderPlayer.class)
public abstract class MixinRenderPlayer extends RendererLivingEntity {
    public MixinRenderPlayer(RenderManager renderManagerIn, ModelBase modelBaseIn, float shadowSizeIn) {
        super(renderManagerIn, modelBaseIn, shadowSizeIn);
    }

    /**
     * Injects after the score objective and player name labels render, calling {@link net.minecraft.client.renderer.entity.RendererLivingEntity#renderOffsetLivingLabel} for each name line.
     * This respects the local y variable in the method, and we add on to it with vertical adjustments and the standard modifier for each line.
     */
    @Inject(method = "renderOffsetLivingLabel(Lnet/minecraft/client/entity/AbstractClientPlayer;DDDLjava/lang/String;FD)V", at = @At("TAIL"))
    private void yedelmod$renderNameLines(AbstractClientPlayer player, double x, double y, double z, String str, float oneDividedByThirtySevenPointFive, double distanceSquared, CallbackInfo ci) {
        NameLineEvent event = new NameLineEvent(NameLineEvent.NameRenderingMethod.STANDARD, player, distanceSquared);
        EventManager.INSTANCE.post(event);
        y += event.getVerticalAdjustment();
        for (String nameLine : event.getNameLines()) {
            y += 9 * 1.15F * 0.02666667;
            super.renderOffsetLivingLabel(player, x, y, z, nameLine, 0.02666667F, distanceSquared);
        }
    }
}
