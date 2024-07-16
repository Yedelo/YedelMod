package at.yedel.yedelmod.utils;



import at.yedel.yedelmod.events.JoinGamePacketEvent;
import at.yedel.yedelmod.events.PacketEvent;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S01PacketJoinGame;
import net.minecraftforge.common.MinecraftForge;



@Sharable
public class YedelModPacketHandler extends ChannelDuplexHandler {
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		if (msg instanceof Packet) {
			if (MinecraftForge.EVENT_BUS.post(new PacketEvent.ReceiveEvent((Packet) msg))) return;
		}
		super.channelRead(ctx, msg);
	}

	@Override
	public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
		if (msg instanceof Packet) {
			if (msg instanceof S01PacketJoinGame) {
				MinecraftForge.EVENT_BUS.post(new JoinGamePacketEvent());
			}
			if (MinecraftForge.EVENT_BUS.post(new PacketEvent.SendEvent((Packet) msg))) return;
		}
		super.write(ctx, msg, promise);
	}
}
