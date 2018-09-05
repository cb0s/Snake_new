package utils.io;

import java.io.OutputStream;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.GregorianCalendar;
import java.util.LinkedList;
import java.util.concurrent.LinkedBlockingQueue;

import javax.swing.JOptionPane;

/**
 * Lets you log text and Exceptions asynchronously with timestamp and optional prefix.</br></br>
 * 
 * <b>Use:</b> Instantiate a new logger or use the standard logger {@link #getDefaultLogger()}.<br>
 * There are multiple prebuilt methods for logging:<ul>
 * <li>{@link #log(String)} for plain logging</li>
 * <li>{@link #logInfo(String)} for logging information</li>
 * <li>{@link #logWarning} for logging warnings</li>
 * <li>{@link #logError} for logging errors</li>
 * <li>{@link #logException(Exception)} for logging Exceptions</li>
 * <li>{@link #logExceptionGraphical(Exception, String, String, boolean)} for logging Exceptions and show them in a TextBox on the screen</li>
 * </ul>
 * 
 * @author Leo, Cedric
 * @version 3.2
 * @category util
 */
public class Logger {

	// *************
	// * Constants *
	// *************
	private final String PREFIX;
	private static final String PREFIX_INFO = "[Info] ";
	private static final String PREFIX_WARNING = "[Warning] ";
	private static final String PREFIX_ERROR = "[Error] ";
	
	public static final String DEAFAULT_DATE_TIME_PATTERN = "dd/MM/yyyy-HH:mm:ss";
	
	


	// **********
	// * Fields *
	// **********
	private static Logger defaultLogger;
	private static LinkedList<Logger> runningThreads;
	
	private final SimpleDateFormat simpleDateFormat;
	private LinkedBlockingQueue<String> buffer;
	private Thread worker;
	private volatile boolean running;


	static {
		runningThreads = new LinkedList<Logger>();
	}
	
	
	// ****************
	// * Constructors *
	// ****************
	private Logger(String dateTimePattern, OutputStream[] outputs, String prefix, boolean isDefault) {
		if(dateTimePattern == null || outputs == null) {
			throw new NullPointerException("Arguments must not be null!");
		}
		this.PREFIX = prefix == null ? "" : prefix + " ";
		this.simpleDateFormat = new SimpleDateFormat(dateTimePattern);
		buffer = new LinkedBlockingQueue<>();
		this.worker = new Thread(new LoggerWorker(buffer, Arrays.stream(outputs).map(PrintWriter::new).toArray(PrintWriter[]::new)));
		this.worker.setDaemon(true);
		this.worker.start();
		if (!isDefault) runningThreads.add(this);
		this.log("----------------------------new-Session-started----------------------------\n");
	}
	
	/**
	 * Creates a new logger using: <ul>
	 * <li>{@link #DEAFAULT_DATE_TIME_PATTERN}</li>
	 * <li>{@link System#out} as output</li>
	 * </ul>
	 * These things can <b>not</b> be changed afterwards to ensure consistency within a logging session.
	 */
	public Logger() {
		this(DEAFAULT_DATE_TIME_PATTERN, new OutputStream[] {System.out});
	}

	/**
	 * Creates a new logger using: <ul>
	 * <li>the given date-time pattern</li>
	 * <li>{@link System#out} as output</li>
	 * </ul>
	 * These things can <b>not</b> be changed afterwards to ensure consistency within a logging session.<br><br>
	 * 
	 * @param dateTimePattern the pattern for the timestamp
	 */
	public Logger(String dateTimePattern) {
		this(dateTimePattern, new OutputStream[] {System.out});
	}

	/**
	 * Creates a new logger using: <ul>
	 * <li>{@link #DEAFAULT_DATE_TIME_PATTERN}</li>
	 * <li>the given OutputStream as output</li>
	 * </ul>
	 * These things can <b>not</b> be changed afterwards to ensure consistency within a logging session.<br><br>
	 * 
	 * @param output the output channel
	 */
	public Logger(OutputStream output) {
		this(DEAFAULT_DATE_TIME_PATTERN, new OutputStream[] {output});
	}

	/**
	 * Creates a new logger using: <ul>
	 * <li>the given date-time pattern</li>
	 * <li>the given OutputStream as output</li>
	 * </ul>
	 * These things can <b>not</b> be changed afterwards to ensure consistency within a logging session.<br><br>
	 * 
	 * @param dateTimePattern the pattern for the timestamp
	 * @param output the output channel
	 */
	public Logger(String dateTimePattern, OutputStream output) {
		this(dateTimePattern, new OutputStream[] {output});
	}

	/**
	 * Creates a new logger using: <ul>
	 * <li>{@link #DEAFAULT_DATE_TIME_PATTERN}</li>
	 * <li>the given OutputStreams as outputs</li>
	 * </ul>
	 * These things can <b>not</b> be changed afterwards to ensure consistency within a logging session.<br><br>
	 * 
	 * @param outputs the output channels
	 */
	public Logger(OutputStream[] outputs) {
		this(DEAFAULT_DATE_TIME_PATTERN, outputs);
	}

