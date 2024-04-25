package at.yedel.yedelmod.mixins.net.minecraftforge.client;



import at.yedel.yedelmod.config.YedelConfig;
import net.minecraft.client.gui.FontRenderer;
import net.minecraftforge.client.GuiIngameForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;



@Mixin(GuiIngameForge.class)
public class MixinGuiIngameForge {
    @Redirect(method = "renderRecordOverlay", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/FontRenderer;drawString(Ljava/lang/String;III)I"))
    public int yedelmod$renderWithShadow(FontRenderer instance, String text, int x, int y, int color) {
        return instance.drawString(text, x, y, color, YedelConfig.shadowActionBar);
    }
}
