package at.yedel.yedelmod.mixins.net.minecraft.client.gui;



import at.yedel.yedelmod.config.YedelConfig;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiMultiplayer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraftforge.fml.client.FMLClientHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static at.yedel.yedelmod.YedelMod.minecraft;



@Mixin(GuiMainMenu.class)
public abstract class MixinGuiMainMenu extends GuiScreen {
    @Unique
    private GuiButton yedelmod$favoriteServerButton;

    @Inject(method = "initGui", at = @At("TAIL"))
    private void yedelmod$addGuiButtons(CallbackInfo ci) {
        if (YedelConfig.getInstance().buttonFavoriteServer) {
            this.buttonList.add(yedelmod$favoriteServerButton = new GuiButton(1600, 5, 5, 125, 20, "Join Favorite Server"));
        }
    }

    @Inject(method = "actionPerformed", at = @At("TAIL"))
    private void yedelmod$registerButtonPress(GuiButton button, CallbackInfo ci) {
        if (button == yedelmod$favoriteServerButton) {
            minecraft.addScheduledTask(() -> {
                FMLClientHandler.instance().connectToServer(
                        new GuiMultiplayer(new GuiMainMenu()), new ServerData("Favorite Server", YedelConfig.getInstance().favoriteServer, false)
                );
            });
        }
    }
}
