package at.yedel.yedelmod.handlers;



import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.fml.common.eventhandler.Event;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.Logger;
import org.apache.logging.log4j.message.Message;



public class LogListenerFilter implements Filter {
	private LogListenerFilter() {}

	private static final LogListenerFilter instance = new LogListenerFilter();

	public static LogListenerFilter getInstance() {
		return instance;
	}

	@Override
	public Result getOnMismatch() {
		return null;
	}

	@Override
	public Result getOnMatch() {
		return null;
	}

	@Override
	public Result filter(Logger logger, Level level, Marker marker, String msg, Object... params) {
		return null;
	}

	@Override
	public Result filter(Logger logger, Level level, Marker marker, Object msg, Throwable t) {
		return null;
	}

	@Override
	public Result filter(Logger logger, Level level, Marker marker, Message msg, Throwable t) {
		return null;
	}

	@Override
	public Result filter(LogEvent event) {
		if (MinecraftForge.EVENT_BUS.post(new Log4JEvent(event))) {
			return Result.DENY;
		}
		return null;
	}

	@Cancelable
	public static class Log4JEvent extends Event {
		private final LogEvent logEvent;

		public LogEvent getLogEvent() {
			return logEvent;
		}

		public Log4JEvent(LogEvent logEvent) {
			this.logEvent = logEvent;
		}
	}

	// Prevents an AbstractMethodError at shutdown hook
	public void stop() {}
}
