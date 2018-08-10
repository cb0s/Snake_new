package utils.io;

import java.io.OutputStream;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.GregorianCalendar;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Lets you log text and Exceptions asynchronously with timestamp and optional prefix.</br></br>
 * 
 * <b>Use:</b> Instantiate a new logger or use one of the standard loggers:<ul>
 * <li>{@link Logger#getDefaultLogger()} for plain logging</li>
 * <li>{@link Logger#getDefaultInfoLogger()} for logging information</li>
 * <li>{@link Logger#getDefaultWarningLogger()} for logging warnings</li>
 * <li>{@link Logger#getDefaultErrorLogger()} for logging errors</li>
 * </ul>
 * 
 * @author Leo, Cedric
 * @version 3.0
 * @category util
 */
public class Logger {

	// *************
	// * Constants *
	// *************
	public static final String defaultDateTimePattern = "dd/MM/yyyy-HH:mm:ss";
	
	


	// **********
	// * Fields *
	// **********
	private static Logger defaultLogger, defaultInfoLogger, defaultWarningLogger, defaultErrorLogger;

	
	private final SimpleDateFormat simpleDateFormat;
	private final String prefix;
	private LinkedBlockingQueue<String> buffer;
	private Thread worker;




	// ****************
	// * Constructors *
	// ****************
	/**
	 * Creates a new logger using: <ul>
	 * <li>{@link #defaultDateTimePattern},</li>
	 * <li>an empty prefix and</li>
	 * <li>{@link System#out} as output.</li>
	 * </ul>
	 * These things can <b>not</b> be changed afterwards to ensure consistency within a logging session.
	 */
	public Logger() {
		this(defaultDateTimePattern, "", new OutputStream[] {System.out});
	}

	/**
	 * Creates a new logger using: <ul>
	 * <li>{@link #defaultDateTimePattern},</li>
	 * <li>the given prefix and</li>
	 * <li>{@link System#out} as output.</li>
	 * </ul>
	 * These things can <b>not</b> be changed afterwards to ensure consistency within a logging session.<br><br>
	 * 
	 * @param prefix the prefix preceeding every log entry
	 */
	public Logger(String prefix) {
		this(defaultDateTimePattern, prefix, new OutputStream[] {System.out});
	}

	/**
	 * Creates a new logger using: <ul>
	 * <li>the given date-time pattern,</li>
	 * <li>the given prefix and</li>
	 * <li>{@link System#out} as output.</li>
	 * </ul>
	 * These things can <b>not</b> be changed afterwards to ensure consistency within a logging session.<br><br>
	 * 
	 * @param dateTimePattern the pattern for the timestamp
	 * @param prefix the prefix preceeding every log entry
	 */
	public Logger(String dateTimePattern, String prefix) {
		this(dateTimePattern, prefix, new OutputStream[] {System.out});
	}

	/**
	 * Creates a new logger using: <ul>
	 * <li>{@link #defaultDateTimePattern},</li>
	 * <li>an empty prefix and</li>
	 * <li>the given OutputStream as output.</li>
	 * </ul>
	 * These things can <b>not</b> be changed afterwards to ensure consistency within a logging session.<br><br>
	 * 
	 * @param output the output channel
	 */
	public Logger(OutputStream output) {
		this(defaultDateTimePattern, "", new OutputStream[] {output});
	}

	/**
	 * Creates a new logger using: <ul>
	 * <li>{@link #defaultDateTimePattern},</li>
	 * <li>the given prefix and</li>
	 * <li>the given OutputStream as output.</li>
	 * </ul>
	 * These things can <b>not</b> be changed afterwards to ensure consistency within a logging session.<br><br>
	 * 
	 * @param prefix the prefix preceeding every log entry
	 * @param output the output channel
	 */
	public Logger(String prefix, OutputStream output) {
		this(defaultDateTimePattern, prefix, new OutputStream[] {output});
	}

	/**
	 * Creates a new logger using: <ul>
	 * <li>the given date-time pattern,</li>
	 * <li>the given prefix and</li>
	 * <li>the given OutputStream as output.</li>
	 * </ul>
	 * These things can <b>not</b> be changed afterwards to ensure consistency within a logging session.<br><br>
	 * 
	 * @param dateTimePattern the pattern for the timestamp
	 * @param prefix the prefix preceeding every log entry
	 * @param output the output channel
	 */
	public Logger(String dateTimePattern, String prefix, OutputStream output) {
		this(dateTimePattern, prefix, new OutputStream[] {output});
	}

	/**
	 * Creates a new logger using: <ul>
	 * <li>{@link #defaultDateTimePattern},</li>
	 * <li>an empty prefix and</li>
	 * <li>the given OutputStreams as outputs.</li>
	 * </ul>
	 * These things can <b>not</b> be changed afterwards to ensure consistency within a logging session.<br><br>
	 * 
	 * @param outputs the output channels
	 */
	public Logger(OutputStream[] outputs) {
		this(defaultDateTimePattern, "", outputs);
	}

	/**
	 * Creates a new logger using: <ul>
	 * <li>{@link #defaultDateTimePattern},</li>
	 * <li>the given prefix and</li>
	 * <li>the given OutputStreams as outputs.</li>
	 * </ul>
	 * These things can <b>not</b> be changed afterwards to ensure consistency within a logging session.<br><br>
	 * 
	 * @param prefix the prefix preceeding every log entry
	 * @param outputs the output channels
	 */
	public Logger(String prefix, OutputStream[] outputs) {
		this(defaultDateTimePattern, prefix, outputs);
	}

	/**
	 * Creates a new logger using: <ul>
	 * <li>the given date-time pattern,</li>
	 * <li>the given prefix and</li>
	 * <li>the given OutputStreams as outputs.</li>
	 * </ul>
	 * These things can <b>not</b> be changed afterwards to ensure consistency within a logging session.<br><br>
	 * 
	 * @param dateTimePattern the pattern for the timestamp
	 * @param prefix the prefix preceeding every log entry
	 * @param outputs the output channels
	 */
	public Logger(String dateTimePattern, String prefix, OutputStream[] outputs) {
		if(dateTimePattern == null || prefix == null || outputs == null) {
			throw new NullPointerException("Arguments must not be null!");
		}
		this.simpleDateFormat = new SimpleDateFormat(dateTimePattern);
		this.prefix = prefix;
		buffer = new LinkedBlockingQueue<>();
		this.worker = new Thread(new LoggerWorker(buffer, Arrays.stream(outputs).map(PrintWriter::new).toArray(PrintWriter[]::new)));
		this.worker.start();
		this.log("----------------------------new-Session-started----------------------------\n");
	}




	// *******************
	// * Private methods *
	// *******************
	/**
	 * Returns the current date-time parsed in this logger's pattern.
	 * 
	 * @return the current date-time parsed in this logger's pattern
	 */
	private String getTime() {
		return simpleDateFormat.format(new GregorianCalendar().getTime());
	}




	// ******************
	// * Public methods *
	// ******************
	/**
	 * Logs the given text.
	 * 
	 * @param text the text to log
	 */
	public synchronized void log(String text) {
		try {
			buffer.add("["+getTime()+"] " + prefix + text);
		} catch (IllegalStateException e) {
			System.out.println("Logger buffer out of bounds!");
			e.printStackTrace();
		}
	}

	/**
	 * Logs the given exception.
	 * 
	 * @param exception the exception to log
	 */
	public synchronized void logException(Exception exception) {
		String msg = exception.toString();
		for (StackTraceElement ste : exception.getStackTrace()) {
			msg+="\tat "+ste.toString()+'\n';
		}
		log(msg);
	}

	
	/**
	 * Returns the default logger for plain logging.
	 * 
	 * @return the default logger for plain logging
	 */
	public static synchronized Logger getDefaultLogger() {
		if(defaultLogger == null) {
			defaultLogger = new Logger();
		}
		return defaultLogger;
	}
	/**
	 * Returns the default logger for plain logging.<br>
	 * Short for {@link #getDefaultLogger()}.
	 * 
	 * @return the default logger for plain logging
	 */
	public static synchronized Logger gdL() {
		return getDefaultLogger();
	}
	/**
	 * Sets the default logger for plain logging.
	 * 
	 * @param logger the logger to set as default for plain logging
	 */
	public static synchronized void setDefaultLogger(Logger logger) {
		Logger.defaultLogger = logger;
	}


	/**
	 * Returns the default logger for logging information.
	 * 
	 * @return the default logger for logging information
	 */
	public static synchronized Logger getDefaultInfoLogger() {
		if(defaultInfoLogger == null) {
			defaultInfoLogger = new Logger("[Info] ");
		}
		return defaultInfoLogger;
	}
	/**
	 * Returns the default logger for logging information.<br>
	 * Short for {@link #getDefaultInfoLogger()}.
	 * 
	 * @return the default logger for logging information
	 */
	public static synchronized Logger gdiL() {
		return getDefaultInfoLogger();
	}
	/**
	 * Sets the default logger for logging information.
	 * 
	 * @param logger the logger to set as default for logging information
	 */
	public static synchronized void setDefaultInfoLogger(Logger logger) {
		Logger.defaultInfoLogger = logger;
	}


	/**
	 * Returns the default logger for logging warnings.
	 * 
	 * @return the default logger for logging warnings
	 */
	public static synchronized Logger getDefaultWarningLogger() {
		if(defaultWarningLogger == null) {
			defaultWarningLogger = new Logger("[Warning] ");
		}
		return defaultWarningLogger;
	}
	/**
	 * Returns the default logger for logging warnings.<br>
	 * Short for {@link #getDefaultWarningLogger()}.
	 * 
	 * @return the default logger for logging warnings
	 */
	public static synchronized Logger gdwL() {
		return getDefaultWarningLogger();
	}
	/**
	 * Sets the default logger for logging warnings.
	 * 
	 * @param logger the logger to set as default for logging warnings
	 */
	public static synchronized void setDefaultWarningLogger(Logger logger) {
		Logger.defaultWarningLogger = logger;
	}


	/**
	 * Returns the default logger for logging errors.
	 * 
	 * @return the default logger for logging errors
	 */
	public static synchronized Logger getDefaultErrorLogger() {
		if(defaultErrorLogger == null) {
			defaultErrorLogger = new Logger("[Error] ", System.err);
		}
		return defaultErrorLogger;
	}
	/**
	 * Returns the default logger for logging errors.<br>
	 * Short for {@link #getDefaultErrorLogger()}.
	 * 
	 * @return the default logger for logging errors
	 */
	public static synchronized Logger gdeL() {
		return getDefaultErrorLogger();
	}
	/**
	 * Sets the default logger for logging errors.
	 * 
	 * @param logger the logger to set as default for logging errors
	 */
	public static synchronized void setDefaultErrorLogger(Logger logger) {
		Logger.defaultErrorLogger = logger;
	}

	
	
	
	// *****************
	// * Inner classes *
	// *****************
	private class LoggerWorker implements Runnable {
		private LinkedBlockingQueue<String> buffer;
		private final PrintWriter[] outputs;
		
		public LoggerWorker(LinkedBlockingQueue<String> buffer, PrintWriter[] outputs) {
			this.buffer = buffer;
			this.outputs = outputs;
		}

		@Override
		public void run() {
			while(true) {
				try {
					String message = buffer.take();
					for(PrintWriter pw : outputs) {
						pw.println(message);
						pw.flush();
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
