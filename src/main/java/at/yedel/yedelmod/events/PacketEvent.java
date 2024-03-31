package at.yedel.yedelmod.events;



import net.minecraft.network.Packet;
import net.minecraftforge.fml.common.eventhandler.Event;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;



public class PacketEvent extends Event {
    public static class SendEvent extends Event {
        public Packet packet;
        public CallbackInfo ci;

        public SendEvent(Packet p_147297_1_, CallbackInfo ci) {
            this.packet = p_147297_1_;
            this.ci = ci;
        }
    }

    public static class ReceiveEvent extends Event {
        public Packet packet;
        public CallbackInfo ci;

        public ReceiveEvent(Packet packetIn, CallbackInfo ci) {
            this.packet = packetIn;
            this.ci = ci;
        }
    }
}
