/*? if legacy {*//*
package at.yedel.yedelmod.mixins.legacy;



import at.yedel.yedelmod.utils.NameLineEvent;
import net.minecraft.client.entity.living.player.ClientPlayerEntity;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.PlayerRenderer;
import net.minecraft.client.render.model.Model;
import org.polyfrost.oneconfig.api.event.v1.EventManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;



/^*
 * Implements the {@link at.yedel.yedelmod.utils.NameLineEvent} via name rendering method STANDARD.
 ^/
@Mixin(PlayerRenderer.class)
public abstract class MixinPlayerRenderer extends LivingEntityRenderer {
    public MixinPlayerRenderer(EntityRenderDispatcher entityRenderDispatcher, Model model, float shadow) {
        super(entityRenderDispatcher, model, shadow);
    }

    /^*
     * Injects after the score objective and player name labels render, calling the vanilla nametag rendering method for each name line.
     * This respects the local y variable in the method, and we add on to it with vertical adjustments and the standard modifier for each line.
     ^/
    @Inject(method = "renderNameTag(Lnet/minecraft/client/entity/living/player/ClientPlayerEntity;DDDLjava/lang/String;FD)V", at = @At("TAIL"))
    private void yedelmod$renderNameLines(ClientPlayerEntity entity, double dx, double dy, double dz, String name, float tickDelta, double squaredDistance, CallbackInfo ci) {
        NameLineEvent event = new NameLineEvent(NameLineEvent.NameRenderingMethod.STANDARD, entity, squaredDistance);
        EventManager.INSTANCE.post(event);
        dy += event.getVerticalAdjustment();
        for (String nameLine : event.getNameLines()) {
            dy += 9 * 1.15F * 0.02666667;
            super.renderNameTag(entity, dx, dy, dz, nameLine, 0.02666667F, squaredDistance);
        }
    }
}*//*?}*/