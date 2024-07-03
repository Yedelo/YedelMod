package at.yedel.yedelmod.events;



import net.minecraft.network.Packet;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.fml.common.eventhandler.Event;



public class PacketEvent extends Event {
    @Cancelable
    public static class SendEvent extends Event {
        private final Packet packet;

        public Packet getPacket() {
            return packet;
        }

        public SendEvent(Packet packet) {
            this.packet = packet;
        }
    }

    @Cancelable
    public static class ReceiveEvent extends Event {
        private final Packet packet;

        public Packet getPacket() {
            return packet;
        }

        public ReceiveEvent(Packet packet) {
            this.packet = packet;
        }
    }
}
