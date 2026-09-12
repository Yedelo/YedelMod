//? if ornithe {
package at.yedel.yedelmod.mixins.legacy;



import net.ornithemc.osl.core.api.util.NamespacedIdentifier;
import net.ornithemc.osl.core.impl.util.NamespacedIdentifierImpl;
import net.ornithemc.osl.networking.api.StringChannelIdentifierParser;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;




@Mixin(StringChannelIdentifierParser.class)
public abstract class MixinStringChannelIdentifierParser {
    /**
     * @author Yedel
     * @reason happiness has to be fought for
     */
    @Overwrite
    public static NamespacedIdentifier fromString(String s) {
        int i = s.indexOf('|');

        if (i < 1) {
            i = s.indexOf(":");
            if (i < 1) {
                // allow null namespaces to support channel ids that do not conform
                // to OSL spec - MC did not enforce a strict spec before 1.13
                return new NamespacedIdentifierImpl("", s);
            }
        }
        return new NamespacedIdentifierImpl(s.substring(0, i), s.substring(i + 1));
    }
}
//?}
