package at.yedel.yedelmod.mixins.com.mojang.authlib.yggdrasil;



import at.yedel.yedelmod.config.YedelConfig;
import com.mojang.authlib.yggdrasil.YggdrasilMinecraftSessionService;
import gg.essential.lib.mixinextras.injector.WrapWithCondition;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;



@Mixin(YggdrasilMinecraftSessionService.class)
public abstract class MixinYggdrasilMinecraftSessionService {
	@WrapWithCondition(method = "getTextures", at = @At(value = "INVOKE", target = "Lorg/apache/logging/log4j/Logger;error(Ljava/lang/String;)V", ordinal = 0), remap = false)
	private boolean yedelmod$hideMissingSignatureErrors(Logger instance, String s) {
		return !YedelConfig.getInstance().hideMissingSignatureErrors;
	}
}
