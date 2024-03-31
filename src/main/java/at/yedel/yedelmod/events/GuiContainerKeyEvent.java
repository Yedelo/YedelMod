package at.yedel.yedelmod.events;



import net.minecraftforge.fml.common.eventhandler.Event;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;



public class GuiContainerKeyEvent extends Event {
    public char typedChar;
    public int keyCode;
    public CallbackInfo ci;

    public GuiContainerKeyEvent(char typedChar, int keyCode, CallbackInfo ci) {
        this.typedChar = typedChar;
        this.keyCode = keyCode;
        this.ci = ci;
    }
}
