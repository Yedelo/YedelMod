package at.yedel.yedelmod.events;



import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.fml.common.eventhandler.Event;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;



@Cancelable
public class GuiContainerKeyEvent extends Event {
    public char typedChar;
    public int keyCode;

    public GuiContainerKeyEvent(char typedChar, int keyCode) {
        this.typedChar = typedChar;
        this.keyCode = keyCode;
    }
}
