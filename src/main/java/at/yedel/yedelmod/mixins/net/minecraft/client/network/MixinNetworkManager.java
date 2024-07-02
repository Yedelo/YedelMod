package at.yedel.yedelmod.mixins.net.minecraft.client.network;



import at.yedel.yedelmod.events.JoinGamePacketEvent;
import at.yedel.yedelmod.events.PacketEvent;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S01PacketJoinGame;
import net.minecraftforge.common.MinecraftForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;



@Mixin(NetworkManager.class)
public abstract class MixinNetworkManager {
    @Inject(method = "channelRead0(Lio/netty/channel/ChannelHandlerContext;Lnet/minecraft/network/Packet;)V", at = @At("HEAD"), cancellable = true)
    private void yedelmod$onReceivePacket(ChannelHandlerContext context, Packet<?> packet, CallbackInfo ci) {
        if (MinecraftForge.EVENT_BUS.post(new PacketEvent.ReceiveEvent(packet))) ci.cancel();
    }

    @Inject(method = "sendPacket(Lnet/minecraft/network/Packet;)V", at = @At("HEAD"), cancellable = true)
    private void yedelmod$onSendPacket(Packet packet, CallbackInfo ci) {
        if (packet instanceof S01PacketJoinGame) {
            MinecraftForge.EVENT_BUS.post(new JoinGamePacketEvent());
        }
        if (MinecraftForge.EVENT_BUS.post(new PacketEvent.SendEvent(packet))) ci.cancel();
    }
}
