package at.yedel.yedelmod.features;



import java.util.concurrent.TimeUnit;

import at.yedel.yedelmod.config.YedelConfig;
import at.yedel.yedelmod.utils.Schedule;
import gg.essential.api.utils.Multithreading;

import static at.yedel.yedelmod.YedelMod.minecraft;



public class PlaytimeSchedule implements Schedule {
    public Runnable runnable = () -> {
        if (minecraft.theWorld != null) {
            YedelConfig.playtimeMinutes++;
            YedelConfig.save();
        }
    };

    public void startSchedule() {
        Multithreading.INSTANCE.schedule(runnable, 0, 1, TimeUnit.MINUTES);
    }
}
