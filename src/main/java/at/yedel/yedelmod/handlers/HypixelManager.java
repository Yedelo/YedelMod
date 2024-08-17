package at.yedel.yedelmod.handlers;



import java.util.Objects;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

import at.yedel.yedelmod.config.YedelConfig;
import at.yedel.yedelmod.features.LimboCreativeCheck;
import at.yedel.yedelmod.features.major.TNTTagFeatures;
import at.yedel.yedelmod.features.major.ping.PingResponse;
import at.yedel.yedelmod.features.major.ping.PingSender;
import at.yedel.yedelmod.handlers.LogListenerFilter.Log4JEvent;
import at.yedel.yedelmod.utils.Chat;
import at.yedel.yedelmod.utils.Constants.Messages;
import net.hypixel.data.type.GameType;
import net.hypixel.data.type.ServerType;
import net.hypixel.modapi.HypixelModAPI;
import net.hypixel.modapi.error.ModAPIException;
import net.hypixel.modapi.packet.impl.clientbound.ClientboundPingPacket;
import net.hypixel.modapi.packet.impl.clientbound.event.ClientboundLocationPacket;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;



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

	public boolean getInSkywars() {
		return inSkywars;
	}

	private boolean inSkyblock;

	public boolean getInSkyblock() {
		return inSkyblock;
	}

	private boolean inLimbo;

	public boolean getInLimbo() {
		return inLimbo;
	}

	public boolean inBedwars;

	public boolean getInBedwars() {
		return inBedwars;
	}

	private boolean inTNTTag;

	public boolean getInTNTTag() {
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
				Chat.display(Messages.hypixelRateLimited);
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

		// Forge Mod API 1.0.1.1 and after
		MinecraftForge.EVENT_BUS.register(this);
	}

	@SubscribeEvent
	public void onModAPIException(Log4JEvent event) {
		if (event.getLogEvent().getThrown() instanceof ModAPIException) {
			onException((ModAPIException) event.getLogEvent().getThrown());
		}
	}
}
