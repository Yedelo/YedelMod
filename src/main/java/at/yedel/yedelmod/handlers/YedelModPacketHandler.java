package at.yedel.yedelmod.handlers;



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
		super.channelRead(ctx, msg);
		if (msg instanceof Packet) {
			MinecraftForge.EVENT_BUS.post(new PacketEvent.ReceiveEvent((Packet) msg));
			if (msg instanceof S01PacketJoinGame) {
				MinecraftForge.EVENT_BUS.post(new JoinGamePacketEvent());
			}
		}
	}

	@Override
	public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
		super.write(ctx, msg, promise);
		if (msg instanceof Packet) {
			MinecraftForge.EVENT_BUS.post(new PacketEvent.SendEvent((Packet) msg));
		}
	}
}
