package at.yedel.yedelmod.events;



import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Slot;
import net.minecraftforge.fml.common.eventhandler.Event;



public class DrawSlotEvent extends Event {
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
