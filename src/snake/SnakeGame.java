package snake;

import java.awt.HeadlessException;
import java.io.File;
import java.net.URISyntaxException;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.UnsupportedLookAndFeelException;

import snake.ui.GameClock;
import snake.ui.UIManager;
import snake.ui.WindowAdapter;
import utils.io.Installer;
import utils.io.LangAdapter;
import utils.io.Logger;

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
	
	static {
		loggerIniPath = "data/ini/logger.ini";
		iniPath = "data/ini/gui/gui.ini";
		guiIniMainMenuPath = "data/ini/gui/gui_main_menu.ini";
		buttonIniPath = "data/ini/gui/button.ini";
		snakeIniPath = "data/ini/game/snake.ini";
	}
	
	public static void stop() {
		try {
			WindowAdapter.mainDestroy();
			GameClock.stop();
			try {
				// That all Threads can go offline
				Thread.sleep(100);
			} catch (InterruptedException e) {
				Logger.getDefaultLogger().logException(e);
			}
			Logger.getDefaultLogger().logInfo("Exiting now...");
		} catch (Exception e) {
			Logger.getDefaultLogger().logError("Frames and Gameclock have not been initialized");
		}

		System.exit(0);
	}
	
	private static void initGraphics() {
		// Trying to enable OpenGL
		try {
			Logger.getDefaultLogger().logInfo("Activating OpenGL");
			System.setProperty("sun.java2d.opengl", "true");
		} catch (Exception e) {
			Logger.getDefaultLogger().logError("Activating OpenGL failed!");
			Logger.getDefaultLogger().logError(e.getMessage());
			JOptionPane.showMessageDialog(null, "Activating OpenGL failed!\nExiting");
			System.exit(1);
		}
		if(!System.getProperty("sun.java2d.opengl").equals("true")) 
			Logger.getDefaultLogger().logWarning("OpenGL is not activated now");
		else 
			Logger.getDefaultLogger().logInfo("OpenGL successfully loaded");
		
		// Setting LookAndFeel
		try {
			Logger.getDefaultLogger().logInfo("Enabling SystemLookAndFeel");
			javax.swing.UIManager.setLookAndFeel(javax.swing.UIManager.getSystemLookAndFeelClassName());
			Logger.getDefaultLogger().logInfo("SystemLookAndFeel successfully loaded");
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
			Logger.getDefaultLogger().logError("Setting UILookAndFeel to SystemLookAndFeel failed!");
			Logger.getDefaultLogger().logException(e);
			Logger.getDefaultLogger().logWarning("The UI may be a bit different to what you are used to");
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
		String argsString = "";
		for(String s : args) argsString += ", " + s;
		if(argsString.equals("")) argsString = "NONE"; 
		Logger.getDefaultLogger().logInfo("Logger successfully initialized -> args: " + argsString);
		initGraphics();
		if(!Installer.isInstalled()) {
			Logger.getDefaultLogger().logInfo("Asking for installation");
			int result = JOptionPane.NO_OPTION;
			try {
				result = JOptionPane.showConfirmDialog(null, LangAdapter.getString("installer_start-question").replaceAll("%installation_dir%", SnakeGame.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath().replaceAll(File.pathSeparator, "/")), LangAdapter.getString("installer_title"), JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
			} catch (HeadlessException | URISyntaxException e) {
				Logger.getDefaultLogger().logError("An unexpected error occured!");
				String msg = Logger.getDefaultLogger().logException(e);
				JOptionPane.showMessageDialog(null, "An unexpected error occured, while loading installation.\n\nError:\n" + msg, "Installation-Error", JOptionPane.ERROR_MESSAGE);
			}
			if(result == JOptionPane.YES_OPTION)
				Installer.install(true);
			else {
				Logger.getDefaultLogger().logInfo("Declined installation");
				if (result == JOptionPane.CANCEL_OPTION) {
					Logger.getDefaultLogger().logInfo("User terminates");
					stop();
				}
			}
		}
		
		// Starts UIManager
		SwingUtilities.invokeLater(new Runnable() { @Override public void run() {UIManager.init();}});
	}
	
}
