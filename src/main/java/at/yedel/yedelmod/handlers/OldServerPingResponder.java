package at.yedel.yedelmod.handlers;



import java.net.InetAddress;
import java.net.UnknownHostException;

import at.yedel.yedelmod.YedelMod;
import at.yedel.yedelmod.features.major.ping.PingResponse;
import at.yedel.yedelmod.features.major.ping.PingSender;
import at.yedel.yedelmod.utils.Chat;
import at.yedel.yedelmod.utils.Constants.Messages;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ServerAddress;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.network.OldServerPinger;
import net.minecraft.network.EnumConnectionState;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.handshake.client.C00Handshake;
import net.minecraft.network.status.INetHandlerStatusClient;
import net.minecraft.network.status.client.C00PacketServerQuery;
import net.minecraft.network.status.client.C01PacketPing;
import net.minecraft.network.status.server.S00PacketServerInfo;
import net.minecraft.network.status.server.S01PacketPong;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;



public class OldServerPingResponder extends OldServerPinger {
	@Override
	public void ping(ServerData server) throws UnknownHostException {
		ServerAddress serverAddress = ServerAddress.fromString(server.serverIP);
		NetworkManager networkManager = NetworkManager.createNetworkManagerAndConnect(InetAddress.getByName(serverAddress.getIP()), serverAddress.getPort(), false);
		// Field names taken from EvergreenHUD's server pinger
		// https://github.com/Polyfrost/EvergreenHUD/blob/oneconfig/src/main/kotlin/org/polyfrost/evergreenhud/utils/ServerPinger.kt
		networkManager.setNetHandler(new INetHandlerStatusClient() {
			private boolean queried = false;
			private boolean received = false;

			public void handleServerInfo(S00PacketServerInfo packetIn) {
				if (received) {
					networkManager.closeChannel(new ChatComponentText("Received unrequested status"));
				}
				received = true;
				PingSender.getInstance().updateLastTime();
				PingSender.getInstance().serverListCheck = true;
				networkManager.sendPacket(new C01PacketPing(Minecraft.getSystemTime()));
				queried = true;
			}

			public void handlePong(S01PacketPong packetIn) {
				PingResponse.getInstance().onServerListResponse();
				networkManager.closeChannel(new ChatComponentText("Finished"));
			}

			public void onDisconnect(IChatComponent reason) {
				if (!queried) {
					Chat.display(Messages.failedServerPingMessage);
					YedelMod.logger.error("Can't ping {}: {}", server.serverIP, reason.getUnformattedText());
				}
			}
		});

		networkManager.sendPacket(new C00Handshake(47, serverAddress.getIP(), serverAddress.getPort(), EnumConnectionState.STATUS));
		networkManager.sendPacket(new C00PacketServerQuery());
	}
}
