package at.yedel.yedelmod.events;



import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.eventhandler.Event;



public class RenderItemEvent extends Event {
    public ItemStack itemStack;
    public int xPosition;
    public int yPosition;

    public RenderItemEvent(ItemStack itemStack, int xPosition, int yPosition) {
        this.itemStack = itemStack;
        this.xPosition = xPosition;
        this.yPosition = yPosition;
    }
}
