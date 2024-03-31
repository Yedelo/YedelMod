package at.yedel.yedelmod.events;



import net.minecraft.inventory.Slot;
import net.minecraftforge.fml.common.eventhandler.Event;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;



public class GuiContainerClickEvent extends Event {
    public Slot slotIn;
    public int slotId;
    public int clickedButton;
    public int clickType;
    public CallbackInfo ci;

    public GuiContainerClickEvent(Slot slotIn, int slotId, int clickedButton, int clickType, CallbackInfo ci) {
        this.slotIn = slotIn;
        this.slotId = slotId;
        this.clickedButton = clickedButton;
        this.clickType = clickType;
        this.ci = ci;
    }
}
