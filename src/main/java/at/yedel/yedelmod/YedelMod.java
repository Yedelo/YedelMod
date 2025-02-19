package at.yedel.yedelmod;



import at.yedel.yedelmod.config.YedelConfig;
import at.yedel.yedelmod.features.*;
import at.yedel.yedelmod.features.major.*;
import at.yedel.yedelmod.features.modern.ChangeTitle;
import at.yedel.yedelmod.features.modern.DrawBookBackground;
import at.yedel.yedelmod.features.modern.ItemSwings;
import at.yedel.yedelmod.features.ping.PingResponse;
import at.yedel.yedelmod.handlers.HypixelManager;
import at.yedel.yedelmod.handlers.YedelModPacketHandler;
import at.yedel.yedelmod.launch.YedelModConstants;
import at.yedel.yedelmod.utils.ThreadManager;
import at.yedel.yedelmod.utils.update.UpdateManager;
import at.yedel.yedelmod.utils.update.UpdateManager.FeedbackMethod;
import cc.polyfrost.oneconfig.events.EventManager;
import cc.polyfrost.oneconfig.libs.universal.UMinecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLLoadCompleteEvent;
import org.lwjgl.input.Keyboard;

import java.util.concurrent.TimeUnit;


// Mod.
@Mod(
	modid = YedelModConstants.modid,
	name = YedelModConstants.name,
	version = YedelModConstants.version,
	clientSideOnly = true
)
public class YedelMod {
	@Instance
	private static YedelMod instance;

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
		// Loads class. preload() exists for this but what ev
		YedelConfig.getInstance();

		ClientCommandHandler.instance.registerCommand(new OldYedelCommand());
		registerEventListeners(
			this,
			AutoGuildWelcome.getInstance(),
			BedwarsFeatures.getInstance(),
			ChangeTitle.getInstance(),
			DrawBookBackground.getInstance(),
			DropperGG.getInstance(),
			EasyAtlasVerdicts.getInstance(),
			FavoriteServerButton.getInstance(),
			CustomHitParticles.getInstance(),
			ItemSwings.getInstance(),
			MarketSearch.getInstance(),
			PingResponse.getInstance(),
			RegexChatFilter.getInstance(),
			StrengthIndicators.getInstance(),
			TNTTagFeatures.getInstance(),
			YedelCheck.getInstance()
		);
		EventManager.INSTANCE.register(YedelModPacketHandler.getInstance());

		ThreadManager.scheduleRepeat(() -> {
			if (UMinecraft.getWorld() != null) {
				YedelConfig.getInstance().playtimeMinutes++;
				YedelConfig.getInstance().save();
			}
		}, 1, TimeUnit.MINUTES);

		ahSearchKeybind = new KeyBinding("AH search your held item", Keyboard.KEY_K, "YedelMod | Market Searches");
		bzSearchKeybind = new KeyBinding("BZ search your held item", Keyboard.KEY_L, "YedelMod | Market Searches");
		insufficientKeybind = new KeyBinding("Insufficient Evidence", Keyboard.KEY_O, "YedelMod | Atlas");
		sufficientKeybind = new KeyBinding("Evidence Without Doubt", Keyboard.KEY_P, "YedelMod | Atlas");
		ClientRegistry.registerKeyBinding(ahSearchKeybind);
		ClientRegistry.registerKeyBinding(bzSearchKeybind);
		ClientRegistry.registerKeyBinding(insufficientKeybind);
		ClientRegistry.registerKeyBinding(sufficientKeybind);

		HypixelManager.getInstance().setup();
	}

	@EventHandler
	public void checkForUpdates(FMLLoadCompleteEvent event) {
		if (YedelConfig.getInstance().autoCheckUpdates) {
			UpdateManager.getInstance().checkForUpdates(YedelConfig.getInstance().getUpdateSource(), FeedbackMethod.NOTIFICATIONS);
		}
	}

	private void registerEventListeners(Object... eventListeners) {
		for (Object eventListener: eventListeners) {
			MinecraftForge.EVENT_BUS.register(eventListener);
		}
	}
}
