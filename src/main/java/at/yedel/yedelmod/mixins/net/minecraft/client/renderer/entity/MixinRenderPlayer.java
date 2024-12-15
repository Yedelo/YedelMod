package at.yedel.yedelmod.mixins.net.minecraft.client.renderer.entity;



import at.yedel.yedelmod.config.YedelConfig;
import at.yedel.yedelmod.events.RenderScoreEvent;
import gg.essential.lib.mixinextras.injector.ModifyExpressionValue;
import gg.essential.lib.mixinextras.sugar.Local;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;



@Mixin(RenderPlayer.class)
public abstract class MixinRenderPlayer {
    @Inject(method = "renderOffsetLivingLabel(Lnet/minecraft/client/entity/AbstractClientPlayer;DDDLjava/lang/String;FD)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/entity/RenderPlayer;renderLivingLabel(Lnet/minecraft/entity/Entity;Ljava/lang/String;DDDI)V"))
    private void yedelmod$postRenderScoreEvent(AbstractClientPlayer entityIn, double x, double y, double z, String str, float incrementIThink, double distanceSqToEntityIThink, CallbackInfo ci) {
        MinecraftForge.EVENT_BUS.post(new RenderScoreEvent((RenderPlayer) (Object) this, entityIn, x, y, z, str, incrementIThink, distanceSqToEntityIThink));
    }

    @ModifyExpressionValue(method = "setModelVisibilities", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/entity/AbstractClientPlayer;getItemInUseCount()I"))
    private int yedelmod$clientSideAutoBlock(int original, @Local ItemStack heldItem) {
        if (YedelConfig.getInstance().clientSideAutoBlock && heldItem.getItemUseAction() == EnumAction.BLOCK) return 1;
        return original;
    }
}
