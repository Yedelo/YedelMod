package at.yedel.yedelmod.utils;



import at.yedel.yedelmod.config.YedelConfig;
import cc.polyfrost.oneconfig.gui.animations.Animation;
import cc.polyfrost.oneconfig.gui.animations.DummyAnimation;
import cc.polyfrost.oneconfig.internal.utils.Notification;
import cc.polyfrost.oneconfig.renderer.asset.Icon;
import cc.polyfrost.oneconfig.utils.Notifications;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;



public class ClickNotifications {
    private static final ClickNotifications INSTANCE = new ClickNotifications();

    public static ClickNotifications getInstance() {
        return INSTANCE;
    }

    private static final float DEFAULT_DURATION = 4000;

    private final Field $notifications;
    private final LinkedHashMap<Notification, Animation> notifications;
    private final LinkedHashMap<Notification, Runnable> actionMap = new LinkedHashMap<Notification, Runnable>();

    private ClickNotifications() {
        try {
            $notifications = Notifications.class.getDeclaredField("notifications");
            $notifications.setAccessible(true);
            notifications = (LinkedHashMap<Notification, Animation>) $notifications.get(Notifications.INSTANCE);
        }
        catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public void clickNotification() {
        Set<Map.Entry<Notification, Runnable>> entrySet = actionMap.entrySet();
        Object[] entryArray = entrySet.toArray();
        if (entryArray.length != 0) {
            int lastIndex = entryArray.length - 1;
            Map.Entry<Notification, Runnable> lastEntry = (Map.Entry<Notification, Runnable>) entryArray[lastIndex];
            if (lastEntry != null) {
                if (!lastEntry.getKey().isFinished()) {
                    lastEntry.getValue().run();
                }
                actionMap.remove(lastEntry.getKey());
            }
        }
    }

    @SubscribeEvent
    public void clickNotificationInGui(GuiScreenEvent.KeyboardInputEvent event) {
        if (YedelConfig.getInstance().clickNotificationKeybind.isActive()) {
            clickNotification();
        }
    }

    /**
     * Send a notification to the user
     *
     * @param title       The title of the notification
     * @param message     The message of the notification
     * @param icon        The icon of the notification, null for none
     * @param duration    The duration the notification is on screen in ms
     * @param progressbar A callable that returns the progress from 0-1
     * @param action      The action executed when the notification is pressed
     */
    public void send(String title, String message, @Nullable Icon icon, float duration, @Nullable Callable<Float> progressbar, @Nullable Runnable action) {
        Notification notification =
            new Notification(title, message.replace("%k", YedelConfig.getInstance().clickNotificationKeybind.getDisplay()), icon, duration, progressbar, action);
        notifications.put(notification, new DummyAnimation(-1));
        actionMap.put(notification, action);
    }

    /**
     * Send a notification to the user
     *
     * @param title       The title of the notification
     * @param message     The message of the notification
     * @param duration    The duration the notification is on screen in ms
     * @param progressbar A callable that returns the progress from 0-1
     * @param action      The action executed when the notification is pressed
     */
    public void send(String title, String message, float duration, @Nullable Callable<Float> progressbar, @Nullable Runnable action) {
        send(title, message, null, duration, progressbar, action);
    }

    /**
     * Send a notification to the user
     *
     * @param title       The title of the notification
     * @param message     The message of the notification
     * @param icon        The icon of the notification, null for none
     * @param duration    The duration the notification is on screen in ms
     * @param progressbar A callable that returns the progress from 0-1
     */
    public void send(String title, String message, @Nullable Icon icon, float duration, @Nullable Callable<Float> progressbar) {
        send(title, message, icon, duration, progressbar, null);
    }

    /**
     * Send a notification to the user
     *
     * @param title    The title of the notification
     * @param message  The message of the notification
     * @param icon     The icon of the notification, null for none
     * @param duration The duration the notification is on screen in ms
     * @param action   The action executed when the notification is pressed
     */
    public void send(String title, String message, @Nullable Icon icon, float duration, @Nullable Runnable action) {
        send(title, message, icon, duration, null, action);
    }

    /**
     * Send a notification to the user
     *
     * @param title       The title of the notification
     * @param message     The message of the notification
     * @param duration    The duration the notification is on screen in ms
     * @param progressbar A callable that returns the progress from 0-1
     */
    public void send(String title, String message, float duration, @Nullable Callable<Float> progressbar) {
        send(title, message, duration, progressbar, null);
    }

    /**
     * Send a notification to the user
     *
     * @param title    The title of the notification
     * @param message  The message of the notification
     * @param duration The duration the notification is on screen in ms
     * @param action   The action executed when the notification is pressed
     */
    public void send(String title, String message, float duration, @Nullable Runnable action) {
        send(title, message, duration, null, action);
    }

    /**
     * Send a notification to the user
     *
     * @param title       The title of the notification
     * @param message     The message of the notification
     * @param icon        The icon of the notification, null for none
     * @param progressbar A callable that returns the progress from 0-1
     */
    public void send(String title, String message, @Nullable Icon icon, @Nullable Callable<Float> progressbar) {
        send(title, message, icon, DEFAULT_DURATION, progressbar);
    }

    /**
     * Send a notification to the user
     *
     * @param title   The title of the notification
     * @param message The message of the notification
     * @param icon    The icon of the notification, null for none
     * @param action  The action executed when the notification is pressed
     */
    public void send(String title, String message, @Nullable Icon icon, @Nullable Runnable action) {
        send(title, message, icon, DEFAULT_DURATION, action);
    }

    /**
     * Send a notification to the user
     *
     * @param title       The title of the notification
     * @param message     The message of the notification
     * @param progressbar A callable that returns the progress from 0-1
     */
    public void send(String title, String message, @Nullable Callable<Float> progressbar) {
        send(title, message, DEFAULT_DURATION, progressbar);
    }

    /**
     * Send a notification to the user
     *
     * @param title    The title of the notification
     * @param message  The message of the notification
     * @param icon     The icon of the notification, null for none
     * @param duration The duration the notification is on screen in ms
     */
    public void send(String title, String message, @Nullable Icon icon, float duration) {
        send(title, message, icon, duration, (Callable<Float>) null);
    }

    /**
     * Send a notification to the user
     *
     * @param title   The title of the notification
     * @param message The message of the notification
     * @param action  The action executed when the notification is pressed
     */
    public void send(String title, String message, @Nullable Runnable action) {
        send(title, message, DEFAULT_DURATION, action);
    }

    /**
     * Send a notification to the user
     *
     * @param title    The title of the notification
     * @param message  The message of the notification
     * @param duration The duration the notification is on screen in ms
     */
    public void send(String title, String message, float duration) {
        send(title, message, duration, (Callable<Float>) null);
    }

    /**
     * Send a notification to the user
     *
     * @param title   The title of the notification
     * @param message The message of the notification
     * @param icon    The icon of the notification, null for none
     */
    public void send(String title, String message, @Nullable Icon icon) {
        send(title, message, icon, (Callable<Float>) null);
    }

    /**
     * Send a notification to the user
     *
     * @param title   The title of the notification
     * @param message The message of the notification
     */
    public void send(String title, String message) {
        send(title, message, (Callable<Float>) null);
    }
}
