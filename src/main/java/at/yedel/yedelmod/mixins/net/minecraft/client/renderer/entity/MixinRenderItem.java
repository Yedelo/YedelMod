package at.yedel.yedelmod.mixins.net.minecraft.client.renderer.entity;



import at.yedel.yedelmod.events.RenderItemEvent;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;



@Mixin(RenderItem.class)
public abstract class MixinRenderItem {
    @Inject(method = "renderItemAndEffectIntoGUI", at = @At("HEAD"))
    private void yedelmod$postRenderItemEvent(ItemStack stack, int xPosition, int yPosition, CallbackInfo ci) {
        MinecraftForge.EVENT_BUS.post(new RenderItemEvent(stack, xPosition, yPosition));
    }
}
