package utils.io;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.concurrent.LinkedBlockingQueue;

import snake.SnakeGame;

/**
 * @author Leo, Cedric
 * @version 2.0
 * @category io
 */
public class Logger {

	// ******************************
	// Constants
	// ******************************
	public final static IniAdapter ini;
	private static final boolean loadMonthFromLang;
	private static final String INFO_PREFIX = "[Info]";
	private static final String WARNING_PREFIX = "[Warning]";
	private static final String ERROR_PREFIX = "[Error]";

	static {
		ini = new IniAdapter(SnakeGame.loggerIniPath);
		loadMonthFromLang = Boolean.parseBoolean(ini.getString("loadFromLangFiles"));
		
		boolean fileLogging = false;
		
		if(Installer.isInstalled()) {
			if(Boolean.parseBoolean(ConfigAdapter.getDefaultConfig().getConfigString("logging"))) fileLogging = true;
			/*
			else if(args.length != 0) {
				for(int i = 0; i < args.length; i++) {
					if(args[i].equalsIgnoreCase("-d")) {
						if(ConfigAdapter.getConfigString("debugging").equals("on")) {
							fileLogging = true;
						} else {
							fileLogging = false;
						}
					} else if(args.length != i+1) {
						if(args[i].equalsIgnoreCase("-dp")) {
							if(args[i+1].equalsIgnoreCase("on")) {
								ConfigAdapter.setConfigString("logging", "true");
								ConfigAdapter.setConfigString("debugging", "on");
							}
							else if(args[i+1].equalsIgnoreCase("off")) ConfigAdapter.setConfigString("logging", "false");
							fileLogging = true;
						}
						if(args[i].equalsIgnoreCase("-da")) {
							if(args[i+1].equalsIgnoreCase("on")) {
								ConfigAdapter.setConfigString("debugging", "on");
								fileLogging = true;
							} else {
								ConfigAdapter.setConfigString("debugging", "off");
								fileLogging = false;
							}
						}
					} else {
						System.out.println(Logger.LoggingType.WARNING.type + "Unknown arguments...");
						break;
					}
				}
			}
			*/
		}
		setDefaultLogger(new Logger(fileLogging));
	}

	
	
	
	// ******************************
	// Fields
	// ******************************
	private static Logger defaultLogger;
	
	private LinkedBlockingQueue<String> messages;
	private PrintWriter logFileWriter;

	
	

	// ******************************
	// Constructors
	// ******************************
	/**
	 * Creates a new logger.
	 */
	public Logger() {
		this(null);
	}
	
	/**
	 * Creates a new logger. If fileLogging is true, the log output will be written into the standard logfile.
	 * 
	 * @param fileLogging whether the log output will be written into the standard logfile
	 */
	public Logger(boolean fileLogging) {
		this(fileLogging ? new File(ini.getString("path").replace("*", "") + "snake.log") : null);
	}

	/**
	 * Creates a new logger that writes its ouput into the given file.
	 * 
	 * @param logfile the file to write the log output into
	 */
	public Logger(File logfile) {
		messages = new LinkedBlockingQueue<>();

		//initiates logfile writer if requested
		if(logfile != null) {
			try {
				if(!logfile.exists()) {
					File dir = new File(logfile.getAbsolutePath().substring(0, logfile.getAbsolutePath().lastIndexOf(logfile.getName())));
					if (!dir.exists()) dir.mkdir();
					logfile.createNewFile();
				}
				logFileWriter = new PrintWriter(new FileWriter(logfile, true));
				logFileWriter.write("\n----------------------------new-Session-started----------------------------\n");
				logFileWriter.flush();
			} catch (IOException e) {
				logError("Error while initializing logfile.");
				logError(e.getMessage());
				logFileWriter = null;
			}
		}
		System.out.println("\n----------------------------new-Session-started----------------------------\n");

		//processes the elements in the messages queue in a separate thread
		Thread loggerWorker = new Thread(new Runnable() {
			@Override
			public void run() {
				while (true) {
					try {
						String message = messages.take();
						System.out.println(message);

						if(logFileWriter != null) {
							logFileWriter.println(message);
							logFileWriter.flush();
						}

					} catch (InterruptedException e) {
						logError(e.getMessage());
					}
				}
			}
		});
		//optional
		loggerWorker.setDaemon(true);
		//end-optional
		loggerWorker.start();
	}

	
	

	// ******************************
	// Private methods
	// ******************************
	
