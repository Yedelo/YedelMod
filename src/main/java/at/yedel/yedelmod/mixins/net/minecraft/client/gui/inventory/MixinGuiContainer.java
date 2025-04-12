package at.yedel.yedelmod.mixins.net.minecraft.client.gui.inventory;



import at.yedel.yedelmod.event.events.DrawSlotEvent;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Slot;
import org.polyfrost.oneconfig.api.event.v1.EventManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;



@Mixin(GuiContainer.class)
public abstract class MixinGuiContainer {
	@Inject(method = "drawSlot", at = @At("HEAD"))
	public void yedelmod$postDrawSlotEvent(Slot slotIn, CallbackInfo ci) {
        EventManager.INSTANCE.post(new DrawSlotEvent((GuiContainer) (Object) this, slotIn));
	}
}
