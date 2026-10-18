/*? if legacy {*//*
package at.yedel.yedelmod.mixins.legacy;



import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;



@Mixin(Minecraft.class)
public interface InvokerMinecraft {
    @Invoker("doUse")
    void yedelmod$doUse();
}*//*?}*/