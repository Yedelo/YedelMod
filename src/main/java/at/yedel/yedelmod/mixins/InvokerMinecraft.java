package at.yedel.yedelmod.mixins;



import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;



@Mixin(Minecraft.class)
public interface InvokerMinecraft {
    @Invoker("rightClickMouse")
    void yedelmod$rightClickMouse();
}
