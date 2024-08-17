package at.yedel.yedelmod.features;



import at.yedel.yedelmod.config.YedelConfig;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiMultiplayer;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraftforge.client.event.GuiScreenEvent.ActionPerformedEvent;
import net.minecraftforge.client.event.GuiScreenEvent.InitGuiEvent;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import static at.yedel.yedelmod.YedelMod.minecraft;



public class FavoriteServerButton {
	private FavoriteServerButton() {}

	private static final FavoriteServerButton instance = new FavoriteServerButton();

	public static FavoriteServerButton getInstance() {
		return instance;
	}

	private GuiButton favoriteServerButton;

	@SubscribeEvent
	public void onInitGui(InitGuiEvent event) {
		if (YedelConfig.getInstance().buttonFavoriteServer && event.gui instanceof GuiMainMenu) {
			event.buttonList.add(favoriteServerButton = new GuiButton(1600, 5, 5, 125, 20, "Join Favorite Server"));
		}
	}

	@SubscribeEvent
	public void onPressServerButton(ActionPerformedEvent event) {
		if (event.button == favoriteServerButton) {
			FMLClientHandler.instance().connectToServer(new GuiMultiplayer(minecraft.currentScreen), new ServerData("Favorite Server", YedelConfig.getInstance().favoriteServer, false));
		}
	}
}
