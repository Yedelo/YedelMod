package at.yedel.yedelmod.mixins.client.network;



import at.yedel.yedelmod.events.PacketEvent;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S2DPacketOpenWindow;
import net.minecraft.network.play.server.S2FPacketSetSlot;
import net.minecraft.network.play.server.S37PacketStatistics;
import net.minecraft.network.play.server.S3APacketTabComplete;
import net.minecraft.network.play.server.S3BPacketScoreboardObjective;
import net.minecraftforge.common.MinecraftForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;



@Mixin(NetHandlerPlayClient.class)
public class MixinNetHandlerPlayClient {
    @Inject(method = "addToSendQueue", at = @At("HEAD"))
    public void yedelmod$postPacketSendEvent(Packet p_147297_1_, CallbackInfo ci) {
        MinecraftForge.EVENT_BUS.post(new PacketEvent.SendEvent(p_147297_1_, ci));
    }

    @Inject(method = "handleScoreboardObjective", at = @At("HEAD"))
    public void yedelmod$postScoreboardObjectivePacketEvent(S3BPacketScoreboardObjective packetIn, CallbackInfo ci) {
        MinecraftForge.EVENT_BUS.post(new PacketEvent.ReceiveEvent(packetIn, ci));
    }

    @Inject(method = "handleTabComplete", at = @At("HEAD"))
    public void yedelmod$postTabPacketEvent(S3APacketTabComplete packetIn, CallbackInfo ci) {
        MinecraftForge.EVENT_BUS.post(new PacketEvent.ReceiveEvent(packetIn, ci));
    }

    @Inject(method = "handleStatistics", at = @At("HEAD"))
    public void yedelmod$postStatisticsPacketEvent(S37PacketStatistics packetIn, CallbackInfo ci) {
        MinecraftForge.EVENT_BUS.post(new PacketEvent.ReceiveEvent(packetIn, ci));
    }

    @Inject(method = "handleSetSlot", at = @At("HEAD"))
    public void yedelmod$postSetSlotPacketEvent(S2FPacketSetSlot packetIn, CallbackInfo ci) {
        MinecraftForge.EVENT_BUS.post(new PacketEvent.ReceiveEvent(packetIn, ci));
    }

    @Inject(method = "handleOpenWindow", at = @At("HEAD"))
    public void yedelmod$postOpenWindowPacketEvent(S2DPacketOpenWindow packetIn, CallbackInfo ci) {
        MinecraftForge.EVENT_BUS.post(new PacketEvent.ReceiveEvent(packetIn, ci));
    }
}
