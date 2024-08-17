package at.yedel.yedelmod;



import java.util.concurrent.TimeUnit;

import at.yedel.yedelmod.config.YedelConfig;
import at.yedel.yedelmod.features.AutoGuildWelcome;
import at.yedel.yedelmod.features.DropperGG;
import at.yedel.yedelmod.features.FavoriteServerButton;
import at.yedel.yedelmod.features.HideMissingSignatureErrors;
import at.yedel.yedelmod.features.RegexChatFilter;
import at.yedel.yedelmod.features.YedelCommand;
import at.yedel.yedelmod.features.major.BedwarsFeatures;
import at.yedel.yedelmod.features.major.EasyAtlasVerdicts;
import at.yedel.yedelmod.features.major.MarketSearch;
import at.yedel.yedelmod.features.major.StrengthIndicators;
import at.yedel.yedelmod.features.major.TNTTagFeatures;
import at.yedel.yedelmod.features.major.ping.PingResponse;
import at.yedel.yedelmod.features.modern.ChangeTitle;
import at.yedel.yedelmod.features.modern.DrawBookBackground;
import at.yedel.yedelmod.features.modern.ItemSwings;
import at.yedel.yedelmod.handlers.HypixelManager;
import at.yedel.yedelmod.handlers.LogListenerFilter;
import at.yedel.yedelmod.handlers.YedelModPacketHandler;
import at.yedel.yedelmod.hud.HudManager;
import at.yedel.yedelmod.hud.impl.BedwarsXPHud;
import at.yedel.yedelmod.hud.impl.BountyHuntingHud;
import at.yedel.yedelmod.hud.impl.CustomTextHud;
import at.yedel.yedelmod.hud.impl.MagicMilkTimeHud;
import at.yedel.yedelmod.utils.Functions;
import at.yedel.yedelmod.utils.ThreadManager;
import at.yedel.yedelmod.utils.update.UpdateManager;
import at.yedel.yedelmod.utils.update.UpdateManager.FeedbackMethod;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLLoadCompleteEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent.ClientConnectedToServerEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.input.Keyboard;



@Mod(
	modid = YedelMod.modid,
	name = YedelMod.name,
	version = YedelMod.version,
	clientSideOnly = true,
	guiFactory = "at.yedel.yedelmod.config.forgeconfig.GuiFactory" // Overriden by main config (vigilance)
)
public class YedelMod {
	public static final String modid = "yedelmod";
	public static final String name = "YedelMod";
	public static final String version = "#version#";
	public static final Minecraft minecraft = Minecraft.getMinecraft();

	@Instance
	private static YedelMod instance;
	public static Logger logger = LogManager.getLogger("YedelMod");

	public static YedelMod getInstance() {
		return instance;
	}

	private KeyBinding ahSearchKeybind;

	public KeyBinding getAhSearchKeybind() {
		return ahSearchKeybind;
	}

	private KeyBinding bzSearchKeybind;

	public KeyBinding getBzSearchKeybind() {
		return bzSearchKeybind;
	}

	private KeyBinding insufficientKeybind;

	public KeyBinding getInsufficientKeybind() {
		return insufficientKeybind;
	}

	private KeyBinding sufficientKeybind;

	public KeyBinding getSufficientKeybind() {
		return sufficientKeybind;
	}

	@EventHandler
	public void init(FMLInitializationEvent event) {
		YedelConfig.getInstance().preload();

		ClientCommandHandler.instance.registerCommand(new YedelCommand());

		registerEventListeners(
			this,
			AutoGuildWelcome.getInstance(),
			BedwarsFeatures.getInstance(),
			ChangeTitle.getInstance(),
			DrawBookBackground.getInstance(),
			DropperGG.getInstance(),
			EasyAtlasVerdicts.getInstance(),
			FavoriteServerButton.getInstance(),
			Functions.getInstance().getEvents(),
			HideMissingSignatureErrors.getInstance(),
			ItemSwings.getInstance(),
			MarketSearch.getInstance(),
			PingResponse.getInstance(),
			RegexChatFilter.getInstance(),
			StrengthIndicators.getInstance(),
			TNTTagFeatures.getInstance(),
			YedelCheck.getInstance()
		);

		ThreadManager.scheduleRepeat(() -> {
			if (minecraft.theWorld != null) {
				YedelConfig.getInstance().playtimeMinutes++;
				YedelConfig.getInstance().save();
			}
		}, 1, TimeUnit.MINUTES);

		ThreadManager.scheduleRepeat(() -> {
			BedwarsFeatures.getInstance().decrementMagicMilkTime();
			BedwarsFeatures.getInstance().setMagicMilkTimeText("Magic Milk: §b" + BedwarsFeatures.getInstance().getMagicMilkTime() + "§as");
		}, 1, TimeUnit.SECONDS);

		ahSearchKeybind = new KeyBinding("AH search your held item", Keyboard.KEY_K, "YedelMod | Market Searches");
		bzSearchKeybind = new KeyBinding("BZ search your held item", Keyboard.KEY_L, "YedelMod | Market Searches");
		insufficientKeybind = new KeyBinding("Insufficient Evidence", Keyboard.KEY_O, "YedelMod | Atlas");
		sufficientKeybind = new KeyBinding("Evidence Without Doubt", Keyboard.KEY_P, "YedelMod | Atlas");
		ClientRegistry.registerKeyBinding(ahSearchKeybind);
		ClientRegistry.registerKeyBinding(bzSearchKeybind);
		ClientRegistry.registerKeyBinding(insufficientKeybind);
		ClientRegistry.registerKeyBinding(sufficientKeybind);

		((org.apache.logging.log4j.core.Logger) LogManager.getRootLogger()).addFilter(LogListenerFilter.getInstance());

		HypixelManager.getInstance().setup();

		MinecraftForge.EVENT_BUS.register(HudManager.getInstance());
		HudManager.getInstance().addHud(BedwarsXPHud.getInstance());
		HudManager.getInstance().addHud(BountyHuntingHud.getInstance());
		HudManager.getInstance().addHud(CustomTextHud.getInstance());
		HudManager.getInstance().addHud(MagicMilkTimeHud.getInstance());
	}

	@EventHandler
	public void checkForUpdates(FMLLoadCompleteEvent event) {
		if (YedelConfig.getInstance().autoCheckUpdates) {
			UpdateManager.getInstance().checkForUpdates(YedelConfig.getInstance().getUpdateSource(), FeedbackMethod.NOTIFICATIONS);
		}
	}

	@SubscribeEvent
	public void onServerConnect(ClientConnectedToServerEvent event) {
		event.manager.channel().pipeline().addBefore("packet_handler", "yedelmod_packet_handler", new YedelModPacketHandler());
	}

	private void registerEventListeners(Object... eventListeners) {
		for (Object eventListener: eventListeners) {
			MinecraftForge.EVENT_BUS.register(eventListener);
		}
	}
}
