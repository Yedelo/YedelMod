package at.yedel.yedelmod;



import at.yedel.yedelmod.config.YedelConfig;
import at.yedel.yedelmod.features.*;
import at.yedel.yedelmod.features.major.EasyAtlasVerdicts;
import at.yedel.yedelmod.features.major.StrengthIndicators;
import at.yedel.yedelmod.features.major.TNTTagFeatures;
import at.yedel.yedelmod.features.ping.PingResponse;
import at.yedel.yedelmod.hud.BountyHuntingHud;
import at.yedel.yedelmod.hud.CustomTextHud;
import at.yedel.yedelmod.utils.Threading;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.Minecraft;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.polyfrost.oneconfig.api.commands.v1.CommandManager;
import org.polyfrost.oneconfig.api.event.v1.EventManager;
import org.polyfrost.oneconfig.api.hud.v1.HudManager;

import java.util.concurrent.TimeUnit;



// Mod
public class YedelMod implements ClientModInitializer {
	private static YedelMod INSTANCE;

	public static YedelMod getInstance() {
		return INSTANCE;
	}

	public YedelMod() {
		INSTANCE = this;
	}

	public static final Logger yedelog = LogManager.getLogger("YedelMod");

	@Override
	public void onInitializeClient() {
		// Loads class. preload() exists for this but what ev
		YedelConfig.getInstance().preload();
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
		HudManager.register(new BountyHuntingHud(), new CustomTextHud());

		Threading.scheduleRepeat(() -> {
			if (Minecraft.getInstance().player != null) {
				YedelConfig.getInstance().playtimeMinutes++;
				YedelConfig.getInstance().save();
			}
		}, 1, TimeUnit.MINUTES);
	}

	private void registerEventListeners(Object... eventListeners) {
		for (Object eventListener: eventListeners) {
			// fabric events are registered just by the object existing
			EventManager.INSTANCE.register(eventListener);
		}
	}
}
