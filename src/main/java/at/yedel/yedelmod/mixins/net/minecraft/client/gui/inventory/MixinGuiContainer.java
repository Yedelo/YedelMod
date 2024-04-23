package at.yedel.yedelmod.mixins.net.minecraft.client.gui.inventory;



import at.yedel.yedelmod.events.DrawSlotEvent;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Slot;
import net.minecraftforge.common.MinecraftForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;



@Mixin(GuiContainer.class)
public abstract class MixinGuiContainer {
    @Inject(method = "drawSlot", at = @At("HEAD"))
    private void yedelmod$postDrawSlotEvent(Slot slotIn, CallbackInfo ci) {
        MinecraftForge.EVENT_BUS.post(new DrawSlotEvent(slotIn));
    }
}
