package at.yedel.yedelmod.utils;







import net.minecraft.client.Minecraft;

    //? if modern {
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.sounds.SoundEvents;
//?} else {
/*import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.util.ResourceLocation;
*///?}
//? if v1
import org.polyfrost.compose.render.PolyColor;
//? else
//import cc.polyfrost.oneconfig.config.core.OneColor;



public class Constants {
    //? if legacy
    //public static final ResourceLocation PLING_SOUND_LOCATION = new ResourceLocation("random.successful_hit");
    //? if v0 {
    //public static final OneColor EMPTY_COLOR = new OneColor(0, 0, 0, 0);
    //?} else
    public static final PolyColor EMPTY_COLOR = PolyColor.Companion.getTRANSPARENT();

    public static void playPingSound(float volume, float pitch) {
        //? if legacy {
        //Minecraft.getMinecraft().getSoundHandler().playSound(PositionedSoundRecord.create(PLING_SOUND_LOCATION, pitch));
        //?} else
        Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.EXPERIENCE_ORB_PICKUP, pitch, volume));
    }
}
