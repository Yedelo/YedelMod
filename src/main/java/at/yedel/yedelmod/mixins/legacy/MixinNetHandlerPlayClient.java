package at.yedel.yedelmod.mixins.legacy;

import at.yedel.yedelmod.utils.InstanceAccessor;
import io.netty.buffer.Unpooled;
import net.minecraft.client.Minecraft;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.client.C17PacketCustomPayload;
import net.minecraft.network.play.server.S3FPacketCustomPayload;
import org.apache.logging.log4j.LogManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Objects;

@Mixin(NetHandlerPlayClient.class)
public abstract class MixinNetHandlerPlayClient {
    @Inject(method = "handleCustomPayload", at = @At("HEAD"))
    private void yedelmod$handleCustomPayload(S3FPacketCustomPayload packetIn, CallbackInfo ci) {
        String channel = packetIn.getChannelName();
        LogManager.getLogger().info("Received S3FPacketCustomPayload, channel {}", channel);
        if (Objects.equals(channel, "hypixel:hello")) {
            LogManager.getLogger().info("- Received hypixel:hello packet, attempting to send a hello packet back...");
            PacketBuffer buffer = new PacketBuffer(Unpooled.buffer());
            C17PacketCustomPayload packet = new C17PacketCustomPayload("hypixel:hello", buffer);
            try {
                Field $myNetworkManager = Arrays.stream(Minecraft.class.getDeclaredFields()).filter((field) -> field.getType() == NetworkManager.class).findFirst().orElse(null);
                $myNetworkManager.setAccessible(true);
                NetworkManager manager = (NetworkManager) $myNetworkManager.get(Minecraft.getMinecraft());
                manager.sendPacket(packet);
                LogManager.getLogger().info("First method succeeded");
            }
            catch (Exception e) {
                LogManager.getLogger().error("First method failed with exception: ", e);
            }
            try {
                Minecraft.getMinecraft().getNetHandler().addToSendQueue(packet);
                LogManager.getLogger().info("Second method succeeded");
            }
            catch (Exception e) {
                LogManager.getLogger().error("Second method failed with exception: ", e);
            }
            try {
                InstanceAccessor.lastInstance.sendPacket(packet);
                LogManager.getLogger().info("Third method succeeded");
            }
            catch (Exception e) {
                LogManager.getLogger().error("Third method failed with exception: ", e);
            }
        }
    }

}
