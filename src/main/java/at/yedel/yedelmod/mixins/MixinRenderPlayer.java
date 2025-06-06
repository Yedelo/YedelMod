package at.yedel.yedelmod.mixins;



import at.yedel.yedelmod.config.YedelConfig;
import at.yedel.yedelmod.event.events.NameLineEvent;
import at.yedel.yedelmod.utils.NameLine;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelPlayer;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.client.renderer.entity.RendererLivingEntity;
import net.minecraft.item.EnumAction;
import net.minecraftforge.common.MinecraftForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.stream.Collectors;



@Mixin(RenderPlayer.class)
public abstract class MixinRenderPlayer extends RendererLivingEntity<AbstractClientPlayer> {
    public MixinRenderPlayer(RenderManager renderManagerIn, ModelBase modelBaseIn, float shadowSizeIn) {
        super(renderManagerIn, modelBaseIn, shadowSizeIn);
    }

    @Shadow
    public abstract ModelPlayer getMainModel();

    @Unique
    private static final int FONT_HEIGHT = 9;

    @Inject(method = "renderOffsetLivingLabel(Lnet/minecraft/client/entity/AbstractClientPlayer;DDDLjava/lang/String;FD)V", at = @At("TAIL"))
    private void yedelmod$renderNameLines(AbstractClientPlayer entityIn, double x, double y, double z, String str, float p_177069_9_, double p_177069_10_, CallbackInfo ci) {
        NameLineEvent event = new NameLineEvent();
        MinecraftForge.EVENT_BUS.post(event);
        for (NameLine nameLine : event.getNameLines().stream().filter((nameLine) -> nameLine.shouldShow(entityIn)).collect(Collectors.toList())) {
            y += (FONT_HEIGHT * 1.15 * (2F / 75F));
            renderLivingLabel(entityIn, nameLine.getText(), x, y, z, nameLine.getMaxDistance());
        }
    }

    @Inject(method = "setModelVisibilities", at = @At("TAIL"))
    private void yedelmod$setAutoBlockModel(AbstractClientPlayer clientPlayer, CallbackInfo ci) {
        if (YedelConfig.getInstance().enabled && YedelConfig.getInstance().clientSideAutoBlock && clientPlayer.isUser() && clientPlayer.getHeldItem() != null && clientPlayer.getHeldItem().getItemUseAction() == EnumAction.BLOCK) {
            getMainModel().heldItemRight = 3;
        }
    }
}
