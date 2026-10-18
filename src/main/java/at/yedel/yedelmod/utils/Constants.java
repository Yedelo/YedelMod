package at.yedel.yedelmod.utils;







import net.minecraft.client.Minecraft;

    //? if modern {
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.sounds.SoundEvents;
//?} else {
/*import net.minecraft.client.sound.instance.SimpleSoundInstance;
import net.minecraft.resource.Identifier;
*///?}



public class Constants {
    //? if legacy
    //public static final Identifier PLING_SOUND_LOCATION = new Identifier("random.successful_hit");

    public static void playPingSound(float volume, float pitch) {
        Minecraft.getInstance().getSoundManager().play(/*? if modern {*/SimpleSoundInstance.forUI(SoundEvents.EXPERIENCE_ORB_PICKUP, pitch, volume)/*? else {*//*SimpleSoundInstance.of(PLING_SOUND_LOCATION, pitch)*//*?}*/);
    }
}
