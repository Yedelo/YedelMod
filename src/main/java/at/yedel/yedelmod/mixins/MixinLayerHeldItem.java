package at.yedel.yedelmod.mixins;



import at.yedel.yedelmod.api.config.YedelConfig;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.layers.LayerHeldItem;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;



@Mixin(LayerHeldItem.class)
public abstract class MixinLayerHeldItem {
	@Inject(method = "doRenderLayer", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/ItemRenderer;renderItem(Lnet/minecraft/entity/EntityLivingBase;Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/renderer/block/model/ItemCameraTransforms$TransformType;)V"))
	private void yedelmod$setAutoBlockRotations(EntityLivingBase entitylivingbaseIn, float f, float g, float partialTicks, float h, float i, float j, float scale, CallbackInfo ci) {
        if (YedelConfig.getInstance().enabled && YedelConfig.getInstance().rotateSwordInThirdPerson && YedelConfig.getInstance().clientSideAutoBlock && entitylivingbaseIn instanceof EntityPlayerSP) {
			ItemStack heldItem = entitylivingbaseIn.getHeldItem();
			if (heldItem != null && heldItem.getItemUseAction() == EnumAction.BLOCK && !((EntityPlayerSP) entitylivingbaseIn).isUsingItem()) {
				GlStateManager.translate(0.05F, 0.0F, -0.1F);
				GlStateManager.rotate(-50.0F, 0.0F, 1.0F, 0.0F);
				GlStateManager.rotate(-10.0F, 1.0F, 0.0F, 0.0F);
				GlStateManager.rotate(-60.0F, 0.0F, 0.0F, 1.0F);
			}
		}
	}
}
