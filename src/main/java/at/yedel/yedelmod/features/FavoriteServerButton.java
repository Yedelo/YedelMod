package at.yedel.yedelmod.features;



import at.yedel.yedelmod.config.YedelConfig;
import dev.deftu.omnicore.client.OmniClientMultiplayer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraftforge.client.event.GuiScreenEvent.ActionPerformedEvent;
import net.minecraftforge.client.event.GuiScreenEvent.InitGuiEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;



public class FavoriteServerButton {
	private FavoriteServerButton() {}

	private static final FavoriteServerButton instance = new FavoriteServerButton();

	public static FavoriteServerButton getInstance() {
		return instance;
	}

	private GuiButton favoriteServerButton;

	@SubscribeEvent
	public void addFavoriteServerButton(InitGuiEvent event) {
		if (YedelConfig.getInstance().favoriteServerButton && event.gui instanceof GuiMainMenu) {
			event.buttonList.add(favoriteServerButton = new GuiButton(1600, 5, 5, 125, 20, "Join Favorite Server"));
		}
	}

	@SubscribeEvent
	public void joinFavoriteServer(ActionPerformedEvent event) {
		if (event.button == favoriteServerButton) {
            OmniClientMultiplayer.connectTo(YedelConfig.getInstance().specifiedServer, "Favorite Server");
		}
	}
}
