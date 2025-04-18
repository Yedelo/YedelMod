package at.yedel.yedelmod;



import at.yedel.yedelmod.config.YedelConfig;
import at.yedel.yedelmod.features.*;
import at.yedel.yedelmod.features.major.*;
import at.yedel.yedelmod.features.modern.ChangeTitle;
import at.yedel.yedelmod.features.modern.DrawBookBackground;
import at.yedel.yedelmod.features.modern.ItemSwings;
import at.yedel.yedelmod.features.ping.PingResponse;
import at.yedel.yedelmod.hud.BedwarsXPHud;
import at.yedel.yedelmod.hud.BountyHuntingHud;
import at.yedel.yedelmod.hud.CustomTextHud;
import at.yedel.yedelmod.hud.MagicMilkTimeHud;
import at.yedel.yedelmod.launch.YedelModConstants;
import at.yedel.yedelmod.utils.Threading;
import at.yedel.yedelmod.utils.update.UpdateManager;
import at.yedel.yedelmod.utils.update.UpdateManager.FeedbackMethod;
import dev.deftu.omnicore.client.OmniClient;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLLoadCompleteEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.polyfrost.oneconfig.api.event.v1.EventManager;
import org.polyfrost.oneconfig.api.hud.v1.HudManager;
import org.polyfrost.oneconfig.api.hypixel.v1.HypixelUtils;

import java.util.concurrent.TimeUnit;



// Mod?
@Mod(
	modid = YedelModConstants.MOD_ID,
	name = YedelModConstants.MOD_NAME,
	version = YedelModConstants.MOD_VERSION,
	clientSideOnly = true
)
public class YedelMod {
	@Instance
	private static YedelMod instance;

	public static YedelMod getInstance() {
		return instance;
	}

	public static final Logger yedelog = LogManager.getLogger("YedelMod");

	@EventHandler
	public void init(FMLInitializationEvent event) {
		YedelConfig.getInstance().preload();
		// Loads HypixelUtils which sets up OneConfig's implementation of the Hypixel Mod API
		HypixelUtils.class.getName();

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
			LimboCreativeCheck.getInstance(),
			MarketSearch.getInstance(),
			PingResponse.getInstance(),
			RegexChatFilter.getInstance(),
			StrengthIndicators.getInstance(),
			TNTTagFeatures.getInstance(),

			YedelCheck.getInstance()
		);
		HudManager.register(
			CustomTextHud.getInstance(),
			BedwarsXPHud.getInstance(),
			MagicMilkTimeHud.getInstance(),
			BountyHuntingHud.getInstance()
		);
		YedelCommand.getInstance().register();

		Threading.scheduleRepeat(() -> {
			if (OmniClient.getWorld() != null) {
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
