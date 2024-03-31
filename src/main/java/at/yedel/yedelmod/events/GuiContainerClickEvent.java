package at.yedel.yedelmod.events;



import net.minecraft.inventory.Slot;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.fml.common.eventhandler.Event;



@Cancelable
public class GuiContainerClickEvent extends Event {
    public Slot slotIn;
    public int slotId;
    public int clickedButton;
    public int clickType;

    public GuiContainerClickEvent(Slot slotIn, int slotId, int clickedButton, int clickType) {
        this.slotIn = slotIn;
        this.slotId = slotId;
        this.clickedButton = clickedButton;
        this.clickType = clickType;
    }
}
