package at.yedel.yedelmod.features.ping;



import java.util.function.Consumer;



public class PingQueueInfo {
    private Consumer<Long> callback;
    private Consumer<PingException> errorHandler;
    private long startTime;
    private long endTime;

    public PingQueueInfo(Consumer<Long> callback, Consumer<PingException> errorHandler) {
        this.startTime = System.nanoTime();
        this.callback = callback;
        this.errorHandler = errorHandler;
    }

    public void end() {
        this.endTime = System.nanoTime();
    }

    public void run(long ping) {
        callback.accept(ping);
    }

    public void handleError(PingException exception) {
        errorHandler.accept(exception);
    }

    public long startTime() {
        return startTime;
    }

    public long endTime() {
        return endTime;
    }
}
