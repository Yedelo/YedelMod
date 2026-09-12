//? if ornithe {
package at.yedel.yedelmod.mixins.legacy;



import at.yedel.yedelmod.utils.InstanceAccessor;
import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import io.netty.buffer.Unpooled;
import net.hypixel.modapi.fabric.FabricModAPI;
import net.hypixel.modapi.fabric.payload.ClientboundHypixelPayload;
import net.hypixel.modapi.fabric.payload.ServerboundHypixelPayload;
import net.hypixel.modapi.packet.HypixelPacket;
import net.hypixel.modapi.serializer.PacketSerializer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.network.NetHandlerLoginClient;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.client.C17PacketCustomPayload;import net.ornithemc.osl.core.api.util.NamespacedIdentifier;
import net.ornithemc.osl.core.api.util.NamespacedIdentifiers;
import net.ornithemc.osl.networking.api.ChannelRegistry;
import net.ornithemc.osl.networking.api.StringChannelIdentifierParser;import net.ornithemc.osl.networking.api.client.ClientPlayNetworking;
import org.apache.logging.log4j.LogManager;
import org.slf4j.Logger;import org.spongepowered.asm.mixin.Final;import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;



@Mixin(FabricModAPI.class)
public abstract class MixinFabricModAPI {
    @Shadow public abstract boolean isConnectedToHypixel();

    @Shadow @Final private static Logger LOGGER;

    @Shadow private static void handleIncomingPayload(String identifier, ClientboundHypixelPayload payload) {throw new UnsupportedOperationException("Implemented via mixin");}

    /**
     * @author
     * @reason
     */
    @Overwrite
    private static void registerClientbound(String identifier) {
        try {
            var clientboundId = StringChannelIdentifierParser.fromString(identifier);
            LogManager.getLogger().info("Clientbound id is {}", clientboundId);

            // Also register the global receiver for handling incoming packets during PLAY and CONFIGURATION
            ChannelRegistry.register(clientboundId, true, false);
            ClientPlayNetworking.registerListener(clientboundId, () -> new ClientboundHypixelPayload(identifier), (minecraft, data) -> {
                LOGGER.debug("Received packet with identifier '{}', during PLAY", identifier);
                handleIncomingPayload(identifier, data);
            });
        } catch (IllegalArgumentException ignored) {
            // Ignored as this is fired when we reload the registrations and the packet is already registered
        }
    }

    /**
     * @author
     * @reason
     */
    @Overwrite
    private static void registerServerbound(String identifier) {
        try {
            ChannelRegistry.register(StringChannelIdentifierParser.fromString(identifier), false, true);
        } catch (IllegalArgumentException ignored) {

        }
    }

    /**
     * @author Yedel
     * @reason happiness has to be fought for
     */
    @Overwrite
    public boolean sendPacket(HypixelPacket packet) {
        if (!isConnectedToHypixel()) {
            return false;
        }
        ServerboundHypixelPayload hypixelPayload = new ServerboundHypixelPayload(packet);
        NamespacedIdentifier id = NamespacedIdentifiers.parse(packet.getIdentifier());
        LogManager.getLogger().info("Sending hypixel packet {}", packet);
        PacketBuffer buf = new PacketBuffer(Unpooled.buffer());
        PacketSerializer serializer = new PacketSerializer(buf);
        packet.write(serializer);
        InstanceAccessor.lastInstance.sendPacket(new C17PacketCustomPayload(packet.getIdentifier(), buf));
        return true;
    }
}
//?}