package at.yedel.yedelmod.handlers;



import java.util.Objects;
import java.util.logging.Handler;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

import at.yedel.yedelmod.config.YedelConfig;
import at.yedel.yedelmod.features.LimboCreativeCheck;
import at.yedel.yedelmod.features.major.TNTTagFeatures;
import at.yedel.yedelmod.features.major.ping.PingResponse;
import at.yedel.yedelmod.features.major.ping.PingSender;
import at.yedel.yedelmod.utils.Chat;
import at.yedel.yedelmod.utils.Constants.Messages;
import net.hypixel.data.type.GameType;
import net.hypixel.data.type.ServerType;
import net.hypixel.modapi.HypixelModAPI;
import net.hypixel.modapi.error.ModAPIException;
import net.hypixel.modapi.packet.impl.clientbound.ClientboundHelloPacket;
import net.hypixel.modapi.packet.impl.clientbound.ClientboundPingPacket;
import net.hypixel.modapi.packet.impl.clientbound.event.ClientboundLocationPacket;
import net.hypixel.modapi.packet.impl.serverbound.ServerboundPingPacket;



public class HypixelManager {
	private HypixelManager() {}

	private static HypixelManager instance = new HypixelManager();

	public static HypixelManager getInstance() {
		return instance;
	}

	private static final String pingPacketIdentifier = HypixelModAPI.getInstance().getRegistry().getIdentifier(ClientboundPingPacket.class);

	public void setup() {
		HypixelModAPI.getInstance().subscribeToEventPacket(ClientboundLocationPacket.class);

		HypixelModAPI.getInstance().registerHandler(ClientboundHelloPacket.class, this::onHelloPacket);
		HypixelModAPI.getInstance().registerHandler(ClientboundPingPacket.class, this::onPingPacket);
		HypixelModAPI.getInstance().registerHandler(ClientboundLocationPacket.class, this::onLocationPacket);

		// This was made before Connor added error handling to ClientboundPacketHandlers.
		// This feature is not released yet, not significant to my use and would require a newer version of the Mod API in the future.
		Logger.getLogger("HypixelModAPI").addHandler(new Handler() {
			@Override
			public void publish(LogRecord record) {
				Throwable throwable = record.getThrown();
				if (throwable instanceof ModAPIException) {
					onException((ModAPIException) throwable);
				}
			}

			@Override
			public void flush() {}

			@Override
			public void close() {}
		});
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

	private void onHelloPacket(ClientboundHelloPacket helloPacket) {
		HypixelModAPI.getInstance().sendPacket(new ServerboundPingPacket());
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
			inBedwars = serverType == GameType.BEDWARS;
		}
		if (locationPacket.getMode().isPresent() && locationPacket.getMode().get().equals("TNTAG")) {
			TNTTagFeatures.getInstance().onTNTTagJoin();
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
}
