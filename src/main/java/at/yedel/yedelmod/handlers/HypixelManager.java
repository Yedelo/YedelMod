package at.yedel.yedelmod.handlers;



import at.yedel.yedelmod.features.ping.PingSender;
import cc.polyfrost.oneconfig.libs.universal.UChat;
import net.hypixel.modapi.HypixelModAPI;
import net.hypixel.modapi.error.ModAPIException;
import net.hypixel.modapi.packet.impl.clientbound.ClientboundPingPacket;
import net.hypixel.modapi.packet.impl.clientbound.event.ClientboundLocationPacket;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.Logger;
import org.apache.logging.log4j.message.Message;

import java.util.Objects;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

import static at.yedel.yedelmod.launch.YedelModConstants.LOGO;



public class HypixelManager {
	private HypixelManager() {}

	private static final HypixelManager INSTANCE = new HypixelManager();

	public static HypixelManager getInstance() {
		return INSTANCE;
	}

	private static final String PING_PACKET_IDENTIFIER =
		HypixelModAPI.getInstance().getRegistry().getIdentifier(ClientboundPingPacket.class);

	public void setup() {
		HypixelModAPI.getInstance().subscribeToEventPacket(ClientboundLocationPacket.class);
		setupLogHandlers();
	}

	private void handleException(ModAPIException exception) {
		if (Objects.equals(exception.getIdentifier(), PING_PACKET_IDENTIFIER)) {
			if (PingSender.getInstance().hypixelCheck) {
				PingSender.getInstance().hypixelCheck = false;
				UChat.chat(LOGO + " §cYou were rate limited while using this method!");
			}
		}
	}

	private void setupLogHandlers() {
		// Forge Mod API 1.0.0.1 and before
		java.util.logging.Logger.getLogger("HypixelModAPI").addHandler(new Handler() {
			@Override
			public void publish(LogRecord record) {
				if (record.getThrown() instanceof ModAPIException) {
					handleException((ModAPIException) record.getThrown());
				}
			}

			@Override
			public void flush() {}

			@Override
			public void close() {}
		});

		// Forge Mod API 1.0.1.1 and above
		Logger logger = ((Logger) LogManager.getLogger("HypixelModAPI"));
		logger.addFilter(new Filter() {
			@Override
			public Result getOnMismatch() {
				return null;
			}

			@Override
			public Result getOnMatch() {
				return null;
			}

			@Override
			public Result filter(Logger logger, Level level, Marker marker, String msg, Object... params) {
				return null;
			}

			@Override
			public Result filter(Logger logger, Level level, Marker marker, Object msg, Throwable t) {
				return null;
			}

			@Override
			public Result filter(Logger logger, Level level, Marker marker, Message msg, Throwable t) {
				return null;
			}

			@Override
			public Result filter(LogEvent event) {
				if (event.getThrown() instanceof ModAPIException) {
					handleException((ModAPIException) event.getThrown());
				}
				return null;
			}

			// This is apparently an abstract method of Filter, so putting this stops an AbstractMethodError when the game is closed
			public void stop() {

			}
		});
	}
}
