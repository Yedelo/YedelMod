package at.yedel.yedelmod.mixins.net.minecraft.client.renderer;



import at.yedel.yedelmod.config.YedelConfig;
import gg.essential.lib.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;



@Mixin(ItemRenderer.class)
public abstract class MixinItemRenderer {
	@Shadow
	private ItemStack itemToRender;

	@ModifyExpressionValue(method = "renderItemInFirstPerson", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/entity/AbstractClientPlayer;getItemInUseCount()I"))
	private int yedelmod$clientSideAutoBlock(int original) {
		if (YedelConfig.getInstance().clientSideAutoBlock && itemToRender.getItemUseAction() == EnumAction.BLOCK)
			return 1;
		return original;
	}
}
