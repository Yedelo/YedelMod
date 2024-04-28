package at.yedel.yedelmod.mixins.net.minecraft.client;



import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;



@Mixin(Minecraft.class)
public interface AccessorMinecraft {
    @Accessor("serverName")
    public void setServerName(String serverName);

    @Accessor("serverPort")
    public void setServerPort(int serverPort);
}
