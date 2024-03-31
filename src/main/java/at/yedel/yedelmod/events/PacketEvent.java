package at.yedel.yedelmod.events;



import net.minecraft.network.Packet;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.fml.common.eventhandler.Event;



public class PacketEvent extends Event {
    @Cancelable
    public static class SendEvent extends Event {
        public Packet packet;

        public SendEvent(Packet packetIn) {
            this.packet = packetIn;
        }
    }

    @Cancelable
    public static class ReceiveEvent extends Event {
        public Packet packet;

        public ReceiveEvent(Packet packetIn) {
            this.packet = packetIn;
        }
    }
}
