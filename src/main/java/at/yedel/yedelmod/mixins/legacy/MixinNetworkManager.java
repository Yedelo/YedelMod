package at.yedel.yedelmod.mixins.legacy;



import io.netty.channel.ChannelHandlerContext;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C17PacketCustomPayload;
import net.minecraft.network.play.server.S3FPacketCustomPayload;
import org.apache.logging.log4j.LogManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;



@Mixin(NetworkManager.class)
public abstract class MixinNetworkManager {
    @Inject(method = "dispatchPacket", at = @At("HEAD"))
    private void yedelmod$onPacketSend(Packet inPacket, GenericFutureListener<? extends Future<? super Void>>[] futureListeners, CallbackInfo ci) {
        if (inPacket instanceof C17PacketCustomPayload payload) {
            LogManager.getLogger().info("! Sent C17PacketCustomPayload {}", payload.getChannelName());
        }
    }

    @Inject(method = "channelRead0(Lio/netty/channel/ChannelHandlerContext;Lnet/minecraft/network/Packet;)V", at = @At("HEAD"))
    private void yedelmod$onPacketReceive(ChannelHandlerContext channelHandlerContext, Packet packet, CallbackInfo ci) {
        if (packet instanceof S3FPacketCustomPayload payload) {
            LogManager.getLogger().info("! Received S3FPacketCustomPayload {}", payload.getChannelName());
        }
    }
}