	private enum Months {
		JANUARY(!loadMonthFromLang ? ini.getString("January") : LangAdapter.getString("January")),
		FEBRUARY(!loadMonthFromLang ? ini.getString("February") : LangAdapter.getString("February")),
		MARCH(!loadMonthFromLang ? ini.getString("March") : LangAdapter.getString("March")),
		APRIL(!loadMonthFromLang ? ini.getString("April") : LangAdapter.getString("April")),
		MAY(!loadMonthFromLang ? ini.getString("May") : LangAdapter.getString("May")),
		JUNE(!loadMonthFromLang ? ini.getString("June") : LangAdapter.getString("June")),
		JULY(!loadMonthFromLang ? ini.getString("July") : LangAdapter.getString("July")),
		AUGUST(!loadMonthFromLang ? ini.getString("August") : LangAdapter.getString("August")),
		SEPTEMBER(!loadMonthFromLang ? ini.getString("September") : LangAdapter.getString("September")),
		OCTOBER(!loadMonthFromLang ? ini.getString("October") : LangAdapter.getString("October")),
		NOVEMBER(!loadMonthFromLang ? ini.getString("November") : LangAdapter.getString("November")),
		DECEMBER(!loadMonthFromLang ? ini.getString("December") : LangAdapter.getString("December"));

		private final String monthShortForm;

		private Months(String monthShortForm) {
			this.monthShortForm = monthShortForm;
		}

		private String getMonthSF() {
			return monthShortForm;
		}

		private static Months getMonth(int id) {
			switch(id) {
			case 1:
				return JANUARY;
			case 2:
				return FEBRUARY;
			case 3:
				return MARCH;
			case 4:
				return APRIL;
			case 5:
				return MAY;
			case 6:
				return JUNE;
			case 7:
				return JULY;
			case 8:
				return AUGUST;
			case 9:
				return SEPTEMBER;
			case 10:
				return OCTOBER;
			case 11:
				return NOVEMBER;
			case 12:
				return DECEMBER;
			default:
				return null;
			}
		}
	}

	/**
	 * Internal method for adding text with a timestamp to the logger buffer.
	 * 
	 * @param text the text to enqueue in the buffer
	 */
	private synchronized void log(String text) {
		try {
			messages.add("["+getTime()+"] " + text);
		} catch (IllegalStateException e) {
			System.out.println("Logger buffer out of bounds!");
			e.printStackTrace();
		}
	}

	
	
	
	// ******************************
	// Public methods
	// ******************************
	/**
	 * Returns the time at the moment, it gets called
	 * 
	 * @return Returns the time at the moment, it gets called
	 */
	public static String getTime() {
		GregorianCalendar gregorianCalendar = new GregorianCalendar();
		int day_i = gregorianCalendar.get(Calendar.DAY_OF_MONTH);
		int month_i = gregorianCalendar.get(Calendar.MONTH)+1;
		int year = gregorianCalendar.get(Calendar.YEAR);
		int hour_i = gregorianCalendar.get(Calendar.HOUR);
		int hour24_i = gregorianCalendar.get(Calendar.HOUR_OF_DAY);
		String am_pm = gregorianCalendar.get(Calendar.AM_PM) == Calendar.AM ? "a" : "p";
		int minute_i = gregorianCalendar.get(Calendar.MINUTE);
		int second_i = gregorianCalendar.get(Calendar.SECOND);

		String day = day_i < 10 ? "0" + day_i : ""+day_i;
		String month = month_i < 10 ? "0" + month_i : ""+month_i;
		String hour = hour_i < 10 ? "0" + hour_i : ""+hour_i;
		String hour24 = hour24_i < 10 ? "0" + hour24_i : ""+hour24_i;
		String minute = minute_i < 10 ? "0" + minute_i : ""+minute_i;
		String second = second_i < 10 ? "0" + second_i : ""+second_i;

		switch(ini.getString("time_format")) {
		case "dd/mm/yyyy-24hh:mm:ss":
			return day + "/" + month + "/" + year + "-" + hour24 + ":" + minute + ":" + second;
		case "dd/mm/yyyy-hh:mm:ss":
			return day + "/" + month + "/" + year + "-" + hour + am_pm + ":" + minute + ":" + second;
		case "mm/dd/yyyy-hh:mm:ss":
			return month + "/" + day + "/" + year + "-" + hour + am_pm + ":" + minute + ":" + second;
		case "yyyy/mm/dd-24hh:mm:ss":
			return year + "/" + month + "/" + day + "-" + hour24 + ":" + minute + ":" + second;
		case "yyyy/mm/dd-hh:mm:ss":
			return year + "/" + month + "/" + day + "-" + hour + am_pm + ":" + minute + ":" + second;
		case "yyyy/dd/mm-hh:mm:ss":
			return year + "/" + day + "/" + month + "-" + hour + am_pm + ":" + minute + ":" + second;
		case "dd/mmm/yyyy-24hh:mm:ss":
			return day + "/" + Months.getMonth(month_i).getMonthSF() + "/" + year + "-" + hour24 + ":" + minute + ":" + second;
		case "dd/mmm/yyyy-hh:mm:ss":
			return day + "/" + Months.getMonth(month_i).getMonthSF() + "/" + year + "-" + hour + am_pm + ":" + minute + ":" + second;
		case "mmm/dd/yyyy-hh:mm:ss":
			return Months.getMonth(month_i).getMonthSF() + "/" + day + "/" + year + "-" + hour + am_pm + ":" + minute + ":" + second;
		case "yyyy/mmm/dd-24hh:mm:ss":
			return year + "/" + Months.getMonth(month_i).getMonthSF() + "/" + day + "-" + hour24 + ":" + minute + ":" + second;
		case "yyyy/mmm/dd-hh:mm:ss":
			return year + "/" + Months.getMonth(month_i).getMonthSF() + "/" + day + "-" + hour + am_pm + ":" + minute + ":" + second;
		case "yyyy/dd/mmm-hh:mm:ss":
			return year + "/" + day + "/" + Months.getMonth(month_i).getMonthSF() + "-" + hour + am_pm + ":" + minute + ":" + second;
		default:
			return gregorianCalendar.getTime().toString();
		}
	}
	
