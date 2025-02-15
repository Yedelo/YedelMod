package at.yedel.yedelmod.utils;



import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;



public class ThreadManager {
    private static final ScheduledExecutorService service = Executors.newScheduledThreadPool(10);

    public static void scheduleRepeat(Runnable runnable, int delay, TimeUnit unit) {
        service.scheduleWithFixedDelay(runnable, 0L, delay, unit);
    }

    public static void scheduleOnce(Runnable runnable, int delay) {
        scheduleOnce(runnable, delay, TimeUnit.MILLISECONDS);
    }

    public static void scheduleOnce(Runnable runnable, int delay, TimeUnit unit) {
        service.schedule(runnable, delay, unit);
    }
}
