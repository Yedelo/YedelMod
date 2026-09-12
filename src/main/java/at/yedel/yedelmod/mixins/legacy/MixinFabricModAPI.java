//? if ornithe {
package at.yedel.yedelmod.mixins.legacy;



import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.hypixel.modapi.fabric.FabricModAPI;
import net.hypixel.modapi.fabric.payload.ServerboundHypixelPayload;
import net.hypixel.modapi.packet.HypixelPacket;
import net.minecraft.client.Minecraft;
import net.ornithemc.osl.core.api.util.NamespacedIdentifier;
import net.ornithemc.osl.core.api.util.NamespacedIdentifiers;
import net.ornithemc.osl.networking.api.client.ClientPlayNetworking;
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
        ClientPlayNetworking.send(id, hypixelPayload);
        return true;
    }
}
//?}