package at.yedel.yedelmod.events;



import net.minecraft.inventory.Slot;
import net.minecraftforge.fml.common.eventhandler.Event;



public class DrawSlotEvent extends Event {
    private final Slot slot;

    public Slot getSlot() {
        return slot;
    }

    public DrawSlotEvent(Slot slot) {
        this.slot = slot;
    }
}
