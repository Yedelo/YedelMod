package at.yedel.yedelmod.mixins.net.minecraft.client.network;



import at.yedel.yedelmod.features.major.ping.PingResponse;
import at.yedel.yedelmod.features.major.ping.PingSender;
import net.minecraft.network.status.server.S00PacketServerInfo;
import net.minecraft.network.status.server.S01PacketPong;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;



@Mixin(targets = "net.minecraft.client.network.OldServerPinger$1")
public abstract class MixinOldServerPinger {
	@Inject(method = "handleServerInfo", at = @At(value = "INVOKE", target = "Lnet/minecraft/network/NetworkManager;sendPacket(Lnet/minecraft/network/Packet;)V"))
	private void yedelmod$handleServerConnection(S00PacketServerInfo packetIn, CallbackInfo ci) {
		PingSender.getInstance().lastTime = System.nanoTime();
	}

	@Inject(method = "handlePong", at = @At("HEAD"))
	private void yedelmod$handleServerPong(S01PacketPong packetIn, CallbackInfo ci) {
		PingResponse.getInstance().onServerListResponse();
	}
}
