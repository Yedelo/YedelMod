package at.yedel.yedelmod.mixins.net.minecraft.client.renderer;



import net.minecraft.client.renderer.ItemRenderer;
import org.spongepowered.asm.mixin.Mixin;



// Priority is 1024 to apply before mixins of Antimations < 2.2.1
@Mixin(value = ItemRenderer.class, priority = 1024)
public abstract class MixinItemRenderer {
	/*
	@Shadow
	private ItemStack itemToRender;

	@Redirect(method = "renderItemInFirstPerson", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/entity/AbstractClientPlayer;getItemInUseCount()I"))
	private int yedelmod$firstPersonAutoBlock(AbstractClientPlayer instance) {
		if (YedelConfig.getInstance().clientSideAutoBlock && itemToRender.getItemUseAction() == EnumAction.BLOCK)
			return 1;
		return instance.getItemInUseCount();
	}
	 */
}
