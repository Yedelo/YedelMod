package at.yedel.yedelmod.mixins;



import at.yedel.yedelmod.event.events.DrawSlotEvent;
import cc.polyfrost.oneconfig.events.EventManager;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Slot;
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
