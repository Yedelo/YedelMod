package at.yedel.yedelmod.mixins.client.gui.inventory;



import at.yedel.yedelmod.events.DrawSlotEvent;
import at.yedel.yedelmod.events.GuiContainerClickEvent;
import at.yedel.yedelmod.events.GuiContainerKeyEvent;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Slot;
import net.minecraftforge.common.MinecraftForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;



@Mixin(GuiContainer.class)
public class MixinGuiContainer {
    @Inject(method = "drawSlot", at = @At("HEAD"))
    public void yedelmod$postDrawSlotEvent(Slot slotIn, CallbackInfo ci) {
        MinecraftForge.EVENT_BUS.post(new DrawSlotEvent(slotIn));
    }

    @Inject(method = "handleMouseClick", at = @At("HEAD"), cancellable = true)
    public void yedelmod$postGuiContainerClickEvent(Slot slotIn, int slotId, int clickedButton, int clickType, CallbackInfo ci) {
        if (MinecraftForge.EVENT_BUS.post(new GuiContainerClickEvent(slotIn, slotId, clickedButton, clickType))) ci.cancel();
    }

    @Inject(method = "keyTyped", at = @At("HEAD"), cancellable = true)
    public void yedelmod$postGuiContainerKeyEvent(char typedChar, int keyCode, CallbackInfo ci) {
        if (MinecraftForge.EVENT_BUS.post(new GuiContainerKeyEvent(typedChar, keyCode))) ci.cancel();
    }
}
