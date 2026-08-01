package at.yedel.yedelmod.features.ping;



import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;



public class PingQueue {
    private static final PingQueue INSTANCE = new PingQueue();

    public static PingQueue getInstance() {
        return INSTANCE;
    }

    private final Map<PingMethod, PingQueueInfo> queue = new HashMap<>();

    public void queue(PingMethod method, Consumer<Long> callback) {
        queue(method, callback, (exception) -> {
            throw exception;
        });
    }

    public void queue(PingMethod method, Consumer<Long> callback, Consumer<PingException> errorHandler) {
        try {
            queue.put(method, new PingQueueInfo(callback, errorHandler));
            method.starting.run();
        }
        catch (PingException exception) {
            errorHandler.accept(exception);
        }
    }

    public boolean post(PingMethod method) {
        PingQueueInfo info = queue.get(method);
        if (info == null) {
            return false;
        }
        try {
            info.end();
            info.run(method.calculator.apply(info));
        }
        catch (PingException exception) {
            info.handleError(exception);
        }
        queue.remove(method);
        return true;
    }

    public Map<PingMethod, PingQueueInfo> getQueue() {
        return queue;
    }
}
