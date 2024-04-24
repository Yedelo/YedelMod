package at.yedel.yedelmod.features;



import java.util.concurrent.TimeUnit;

import at.yedel.yedelmod.config.YedelConfig;
import at.yedel.yedelmod.utils.Schedule;
import at.yedel.yedelmod.utils.ThreadManager;

import static at.yedel.yedelmod.YedelMod.minecraft;



public class PlaytimeSchedule implements Schedule {
    public Runnable runnable = () -> {
        if (minecraft.theWorld != null) {
            YedelConfig.playtimeMinutes++;
            YedelConfig.save();
        }
    };

    public void startSchedule() {
        ThreadManager.scheduleRepeat(runnable, 1, TimeUnit.MINUTES);
    }
}
