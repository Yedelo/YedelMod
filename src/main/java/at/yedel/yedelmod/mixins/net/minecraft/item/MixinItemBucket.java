package at.yedel.yedelmod.mixins.net.minecraft.item;



import at.yedel.yedelmod.config.YedelConfig;
import at.yedel.yedelmod.utils.SwingItemDuck;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBucket;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;



@Mixin(ItemBucket.class)
public abstract class MixinItemBucket {
	@Inject(method = "onItemRightClick", at = @At("RETURN"))
	private void yedelmod$swingOnBucketUse(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn, CallbackInfoReturnable<ItemStack> cir) {
		if (YedelConfig.getInstance().itemUseSwings) {
			if (itemStackIn != cir.getReturnValue()) {
				((SwingItemDuck) playerIn).yedelmod$swingItemLocally();
			}
		}
	}
}
