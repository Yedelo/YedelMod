package at.yedel.yedelmod;



import at.yedel.yedelmod.config.YedelConfig;
import at.yedel.yedelmod.features.*;
import at.yedel.yedelmod.features.major.*;
import at.yedel.yedelmod.features.modern.ChangeTitle;
import at.yedel.yedelmod.features.modern.DrawBookBackground;
import at.yedel.yedelmod.features.modern.ItemSwings;
import at.yedel.yedelmod.features.ping.PingResponse;
import at.yedel.yedelmod.handlers.HypixelManager;
import at.yedel.yedelmod.launch.YedelModConstants;
import at.yedel.yedelmod.utils.ClickNotifications;
import at.yedel.yedelmod.utils.Threading;
import at.yedel.yedelmod.utils.update.UpdateManager;
import at.yedel.yedelmod.utils.update.UpdateManager.FeedbackMethod;
import cc.polyfrost.oneconfig.events.EventManager;
import cc.polyfrost.oneconfig.libs.universal.UMinecraft;
import cc.polyfrost.oneconfig.utils.commands.CommandManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLLoadCompleteEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.TimeUnit;



// Mod...
@Mod(
	modid = YedelModConstants.MOD_ID,
	name = YedelModConstants.MOD_NAME,
	version = YedelModConstants.MOD_VERSION,
	clientSideOnly = true
)
public class YedelMod {
	@Instance
	private static YedelMod INSTANCE;

	public static YedelMod getInstance() {
		return INSTANCE;
	}

	public static final Logger yedelog = LogManager.getLogger("YedelMod");

	@EventHandler
	public void init(FMLInitializationEvent event) {
		// Loads class. preload() exists for this but what ev
		YedelConfig.getInstance();
		HypixelManager.getInstance().setup();
		CommandManager.INSTANCE.registerCommand(YedelCommand.getInstance());

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
			RandomPlaceholder.getInstance(),
			RegexChatFilter.getInstance(),
			StrengthIndicators.getInstance(),
			TNTTagFeatures.getInstance()
		);
		registerEventListeners(
			ClickNotifications.getInstance(),
			YedelCheck.getInstance()
		);

		Threading.scheduleRepeat(() -> {
			if (UMinecraft.getWorld() != null) {
				YedelConfig.getInstance().playtimeMinutes++;
				YedelConfig.getInstance().save();
			}
		}, 1, TimeUnit.MINUTES);
	}

	@EventHandler
	public void checkForUpdates(FMLLoadCompleteEvent event) {
		if (YedelConfig.getInstance().automaticallyCheckForUpdates) {
			UpdateManager.getInstance().checkForUpdates(YedelConfig.getInstance().getUpdateSource(), FeedbackMethod.NOTIFICATIONS);
		}
	}

	private void registerEventListeners(Object... eventListeners) {
		for (Object eventListener: eventListeners) {
			MinecraftForge.EVENT_BUS.register(eventListener);
			EventManager.INSTANCE.register(eventListener);
		}
	}
}
