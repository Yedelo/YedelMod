package at.yedel.yedelmod.utils;



import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;



public class ThreadManager {
    public static ScheduledExecutorService service = Executors.newScheduledThreadPool(10);

    public static void scheduleRepeat(Runnable runnable, int delay) {
        scheduleRepeat(runnable, delay, TimeUnit.MILLISECONDS);
    }

    public static void scheduleRepeat(Runnable runnable, int delay, TimeUnit unit) {
        service.scheduleWithFixedDelay(runnable, 0L, delay, unit);
    }

    public static void scheduleOnce(Runnable runnable, int delay) {
        scheduleOnce(runnable, delay, TimeUnit.MILLISECONDS);
    }

    public static void scheduleOnce(Runnable runnable, int delay, TimeUnit unit) {
        service.schedule(runnable, delay, unit);
    }

    public static void run(Runnable runnable) {
        scheduleOnce(runnable, 0, TimeUnit.DAYS);
    }
}
