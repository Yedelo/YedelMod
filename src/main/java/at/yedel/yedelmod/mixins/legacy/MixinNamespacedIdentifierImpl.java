//? if ornithe {
package at.yedel.yedelmod.mixins.legacy;



import net.ornithemc.osl.core.api.util.NamespacedIdentifier;
import net.ornithemc.osl.core.impl.util.NamespacedIdentifierImpl;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;



@Mixin(NamespacedIdentifierImpl.class)
public abstract class MixinNamespacedIdentifierImpl implements NamespacedIdentifier {
    @Shadow @Final private String namespace;
    @Shadow @Final private String identifier;

    /**
     * @author Yedel
     * @reason happiness has to be fought for
     */
    @Overwrite
    public String toString() {
        if (namespace.isEmpty()) {
            return identifier;
        }
        else {
            return namespace + SEPARATOR + identifier;
        }
    }
}
//?}
