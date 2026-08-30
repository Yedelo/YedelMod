package at.yedel.yedelmod;



import at.yedel.yedelmod.config.YedelConfig;
import at.yedel.yedelmod.features.*;
import at.yedel.yedelmod.features.major.EasyAtlasVerdicts;
import at.yedel.yedelmod.features.major.StrengthIndicators;
import at.yedel.yedelmod.features.major.TNTTagFeatures;
import at.yedel.yedelmod.features.ping.PingResponse;
import at.yedel.yedelmod.hud.BountyHuntingHud;
import at.yedel.yedelmod.hud.CustomTextHud;
import at.yedel.yedelmod.launch.YedelModConstants;
import at.yedel.yedelmod.utils.Threading;

import net.minecraft.client.Minecraft;
//? if forge {
/*import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLLoadCompleteEvent;
import at.yedel.yedelmod.utils.update.UpdateManager;
import at.yedel.yedelmod.utils.update.UpdateSource;
import at.yedel.yedelmod.features.CustomHitParticles;
*///?}
//? else if fabric {
 import net.fabricmc.api.ClientModInitializer;
//?}
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
//? if v0 {
/*import cc.polyfrost.oneconfig.events.EventManager;
import cc.polyfrost.oneconfig.utils.commands.CommandManager;
*///? else {
import org.polyfrost.oneconfig.api.commands.v1.CommandManager;
 import org.polyfrost.oneconfig.api.event.v1.EventManager;
 import org.polyfrost.oneconfig.api.hud.v1.HudManager;import org.polyfrost.oneconfig.api.platform.v1.Platform;import org.polyfrost.oneconfig.api.platform.v1.ScreenPlatform;
import org.polyfrost.oneconfig.api.platform.v1.internal.ScreenPlatformImpl;
    //?}

import java.util.concurrent.TimeUnit;



// Mod
/*? if forge {*//*
@Mod(
	modid = YedelModConstants.MOD_ID,
	name = YedelModConstants.MOD_NAME,
	version = YedelModConstants.MOD_VERSION,
	clientSideOnly = true
)
*//*?}*/
public class YedelMod /*? if fabric {*/ implements ClientModInitializer /*?}*/ {
	private static YedelMod INSTANCE;

	public static YedelMod getInstance() {
		return INSTANCE;
	}

	public YedelMod() {
		INSTANCE = this;
	}

	public static final Logger yedelog = LogManager.getLogger("YedelMod");

	private void initialize() {
		YedelConfig.getInstance();
		CommandManager.register(YedelCommand.getInstance());

		registerEventListeners(
			AutoGuildWelcome.getInstance(),
			DropperGG.getInstance(),
			EasyAtlasVerdicts.getInstance(),
			PingResponse.getInstance(),
			RegexChatFilter.getInstance(),
			StrengthIndicators.getInstance(),
			TNTTagFeatures.getInstance()
		);
		RandomPlaceholder.getInstance();
		LimboCreative.getInstance();
		//? if v1
		HudManager.register(new BountyHuntingHud(), new CustomTextHud());

		Threading.scheduleRepeat(() -> {
			//~ if modern 'Minecraft.getMinecraft().thePlayer' -> 'Minecraft.getInstance().player'
			if (Minecraft.getInstance().player != null) {
				YedelConfig.getInstance().playtimeMinutes++;
				YedelConfig.getInstance().save();
			}
		}, 1, TimeUnit.MINUTES);
	}

	/*? if forge {*//*
	public final UpdateManager updateManager = new UpdateManager(
		"YedelMod", YedelModConstants.MOD_VERSION, "yedelmod", "Yedelo/YedelMod", YedelModConstants.yedelogo
	);

	@Mod.EventHandler
	public void init(FMLInitializationEvent event) {
		initialize();
		registerEventListeners(this, RandomPlaceholder.getInstance(), CustomHitParticles.getInstance());
	}

	@Mod.EventHandler
	public void checkForUpdates(FMLLoadCompleteEvent event) {
		if (YedelConfig.getInstance().enabled && YedelConfig.getInstance().automaticallyCheckForUpdates) {
			updateManager.checkForUpdates(YedelConfig.getInstance().getUpdateSource(), UpdateManager.FeedbackMethod.NOTIFICATIONS);
		}
	}

	private void registerEventListeners(Object... eventListeners) {
		for (Object eventListener: eventListeners) {
			MinecraftForge.EVENT_BUS.register(eventListener);
			EventManager.INSTANCE.register(eventListener);
		}
	}

	public UpdateManager getUpdateManager() {
		return updateManager;
	}
	*//*?} else {*/
	
	@Override
	public void onInitializeClient() {
		initialize();
	}

	private void registerEventListeners(Object... eventListeners) {
		for (Object eventListener: eventListeners) {
			// fabric events are registered just by the object existing
			EventManager.INSTANCE.register(eventListener);
		}
	}
	 
	/*?}*/
}
