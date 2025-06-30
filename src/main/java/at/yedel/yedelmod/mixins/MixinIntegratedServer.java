package at.yedel.yedelmod.mixins;



import at.yedel.yedelmod.api.config.YedelConfig;
import net.minecraft.server.integrated.IntegratedServer;
import org.lwjgl.opengl.Display;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;



@Mixin(IntegratedServer.class)
public abstract class MixinIntegratedServer {
    @ModifyArg(method = "shareToLAN", at = @At(value = "INVOKE", target = "Lnet/minecraft/network/NetworkSystem;addLanEndpoint(Ljava/net/InetAddress;I)V"), index = 1)
    private int yedelmod$setLanPortTitle(int port) {
        if (YedelConfig.getInstance().enabled && YedelConfig.getInstance().changeWindowTitle) {
            Display.setTitle("Minecraft 1.8.9 - LAN Singleplayer - " + port);
        }
        return port;
    }
}
