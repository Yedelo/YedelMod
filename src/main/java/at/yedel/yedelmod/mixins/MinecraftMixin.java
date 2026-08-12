package at.yedel.yedelmod.mixins;



import at.yedel.yedelmod.features.YedelCommand;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;



@Mixin(Minecraft.class)
public abstract class MinecraftMixin {
    @Inject(method = "createTitle", at = @At("HEAD"), cancellable = true)
    private void yedelmod$settitle(CallbackInfoReturnable<String> cir) {
        String displayTitle = YedelCommand.getInstance().getDisplayTitle();
        if (displayTitle != null) {
            cir.setReturnValue(displayTitle);
        }
    }
}