	/**
	 * Creates a new logger using: <ul>
	 * <li>the given OutputStreams as outputs</li>
	 * <li>the given prefix</li>
	 * </ul>
	 * These things can <b>not</b> be changed afterwards to ensure consistency within a logging session.<br><br>
	 * 
	 * @param outputs the output channels
	 * @param prefix the prefix in front of every log
	 */
	public Logger(OutputStream[] outputs, String prefix) {
		this(DEAFAULT_DATE_TIME_PATTERN, outputs, prefix);
	}
	
	/**
	 * Creates a new logger using: <ul>
	 * <li>the given date-time pattern</li>
	 * <li>the given OutputStreams as outputs</li>
	 * </ul>
	 * These things can <b>not</b> be changed afterwards to ensure consistency within a logging session.<br><br>
	 * 
	 * @param dateTimePattern the pattern for the timestamp
	 * @param outputs the output channels
	 */
	public Logger(String dateTimePattern, OutputStream[] outputs) {
		this(dateTimePattern, outputs, null);
	}
	
	/**
	 * Creates a new logger using: <ul>
	 * <li>the given date-time pattern</li>
	 * <li>the given OutputStreams as outputs</li>
	 * <li>the given prefix</li>
	 * </ul>
	 * These things can <b>not</b> be changed afterwards to ensure consistency within a logging session.<br><br>
	 * 
	 * @param dateTimePattern the pattern for the timestamp
	 * @param outputs the output channels
	 * @param prefix the prefix in front of every log
	 */
	public Logger(String dateTimePattern, OutputStream[] outputs, String prefix) {
		this(dateTimePattern, outputs, prefix, false);
	}



	// *******************
	// * Package methods *
	// *******************
	/**
	 * Returns the current date-time parsed in this logger's pattern.
	 * 
	 * @return the current date-time parsed in this logger's pattern
	 */
	String getTime() {
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
			if (running) buffer.add("["+getTime()+"] " + PREFIX + text);
		} catch (IllegalStateException e) {
			System.out.println("Logger buffer out of bounds!");
			e.printStackTrace();
		}
	}

	/**
	 * Logs the given text with the prefix {@value #PREFIX_INFO}.
	 * 
	 * @param text the text to log
	 */
	public synchronized void logInfo(String text) {
		log(PREFIX_INFO + text);
	}

	/**
	 * Logs the given text with the prefix {@value #PREFIX_WARNING}.
	 * 
	 * @param text the text to log
	 */
	public synchronized void logWarning(String text) {
		log(PREFIX_WARNING + text);
	}

	/**
	 * Logs the given text with the prefix {@value #PREFIX_ERROR}.
	 * 
	 * @param text the text to log
	 */
	public synchronized void logError(String text) {
		log(PREFIX_ERROR + text);
	}

	/**
	 * Logs the given exception.
	 * 
	 * @param exception the exception to log
	 */
	public synchronized String logException(Throwable exception) {
		String exMsg = exception.toString() + '\n';
		for (StackTraceElement ste : exception.getStackTrace()) {
			exMsg+="\tat "+ste.toString()+'\n';
		}
		logError(exMsg);
		return exMsg;
	}
	
	/**
	 * 
	 * @param exception
	 * @param msg The Error-Message where <i>%exception%</i> gets replaced by the message of the exception.
	 * @param title The title of the Error-Message
	 * @param exit Whether the program should exit after that log or not
	 * @return 
	 */
	public String logExceptionGraphical(Throwable exception, String msg, String title, boolean exit) {
		String exMsg = logException(exception);
		if (msg != null) JOptionPane.showMessageDialog(null, msg.replace("%exception%", exMsg), title, JOptionPane.ERROR_MESSAGE);
		if (exit) System.exit(1);
		return exMsg;
	}
	
	
	/**
	 * Returns the default logger.
	 * 
	 * @return the default logger
	 */
	public static synchronized Logger getDefaultLogger() {
		if(defaultLogger == null) {
			defaultLogger = new Logger(DEAFAULT_DATE_TIME_PATTERN, new OutputStream[] {System.out}, null, true);
		}
		return defaultLogger;
	}
	/**
	 * Returns the default logger.<br>
	 * Short for {@link #getDefaultLogger()}.
	 * 
	 * @return the default logger
	 */
	public static synchronized Logger gdL() {
		return getDefaultLogger();
	}
	/**
	 * Sets the default logger.
	 * 
	 * @param logger the logger to set as default
	 */
	public static synchronized void setDefaultLogger(Logger logger) {
		Logger.defaultLogger = logger;
	}
	
	public synchronized void shutdown() {
		running = false;
		log("Shutting down Logger");
		runningThreads.remove(this);
		defaultLogger.logInfo("Shut down Logger with prefix " + PREFIX);
	}
	
	public static void shutdownAll() {
		defaultLogger.logInfo("Shutting down all Loggers");
		for (Logger l : runningThreads) l.shutdown();
	}
	
	public synchronized void stop() throws InterruptedException {
		if (running) {
			shutdown();
			if (worker.isAlive())
				worker.join();
		}
	}
	
	/**
	 * Stops all custom loggers.
	 */
	public static void stopAll() throws InterruptedException {
		defaultLogger.logInfo("Stopping all Loggers");
		for (Logger l : runningThreads) l.stop();
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
			running = true;
			while(running) {
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
