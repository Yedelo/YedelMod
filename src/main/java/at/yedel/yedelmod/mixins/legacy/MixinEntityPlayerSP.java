//? if legacy {
/*package at.yedel.yedelmod.mixins.legacy;



import at.yedel.yedelmod.features.RandomPlaceholder;
import net.minecraft.client.entity.EntityPlayerSP;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;



@Mixin(EntityPlayerSP.class)
public abstract class MixinEntityPlayerSP {
    @ModifyVariable(method = "sendChatMessage", at = @At("HEAD"), ordinal = 0, argsOnly = true)
    private String yedelmod$randomPlaceholder(String original) {
        if (RandomPlaceholder.getInstance().should()) {
            return RandomPlaceholder.getInstance().replace(original);
        }
        return original;
    }
}
*///?}
