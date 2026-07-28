package at.yedel.yedelmod.utils;



import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.sounds.SoundEvents;
import org.polyfrost.compose.render.PolyColor;



public class Constants {
    public static final PolyColor EMPTY_COLOR = PolyColor.Companion.getTRANSPARENT();

    public static void playPingSound(float volume, float pitch) {
        Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.EXPERIENCE_ORB_PICKUP, pitch, volume));
    }
}
