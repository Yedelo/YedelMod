package at.yedel.yedelmod;



import at.yedel.yedelmod.config.YedelConfig;
import at.yedel.yedelmod.features.*;
import at.yedel.yedelmod.features.major.BedwarsFeatures;
import at.yedel.yedelmod.features.major.EasyAtlasVerdicts;
import at.yedel.yedelmod.features.major.StrengthIndicators;
import at.yedel.yedelmod.features.major.TNTTagFeatures;
import at.yedel.yedelmod.features.ping.PingResponse;
import at.yedel.yedelmod.utils.Threading;
import cc.polyfrost.oneconfig.events.EventManager;
import cc.polyfrost.oneconfig.libs.universal.UMinecraft;
import cc.polyfrost.oneconfig.utils.commands.CommandManager;
import net.hypixel.modapi.HypixelModAPI;
import net.hypixel.modapi.packet.impl.clientbound.event.ClientboundLocationPacket;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.TimeUnit;



public class YedelMod {
	private static YedelMod INSTANCE;

	public static YedelMod getInstance() {
		return INSTANCE;
	}

	public static final Logger yedelog = LogManager.getLogger("YedelMod");

	@EventHandler
	public void init(FMLInitializationEvent event) {
		// Loads class. preload() exists for this but what ev
		YedelConfig.getInstance();
		HypixelModAPI.getInstance().subscribeToEventPacket(ClientboundLocationPacket.class);
		CommandManager.INSTANCE.registerCommand(YedelCommand.getInstance());

		registerEventListeners(
			this,
			AutoGuildWelcome.getInstance(),
			BedwarsFeatures.getInstance(),
			DropperGG.getInstance(),
			EasyAtlasVerdicts.getInstance(),
			FavoriteServerButton.getInstance(),
			CustomHitParticles.getInstance(),
			PingResponse.getInstance(),
			RandomPlaceholder.getInstance(),
			RegexChatFilter.getInstance(),
			StrengthIndicators.getInstance(),
            TNTTagFeatures.getInstance()
		);

		Threading.scheduleRepeat(() -> {
			if (UMinecraft.getWorld() != null) {
				YedelConfig.getInstance().playtimeMinutes++;
				YedelConfig.getInstance().save();
			}
		}, 1, TimeUnit.MINUTES);
	}

	private void registerEventListeners(Object... eventListeners) {
		for (Object eventListener: eventListeners) {
			MinecraftForge.EVENT_BUS.register(eventListener);
			EventManager.INSTANCE.register(eventListener);
		}
	}
}
