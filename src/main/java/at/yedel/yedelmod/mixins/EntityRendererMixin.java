package at.yedel.yedelmod.mixins;



import at.yedel.yedelmod.utils.NameLineEvent;
import com.mojang.blaze3d.vertex.PoseStack;
import net.fabricmc.fabric.api.client.rendering.v1.RenderStateDataKey;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import org.polyfrost.oneconfig.api.event.v1.EventManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;



@Mixin(EntityRenderer.class)
public abstract class EntityRendererMixin<T extends Entity, S extends EntityRenderState> {
    @Unique
    private static final RenderStateDataKey<Entity> ENTITY_KEY = RenderStateDataKey.create(() -> "YedelMod entity holding");

    @Inject(method = "extractRenderState", at = @At("HEAD"))
    private void yedelmod$appendEntity(T entity, S state, float partialTicks, CallbackInfo ci) {
        state.setData(ENTITY_KEY, entity);
    }

    @Inject(
        method = "submitNameDisplay(Lnet/minecraft/client/renderer/entity/state/EntityRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;Lnet/minecraft/client/renderer/state/level/CameraRenderState;I)V",
        at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/vertex/PoseStack;popPose()V")
    )
    private void yedelmod$submitNameLines(S state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera, int offset, CallbackInfo ci) {
        Entity entity = state.getData(ENTITY_KEY);
        if (entity == null) {
            return;
        }
        NameLineEvent event = new NameLineEvent(entity, state.distanceToCameraSq);
        EventManager.INSTANCE.post(event);
        poseStack.translate(0, event.getVerticalAdjustment(), 0);
        for (Component nameLine : event.getNameLines()) {
            poseStack.translate(0, 9.0F * 1.15F * 0.025F, 0);
            submitNodeCollector.submitNameTag(
                poseStack, state.nameTagAttachment, offset, nameLine, !state.isDiscrete, state.lightCoords, /*? <= 26.1 {*/state.distanceToCameraSq,/*?}*/ camera
            );
        }
    }
}
