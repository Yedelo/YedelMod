//? if ornithe {
package at.yedel.yedelmod.mixins.legacy;



import at.yedel.yedelmod.utils.InstanceAccessor;
import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import io.netty.buffer.Unpooled;
import net.hypixel.modapi.fabric.FabricModAPI;
import net.hypixel.modapi.fabric.payload.ServerboundHypixelPayload;
import net.hypixel.modapi.packet.HypixelPacket;
import net.hypixel.modapi.serializer.PacketSerializer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.network.NetHandlerLoginClient;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.client.C17PacketCustomPayload;import net.ornithemc.osl.core.api.util.NamespacedIdentifier;
import net.ornithemc.osl.core.api.util.NamespacedIdentifiers;
import net.ornithemc.osl.networking.api.client.ClientPlayNetworking;
import org.apache.logging.log4j.LogManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;



@Mixin(FabricModAPI.class)
public abstract class MixinFabricModAPI {
    @Shadow public abstract boolean isConnectedToHypixel();

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