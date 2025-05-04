package cc.polyfrost.oneconfig.internal.utils;



import cc.polyfrost.oneconfig.renderer.asset.Icon;

import java.util.concurrent.Callable;



public class Notification {
    public Notification(String title, String message, Icon icon, float duration, Callable<Float> progressbar, Runnable action) {}

    public boolean isFinished() {
        throw new IllegalStateException("Dummy code called!");
    }
}
