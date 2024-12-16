package at.yedel.yedelmod.handlers;



import java.util.Objects;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

import at.yedel.yedelmod.config.YedelConfig;
import at.yedel.yedelmod.features.LimboCreativeCheck;
import at.yedel.yedelmod.features.major.TNTTagFeatures;
import at.yedel.yedelmod.features.major.ping.PingResponse;
import at.yedel.yedelmod.features.major.ping.PingSender;
import at.yedel.yedelmod.utils.Chat;
import net.hypixel.data.type.GameType;
import net.hypixel.data.type.ServerType;
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



public class HypixelManager {
	private HypixelManager() {}

	private static final HypixelManager instance = new HypixelManager();

	public static HypixelManager getInstance() {
		return instance;
	}

	private static final String pingPacketIdentifier = HypixelModAPI.getInstance().getRegistry().getIdentifier(ClientboundPingPacket.class);

	public void setup() {
		HypixelModAPI.getInstance().subscribeToEventPacket(ClientboundLocationPacket.class);

		HypixelModAPI.getInstance().registerHandler(ClientboundPingPacket.class, this::onPingPacket);
		HypixelModAPI.getInstance().registerHandler(ClientboundLocationPacket.class, this::onLocationPacket);

		setupLogHandlers();
	}

	private boolean inSkywars;

	public boolean isInSkywars() {
		return inSkywars;
	}

	private boolean inSkyblock;

	public boolean isInSkyblock() {
		return inSkyblock;
	}

	private boolean inLimbo;

	public boolean isInLimbo() {
		return inLimbo;
	}

	public boolean inBedwars;

	public boolean isInBedwars() {
		return inBedwars;
	}

	private boolean inTNTTag;

	public boolean isInTNTTag() {
		return inTNTTag;
	}

	private void onPingPacket(ClientboundPingPacket pingPacket) {
		PingResponse.getInstance().onHypixelPingPacket();
	}

	// I do not like Optional
	private void onLocationPacket(ClientboundLocationPacket locationPacket) {
		inLimbo = Objects.equals(locationPacket.getServerName(), "limbo");
		if (locationPacket.getServerType().isPresent()) {
			ServerType serverType = locationPacket.getServerType().get();
			inSkywars = serverType == GameType.SKYWARS;
			inSkyblock = serverType == GameType.SKYBLOCK;
			inBedwars = serverType == GameType.BEDWARS && !locationPacket.getLobbyName().isPresent(); // ok maybe it is good
		}
		if (locationPacket.getMode().isPresent() && locationPacket.getMode().get().equals("TNTAG")) {
			inTNTTag = true;
			TNTTagFeatures.getInstance().onTNTTagJoin();
		}
		else {
			inTNTTag = false;
		}
		if (inLimbo && YedelConfig.getInstance().limboCreative) {
			LimboCreativeCheck.getInstance().giveCreative();
		}
	}

	private void onException(ModAPIException exception) {
		if (Objects.equals(exception.getIdentifier(), pingPacketIdentifier)) {
			if (PingSender.getInstance().hypixelCheck) {
				PingSender.getInstance().hypixelCheck = false;
				Chat.logoDisplay("§cYou were rate limited while using this method!");
			}
		}
	}

	private void setupLogHandlers() {
		// Forge Mod API 1.0.0.1 and before
		java.util.logging.Logger.getLogger("HypixelModAPI").addHandler(new Handler() {
			@Override
			public void publish(LogRecord record) {
				if (record.getThrown() instanceof ModAPIException) {
					onException((ModAPIException) record.getThrown());
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
					onException((ModAPIException) event.getThrown());
				}
				return null;
			}
		});
	}
}