	/**
	 * Returns the default logger.
	 * 
	 * @return the default logger
	 */
	public static Logger getDefaultLogger() {
		return defaultLogger;
	}

	/**
	 * Sets the default logger.
	 * 
	 * @param defaultLogger the logger to set as default
	 */
	public static void setDefaultLogger(Logger defaultLogger) {
		Logger.defaultLogger = defaultLogger;
	}

	
	
	/**
	 * Logs an information text.
	 * 
	 * @param text the text to log
	 */
	public synchronized void logInfo(String text) {
		log(INFO_PREFIX + " " + text);
	}

	/**
	 * Logs a warning text.
	 * 
	 * @param text the text to log
	 */
	public synchronized void logWarning(String text) {
		log(WARNING_PREFIX + " " + text);
	}

	/**
	 * Logs an error text.
	 * 
	 * @param text the text to log
	 */
	public synchronized void logError(String text) {
		log(ERROR_PREFIX + " " + text);
	}
	
	/**
	 * Logs an exception like an error message.<br>
	 * (Replaces Error class.)
	 * 
	 * @param exception the exception to log
	 * @return 
	 */
	public synchronized String logException(Exception exception) {
		String msg = "";
		for (StackTraceElement ste : exception.getStackTrace()) {
			msg+=ste.toString()+'\n';
		}
		logError("Error-Message: " + msg);
		return msg;
	}
	
	/**
	 * Logs a plain text.
	 * 
	 * @param text the text to log
	 */
	public synchronized void logPlain(String text) {
		log(text);
	}
	
	/**
	 * Returns whether the logger is writing its output into a file.
	 * 
	 * @return whether file logging is activated
	 */
	public boolean isFileLogging() {
		return logFileWriter != null;
	}

	
	






	/*
	public void log_old(String s) {
		Runnable r = new Runnable() {
			@Override
			public void run () {
				String date = getTime();
				String msg = "[" + date + "] " + s;
				System.out.println(msg);

				if(fileLogging) {
					try {
						boolean breakup = false;
						switch (ini.getString(SnakeGame.loggerIniPath, "timeAfterSplit")) {
						case "none":
							if (logger == null) {
								logger = new File(ini.getString(SnakeGame.loggerIniPath, "path").replace("*", "") + "snake.log");
								breakup = true;
							}
							break;
						case "g":
							if(logger == null) {
								logger = new File(ini.getString(SnakeGame.loggerIniPath, "path").replace("*", "") + "snake_game_" + getTime() + ".log");
							}
							break;
						default:
							if(logger == null || start+Integer.parseInt(ini.getString(SnakeGame.loggerIniPath, "timeAfterSplit"))*60000 < System.currentTimeMillis()) {
								logger = new File(ini.getString(SnakeGame.loggerIniPath, "path").replace("*", "") + "snake_" + getTime() + ".log");
								start = System.currentTimeMillis();
							}
						}
						if(!logger.exists()) {
							new File(ini.getString(SnakeGame.loggerIniPath, "path").replace("*", "")).mkdirs();
							logger.createNewFile();
							log(LoggingType.INFO.type + "Created new Logging File (" + logger.getAbsolutePath() + ")");
						}
						PrintWriter w = new PrintWriter(new FileWriter(logger, true));
						if(breakup) w.write("\n----------------------------new-Session-started----------------------------\n");
						w.write(msg + '\n');
						w.flush();
						w.close();
					} catch(Exception exception) {
						fileLogging = false;
						exception.printStackTrace();
						log(LoggingType.ERROR.type + exception.toString());
						fileLogging = true;
					}
				}
			}
		};
		if (startNewThread) new Thread (r).start();
		else r.run();
	}
	*/
}
