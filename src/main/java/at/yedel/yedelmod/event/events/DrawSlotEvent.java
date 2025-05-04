package at.yedel.yedelmod.event.events;



import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Slot;



public class DrawSlotEvent {
	private final GuiContainer guiContainer;
	private final Slot slot;

	public DrawSlotEvent(GuiContainer guiContainer, Slot slot) {
		this.guiContainer = guiContainer;
		this.slot = slot;
	}

	public GuiContainer getGuiContainer() {
		return guiContainer;
	}

	public Slot getSlot() {
		return slot;
	}
}
