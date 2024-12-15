package at.yedel.yedelmod.mixins.net.minecraft.client.renderer.entity;



import at.yedel.yedelmod.config.YedelConfig;
import at.yedel.yedelmod.events.RenderScoreEvent;
import gg.essential.lib.mixinextras.injector.ModifyExpressionValue;
import gg.essential.lib.mixinextras.sugar.Share;
import gg.essential.lib.mixinextras.sugar.ref.LocalRef;
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

    @Inject(method = "setModelVisibilities", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/InventoryPlayer;getCurrentItem()Lnet/minecraft/item/ItemStack;"))
    private void yedelmod$saveHeldItem(AbstractClientPlayer clientPlayer, CallbackInfo ci, @Share("heldItem") LocalRef<ItemStack> heldItemRef) {
        heldItemRef.set(clientPlayer.getHeldItem());
    }

    @ModifyExpressionValue(method = "setModelVisibilities", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/entity/AbstractClientPlayer;getItemInUseCount()I"))
    private int yedelmod$thirdPersonAutoBlock(int original, @Share("heldItem") LocalRef<ItemStack> heldItemRef) {
        if (YedelConfig.getInstance().clientSideAutoBlock && heldItemRef.get().getItemUseAction() == EnumAction.BLOCK)
            return 1;
        return original;
    }
}
