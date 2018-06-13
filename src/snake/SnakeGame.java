package snake;

import java.awt.HeadlessException;
import java.io.File;
import java.net.URISyntaxException;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.UnsupportedLookAndFeelException;

import snake.io.ConfigAdapter;
import snake.io.Installer;
import snake.io.LangAdapter;
import snake.io.Logger;
import snake.ui.GameClock;
import snake.ui.UIManager;
import snake.ui.WindowAdapter;

/** 
 * 	@author Cedric	
 *	@version 1.0
 *	@category main
 *
 *	NOTE: 	The game supports multiple languages which can be loaded from the UI.
 *			Also there is an integrated Logger but it just comes in English. If anyone
 *			want to implement it feel free.
 **/

public class SnakeGame {
	
	public final static String loggerIniPath;
	public final static String iniPath;
	public final static String guiIniMainMenuPath;
	public final static String buttonIniPath;
	public final static String snakeIniPath;
	private static boolean logging;
	
	static {
		loggerIniPath = "data/ini/logger.ini";
		iniPath = "data/ini/gui/gui.ini";
		guiIniMainMenuPath = "data/ini/gui/gui_main_menu.ini";
		buttonIniPath = "data/ini/gui/button.ini";
		snakeIniPath = "data/ini/game/snake.ini";
	}
	
	private static Logger log;
	
	public static void log(String msg) {
		log.log(msg);
	}
	
	public static void stop() {
		try {
			WindowAdapter.mainDestroy();
			GameClock.stop();
			try {
				// That all Threads can go offline
				Thread.sleep(100);
			} catch (InterruptedException e) {
				Error.printError(e);
			}
			log(Logger.LoggingType.INFO.type + "Exiting now...");
		} catch (Exception e) {
			log(Logger.LoggingType.WARNING.type + "Frames and Gameclock have not been initialized");
		}

		System.exit(0);
	}
	
	private static void initLogger(String[] args) {
		if(Installer.isInstalled()) {
			if(Boolean.parseBoolean(ConfigAdapter.getConfigString("logging"))) logging = true;
			else if(args.length != 0) {
				for(int i = 0; i < args.length; i++) {
					if(args[i].equalsIgnoreCase("-d")) {
						if(ConfigAdapter.getConfigString("debugging").equals("on")) {
							logging = true;
						} else {
							logging = false;
						}
					} else if(args.length != i+1) {
						if(args[i].equalsIgnoreCase("-dp")) {
							if(args[i+1].equalsIgnoreCase("on")) {
								ConfigAdapter.setConfigString("logging", "true");
								ConfigAdapter.setConfigString("debugging", "on");
							}
							else if(args[i+1].equalsIgnoreCase("off")) ConfigAdapter.setConfigString("logging", "false");
							logging = true;
						}
						if(args[i].equalsIgnoreCase("-da")) {
							if(args[i+1].equalsIgnoreCase("on")) {
								ConfigAdapter.setConfigString("debugging", "on");
								logging = true;
							} else {
								ConfigAdapter.setConfigString("debugging", "off");
								logging = false;
							}
						}
					} else {
						System.out.println(Logger.LoggingType.WARNING.type + "Unknown arguments...");
						break;
					}
				}
			}	
		} else logging = false;
		log = new Logger(logging);
	}
	
	private static void initGraphics() {
		// Trying to enable OpenGL
		try {
			log(Logger.LoggingType.INFO.type + "Activating OpenGL");
			System.setProperty("sun.java2d.opengl", "true");
		} catch (Exception e) {
			log(Logger.LoggingType.ERROR.type + "Activating OpenGL failed!");
			log(Logger.LoggingType.ERROR.type + e.getMessage());
			JOptionPane.showMessageDialog(null, "Activating OpenGL failed!\nExiting");
			System.exit(1);
		}
		if(!System.getProperty("sun.java2d.opengl").equals("true")) log(Logger.LoggingType.WARNING.type + "OpenGL is not activated now");
		else log(Logger.LoggingType.INFO.type + "OpenGL successfully loaded");
		
		// Setting LookAndFeel
		try {
			log(Logger.LoggingType.INFO.type + "Enabling SystemLookAndFeel");
			javax.swing.UIManager.setLookAndFeel(javax.swing.UIManager.getSystemLookAndFeelClassName());
			log(Logger.LoggingType.INFO.type + "SystemLookAndFeel successfully loaded");
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
			log(Logger.LoggingType.ERROR.type + "Setting UILookAndFeel to SystemLookAndFeel failed!");
			Error.printError(e);
			log(Logger.LoggingType.WARNING + "The UI may be a bit different to what you are used to");
			JOptionPane.showMessageDialog(null, "The UI may appear a bit different due to loading errors", "Info", JOptionPane.INFORMATION_MESSAGE);
		}
	}

	/**
	 * @author Cedric
	 * @param args
	 * 
	 * You can control Snake a bit from console. For example you can specify how your Logger works:
	 * Use following Arguments to manually control Logger:
	 *  -d: debug-Mode: Enabling Logger with File-Write-Permission if debugging is enabled in Config
	 *  -dp: - if following arg = on: Enabling Logger and writing this permission also to the Config
	 *  	 - if following arg = off: Disabling Loggger and disabling it also by Config (logging)
	 *  -da: can enable (following arg = on) or disable (following arg = off) debugging by config
	 * 
	 */
	public static void main(String[] args) {
		initLogger(args);
		String argsString = "";
		for(String s : args) argsString += ", " + s;
		if(argsString.equals("")) argsString = "NONE"; 
		log(Logger.LoggingType.INFO.type + "Logger successfully initialized -> args: " + argsString);
		initGraphics();
		if(!Installer.isInstalled()) {
			log(Logger.LoggingType.INFO.type + "Asking for installation");
			int result = JOptionPane.NO_OPTION;
			try {
				result = JOptionPane.showConfirmDialog(null, LangAdapter.getString("installer_start-question").replaceAll("%installation_dir%", SnakeGame.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath().replaceAll(File.pathSeparator, "/")), LangAdapter.getString("installer_title"), JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
			} catch (HeadlessException | URISyntaxException e) {
				log(Logger.LoggingType.ERROR.type + "An unexpected error occured!");
				String msg = Error.printError(e);
				JOptionPane.showMessageDialog(null, "An unexpected error occured, while loading installation.\n\nError:\n" + msg, "Installation-Error", JOptionPane.ERROR_MESSAGE);
			}
			if(result == JOptionPane.YES_OPTION)
				Installer.install(true);
			else {
				log(Logger.LoggingType.INFO.type + "Declined installation");
				if (result == JOptionPane.CANCEL_OPTION) {
					log(Logger.LoggingType.INFO.type + "User terminates");
					stop();
				}
			}
		}
		
		// Starts UIManager
		SwingUtilities.invokeLater(new Runnable() { @Override public void run() {UIManager.init();}});
	}
	
	public static boolean isLogging() {
		return logging;
	}
}
