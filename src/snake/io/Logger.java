package snake.io;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.Calendar;
import java.util.GregorianCalendar;

import snake.SnakeGame;

/**
 *	@author Cedric	
 *	@version 1.0
 *	@category io
 */

public class Logger {
	public final static IniAdapter ini;
	private boolean logging;
	private File logger;
	private long start;
	private static final boolean loadMonthFromLang;
	private static final boolean startNewThread;
	
	static {
		ini = new IniAdapter();
		loadMonthFromLang = Boolean.parseBoolean(ini.getString(SnakeGame.loggerIniPath, "loadFromLangFiles"));
		startNewThread = Boolean.parseBoolean(ini.getString(SnakeGame.loggerIniPath, "startNewThread"));
	}
	
	public Logger(boolean logging) {
		this.logging = logging;
	}
	
	public Logger(boolean logging, File file) {
		this.logging = logging;
		logger = file;
	}
	
	private enum Months {
		JANUARY(!loadMonthFromLang ? ini.getString(SnakeGame.loggerIniPath, "January") : LangAdapter.getString("January")),
		FEBRUARY(!loadMonthFromLang ? ini.getString(SnakeGame.loggerIniPath, "February") : LangAdapter.getString("February")),
		MARCH(!loadMonthFromLang ? ini.getString(SnakeGame.loggerIniPath, "March") : LangAdapter.getString("March")),
		APRIL(!loadMonthFromLang ? ini.getString(SnakeGame.loggerIniPath, "April") : LangAdapter.getString("April")),
		MAY(!loadMonthFromLang ? ini.getString(SnakeGame.loggerIniPath, "May") : LangAdapter.getString("May")),
		JUNE(!loadMonthFromLang ? ini.getString(SnakeGame.loggerIniPath, "June") : LangAdapter.getString("June")),
		JULY(!loadMonthFromLang ? ini.getString(SnakeGame.loggerIniPath, "July") : LangAdapter.getString("July")),
		AUGUST(!loadMonthFromLang ? ini.getString(SnakeGame.loggerIniPath, "August") : LangAdapter.getString("August")),
		SEPTEMBER(!loadMonthFromLang ? ini.getString(SnakeGame.loggerIniPath, "September") : LangAdapter.getString("September")),
		OCTOBER(!loadMonthFromLang ? ini.getString(SnakeGame.loggerIniPath, "October") : LangAdapter.getString("October")),
		NOVEMBER(!loadMonthFromLang ? ini.getString(SnakeGame.loggerIniPath, "November") : LangAdapter.getString("November")),
		DECEMBER(!loadMonthFromLang ? ini.getString(SnakeGame.loggerIniPath, "December") : LangAdapter.getString("December"));
		
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
	
	public enum LoggingType {
		INFO("[Info] "),
		WARNING("[Warning] "),
		ERROR("[Error] ");
		
		public final String type;
		
		private LoggingType(String type) {
			this.type = type;
		}
	}
	
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
		
		switch(ini.getString(SnakeGame.loggerIniPath, "time_format")) {
		case "dd/m/yyyy-24hh:mm:ss":
			return day + "/" + month + "/" + year + "-" + hour24 + ":" + minute + ":" + second;
		case "dd/m/yyyy-hh:mm:ss":
			return day + "/" + month + "/" + year + "-" + hour + am_pm + ":" + minute + ":" + second;
		case "m/dd/yyyy-hh:mm:ss":
			return month + "/" + day + "/" + year + "-" + hour + am_pm + ":" + minute + ":" + second;
		case "yyyy/m/dd-24hh:mm:ss":
			return year + "/" + month + "/" + day + "-" + hour24 + ":" + minute + ":" + second;
		case "yyyy/m/dd-hh:mm:ss":
			return year + "/" + month + "/" + day + "-" + hour + am_pm + ":" + minute + ":" + second;
		case "yyyy/dd/m-hh:mm:ss":
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
			return ""+gregorianCalendar.getTime();
		}
	}

	public void log(String s) {
		Runnable r = new Runnable() {
			@Override
			public void run () {
				String date = getTime();
				String msg = "[" + date + "] " + s;
				System.out.println(msg);
				
				if(logging) {
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
						logging = false;
						exception.printStackTrace();
						log(LoggingType.ERROR.type + exception.toString());
						logging = true;
					}
				}
			}
		};
		if (startNewThread) new Thread (r).start();
		else r.run();
	}
}
