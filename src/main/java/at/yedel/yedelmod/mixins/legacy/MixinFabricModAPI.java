//? if ornithe {
/*package at.yedel.yedelmod.mixins.legacy;



import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.hypixel.modapi.fabric.FabricModAPI;
import net.hypixel.modapi.fabric.payload.ClientboundHypixelPayload;
import net.hypixel.modapi.packet.HypixelPacket;
import net.minecraft.network.play.client.C17PacketCustomPayload;
import net.minecraft.network.play.server.S3FPacketCustomPayload;import net.ornithemc.osl.core.api.util.NamespacedIdentifier;import net.ornithemc.osl.networking.api.client.ClientPlayNetworking;import org.apache.logging.log4j.LogManager;
import org.polyfrost.oneconfig.api.event.v1.EventManager;
import org.polyfrost.oneconfig.api.event.v1.events.PacketEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Coerce;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import java.util.function.Supplier;



@Mixin(FabricModAPI.class)
public abstract class MixinFabricModAPI {
    @Shadow private static void handleIncomingPayload(String identifier, ClientboundHypixelPayload payload) {throw new UnsupportedOperationException("Implemented via mixin");}

    @WrapOperation(method = "registerClientbound", at = @At(value = "INVOKE", target = "Lnet/ornithemc/osl/networking/api/ChannelRegistry;register(Lnet/ornithemc/osl/core/api/util/NamespacedIdentifier;ZZ)Lnet/ornithemc/osl/core/api/util/NamespacedIdentifier;"))
    private static NamespacedIdentifier yedelmod$register$1(NamespacedIdentifier channel, boolean clientbound, boolean serverbound, Operation<NamespacedIdentifier> original) {
        LogManager.getLogger().info("[ghissueclient] register({}, {}, {})", channel, clientbound, serverbound);
        return original.call(channel, clientbound, serverbound);
    }

    @WrapOperation(method = "registerClientbound", at = @At(value = "INVOKE", target = "Lnet/ornithemc/osl/networking/api/client/ClientPlayNetworking;registerListener(Lnet/ornithemc/osl/core/api/util/NamespacedIdentifier;Ljava/util/function/Supplier;Lnet/ornithemc/osl/networking/api/client/ClientPacketListener$Payload;)V"))
    private static void yedelmod$register$2(NamespacedIdentifier channel, Supplier initializer, @Coerce Object listener, Operation<Void> original, @Local(argsOnly = true) String identifier, @Local NamespacedIdentifier clientboundId) {
        LogManager.getLogger().info("[ghissueclient] overriding registerListener({}, {}, {})", channel, initializer, listener);
        ClientPlayNetworking.registerListener(clientboundId, () -> new ClientboundHypixelPayload(identifier), (minecraft, data) -> {
            LogManager.getLogger().info("Received packet with identifier '{}', during PLAY", identifier);
            handleIncomingPayload(identifier, data);
        });
    }

    @Inject(method = "lambda$registerClientbound$3", at = @At("HEAD"))
    private static void yedelmod$inject$0(String identifier, @Coerce Object minecraft, ClientboundHypixelPayload data, CallbackInfo ci) {
        LogManager.getLogger().info("[ghissueclient] registerListener 3rd argument({}, {}, {})", identifier, minecraft, data);
    }

    @Inject(method = "onInitializeClient", at = @At("HEAD"))
    private void yedelmod$inject$1(CallbackInfo ci) {
        LogManager.getLogger().info("[ghissueclient] onInitializeClient");
    }

    @Inject(method = "onInit", at = @At("HEAD"))
    private void yedelmod$inject$2(CallbackInfo ci) {
        LogManager.getLogger().info("[ghissueclient] onInit");
    }

    @Inject(method = "sendPacket", at = @At("HEAD"))
    private void yedelmod$inject$3(HypixelPacket packet, CallbackInfoReturnable<Boolean> cir) {
        LogManager.getLogger().info("[ghissueclient] sendPacket");
    }

    @Inject(method = "isConnectedToHypixel", at = @At("HEAD"))
    private void yedelmod$inject$4(CallbackInfoReturnable<Boolean> cir) {
        LogManager.getLogger().info("[ghissueclient] isConnectedToHypixel");
    }

    @Inject(method = "reloadRegistrations", at = @At("HEAD"))
    private static void yedelmod$inject$5(CallbackInfo ci) {
        LogManager.getLogger().info("[ghissueclient] reloadRegistrations");
    }

    @Inject(method = "registerClientbound", at = @At("HEAD"))
    private static void yedelmod$inject$6(String identifier, CallbackInfo ci) {
        LogManager.getLogger().info("[ghissueclient] registerClientbound({})", identifier);
    }

    @Inject(method = "handleIncomingPayload", at = @At("HEAD"))
    private static void yedelmod$inject$7(String identifier, ClientboundHypixelPayload payload, CallbackInfo ci) {
        LogManager.getLogger().info("[ghissueclient] handleIncomingPayload({}, {})", identifier, payload);
    }

    @Inject(method = "registerServerbound", at = @At("HEAD"))
    private static void yedelmod$inject$8(String identifier, CallbackInfo ci) {
        LogManager.getLogger().info("[ghissueclient] registerServerbound({})", identifier);
    }

    @Inject(method = "registerDebug", at = @At("HEAD"))
    private static void yedelmod$inject$9(CallbackInfo ci) {
        LogManager.getLogger().info("[ghissueclient] registerDebug");
    }
}
*///?}
