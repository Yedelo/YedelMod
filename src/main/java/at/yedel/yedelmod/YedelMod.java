package at.yedel.yedelmod;



import at.yedel.yedelmod.config.YedelConfig;
import at.yedel.yedelmod.features.*;
import at.yedel.yedelmod.features.major.EasyAtlasVerdicts;
import at.yedel.yedelmod.features.major.StrengthIndicators;
import at.yedel.yedelmod.features.major.TNTTagFeatures;
import at.yedel.yedelmod.features.ping.PingResponse;
import at.yedel.yedelmod.utils.Threading;

import net.minecraft.client.Minecraft;
import net.fabricmc.api.ClientModInitializer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.polyfrost.oneconfig.api.commands.v1.CommandManager;
import org.polyfrost.oneconfig.api.event.v1.EventManager;

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
		CustomHitParticles.getInstance();
		RandomPlaceholder.getInstance();
		LimboCreative.getInstance();

		Threading.scheduleRepeat(() -> {
			//~ if modern 'Minecraft.getMinecraft().thePlayer' -> 'Minecraft.getInstance().player'
			if (Minecraft.getInstance().player != null) {
				YedelConfig.getInstance().playtimeMinutes++;
				YedelConfig.getInstance().save();
			}
		}, 1, TimeUnit.MINUTES);
	}

	public void onInitializeClient() {
		initialize();
	}

	private void registerEventListeners(Object... listeners) {
		for (Object listener: listeners) {
			EventManager.INSTANCE.register(listener);
		}
	}
}
