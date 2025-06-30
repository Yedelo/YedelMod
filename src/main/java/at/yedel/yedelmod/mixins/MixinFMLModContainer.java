package at.yedel.yedelmod.mixins;



import net.minecraftforge.fml.common.FMLModContainer;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.Map;
import java.util.Objects;



@Mixin(FMLModContainer.class)
@Debug(export = true)
public abstract class MixinFMLModContainer {
    @Unique private static FMLModContainer yedelmod$firstTextile;

    @Redirect(method = "getModId", at = @At(value = "INVOKE", target = "Ljava/util/Map;get(Ljava/lang/Object;)Ljava/lang/Object;"))
    private Object yedelmod$allowDuplicateTextile(Map instance, Object o) {
        Object original = instance.get(o);
        if (Objects.equals(original, "textile")) {
            FMLModContainer castedThis = (FMLModContainer) (Object) this;
            if (yedelmod$firstTextile == null) {
                yedelmod$firstTextile = castedThis;
            }
            if (castedThis == yedelmod$firstTextile) {
                return "textile";
            }
            else {
                return "textile2";
            }
        }
        return original;
    }
}
