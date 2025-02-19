package at.yedel.yedelmod.events;



import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S01PacketJoinGame;
import net.minecraftforge.fml.common.eventhandler.Event;



public class PacketEvent extends Event {
    private final Packet packet;

    public Packet getPacket() {
        return packet;
    }

    private PacketEvent(Packet packet) {
        this.packet = packet;
    }

    public static class SendEvent extends PacketEvent {
        public SendEvent(Packet packet) {
            super(packet);
        }
    }

    public static class ReceiveEvent extends PacketEvent {
        private final boolean isJoinGamePacket;
        public ReceiveEvent(Packet packet) {
            super(packet);
            this.isJoinGamePacket = packet instanceof S01PacketJoinGame;
        }

        public boolean isJoinGamePacket() {
            return isJoinGamePacket;
        }
    }
}
