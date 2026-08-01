package at.yedel.yedelmod.features.ping;



public class PingException extends RuntimeException {
    public PingException(String message) {
        super(message);
    }

    public static class Send extends PingException {
        public Send(String message) {
            super(message);
        }
    }

    public static class Receive extends PingException {
        public Receive(String message) {
            super(message);
        }
    }
}
