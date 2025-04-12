package at.yedel.yedelmod.event.events;



import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Slot;
import org.polyfrost.oneconfig.api.event.v1.events.Event;



public class DrawSlotEvent implements Event {
	private final GuiContainer guiContainer;

	public GuiContainer getGuiContainer() {
		return guiContainer;
	}

	private final Slot slot;

	public Slot getSlot() {
		return slot;
	}

	public DrawSlotEvent(GuiContainer guiContainer, Slot slot) {
		this.guiContainer = guiContainer;
		this.slot = slot;
	}
}
