package at.yedel.yedelmod.events;



import net.minecraft.inventory.Slot;
import net.minecraftforge.fml.common.eventhandler.Event;



public class DrawSlotEvent extends Event {
    public Slot slotIn;

    public DrawSlotEvent(Slot slotIn) {
        this.slotIn = slotIn;
    }
}
