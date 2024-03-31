package at.yedel.yedelmod.mixins.client.multiplayer;



import at.yedel.yedelmod.config.YedelConfig;
import net.minecraft.client.multiplayer.WorldClient;
import org.lwjgl.opengl.Display;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;



@Mixin(WorldClient.class)
public abstract class MixinWorldClient {
    // for some reason, using the FML event of disconnecting from the server freezes
    @Inject(method = "sendQuittingDisconnectingPacket", at = @At("HEAD"))
    public void yedelmod$onQuit(CallbackInfo ci) {
        if (YedelConfig.changeTitle || Display.getTitle() != "Minecraft 1.8.9") {
            Display.setTitle("Minecraft 1.8.9");
        }
    }
}
