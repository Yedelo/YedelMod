package at.yedel.yedelmod.utils;







import net.minecraft.client.Minecraft;

    //? if modern {
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.sounds.SoundEvents;
//?} else {
/*import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.util.ResourceLocation;
*///?}



public class Constants {
    //? if legacy
    //public static final ResourceLocation PLING_SOUND_LOCATION = new ResourceLocation("random.successful_hit");

    public static void playPingSound(float volume, float pitch) {
        //? if legacy {
        //Minecraft.getInstance().getSoundHandler().playSound(PositionedSoundRecord.create(PLING_SOUND_LOCATION, pitch));
        //?} else
        Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.EXPERIENCE_ORB_PICKUP, pitch, volume));
    }
}
