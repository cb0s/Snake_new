package snake;

import java.awt.Toolkit;
import javax.swing.JOptionPane;
import javax.swing.UnsupportedLookAndFeelException;

import snake.ui.MainMenuState;
import utils.Maths;
import utils.io.IniAdapter;
import utils.io.Logger;
import utils.io.PathsLoader;

/** 
 * 	@author Cedric	
 *	@version 1.1
 *	@category main</br></br>
 *
 *	<b>NOTE:</b> The game supports multiple languages which can be loaded from the UI.
 *			Also there is an integrated Logger but it just comes in English. If anyone
 *			wants to implement multiple languages for it feel free.
 **/

public class SnakeGame {

	private static IniAdapter guiIni;
	private static Game snake;
	
	private static void initGraphics() {
		// Trying to enable OpenGL
		try {
			Logger.gdL().logInfo("Activating OpenGL");
			System.setProperty("sun.java2d.opengl", "true");
		} catch (Exception e) {
			Logger.gdL().logError("Activating OpenGL failed!");
			Logger.gdL().logError(e.getMessage());
			JOptionPane.showMessageDialog(null, "Activating OpenGL failed!\nExiting");
			System.exit(1);
		}
		if(!System.getProperty("sun.java2d.opengl").equals("true")) 
			Logger.gdL().logWarning("OpenGL is not activated now");
		else 
			Logger.gdL().logInfo("OpenGL successfully loaded");
		
		// Setting LookAndFeel
		try {
			Logger.gdL().logInfo("Enabling SystemLookAndFeel");
			javax.swing.UIManager.setLookAndFeel(javax.swing.UIManager.getSystemLookAndFeelClassName());
			Logger.gdL().logInfo("SystemLookAndFeel successfully loaded");
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
			Logger.gdL().logError("Setting UILookAndFeel to SystemLookAndFeel failed!");
			Logger.gdL().logExceptionGraphical(e, "An Error occured while loading the SystemLookAndFeel.\nThe UI may appear a bit different due to these Errors.\n\nError:\n%exception", "Warning", false);
			Logger.gdL().logWarning("The UI may be a bit different to what you are used to");
		}
	}
	
	public static Game getSnake() {
		return snake;
	}
	
	/**
	 * This method starts the program with the arguments args.
	 * 
	 * @param args Arguments when for starting program
	 */
	public static void main(String[] args) {
		try {
			Logger.getDefaultLogger().logInfo("Starting Snake");
			
			guiIni = new IniAdapter(PathsLoader.getSavedPath("gui_ini"));
			initGraphics();
			
			snake = new Game(MainMenuState.mainMenuIni.getString("title"), (int) Maths.format(guiIni.getString("width").replace("%screensize%", Toolkit.getDefaultToolkit().getScreenSize().width+"")), (int) Maths.format(guiIni.getString("height").replace("%screensize%", Toolkit.getDefaultToolkit().getScreenSize().height+"")));
		
		} catch (Error | Exception e) {
			try {
				Logger.gdL().logError("A fatal error occured while running Snake! Error could not be identified!\nError:");
				Logger.gdL().logExceptionGraphical(e, "A fatal error occured while running Snake! Error could not be identified!\n\nError:\n%exception%\n\nExiting", "Fatal Error!", true);
			} catch(Exception e2) {
				System.err.println("FATAL ERROR! THIS COULD NOT BE LOGGED!");
				e.printStackTrace();
				System.err.println("LOGGING ERROR:");
				e2.printStackTrace();
			}
			System.exit(2);
		}	
	}
}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	/* Old Code (!)
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
			Logger.gdiL().log("Exiting now...");
		} catch (Exception e) {
			Logger.gdeL().log("Frames and Gameclock have not been initialized");
		}

		System.exit(0);
	}
	
	private static void initGraphics() {
		// Trying to enable OpenGL
		try {
			Logger.gdiL().log("Activating OpenGL");
			System.setProperty("sun.java2d.opengl", "true");
		} catch (Exception e) {
			Logger.gdeL().log("Activating OpenGL failed!");
			Logger.gdeL().log(e.getMessage());
			JOptionPane.showMessageDialog(null, "Activating OpenGL failed!\nExiting");
			System.exit(1);
		}
		if(!System.getProperty("sun.java2d.opengl").equals("true")) 
			Logger.gdwL().log("OpenGL is not activated now");
		else 
			Logger.gdiL().log("OpenGL successfully loaded");
		
		// Setting LookAndFeel
		try {
			Logger.gdiL().log("Enabling SystemLookAndFeel");
			javax.swing.UIManager.setLookAndFeel(javax.swing.UIManager.getSystemLookAndFeelClassName());
			Logger.gdiL().log("SystemLookAndFeel successfully loaded");
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
			Logger.gdeL().log("Setting UILookAndFeel to SystemLookAndFeel failed!");
			Logger.getDefaultLogger().logException(e);
			Logger.gdwL().log("The UI may be a bit different to what you are used to");
			JOptionPane.showMessageDialog(null, "The UI may appear a bit different due to loading errors", "Info", JOptionPane.INFORMATION_MESSAGE);
		}
	}*/
//
//	/**
//	 * @author Cedric
//	 * @param args
//	 * 
//	 * You can control Snake a bit from console. For example you can specify how your Logger works:
//	 * Use following Arguments to manually control Logger:
//	 *  -d: debug-Mode: Enabling Logger with File-Write-Permission if debugging is enabled in Config
//	 *  -dp: - if following arg = on: Enabling Logger and writing this permission also to the Config
//	 *  	 - if following arg = off: Disabling Loggger and disabling it also by Config (logging)
//	 *  -da: can enable (following arg = on) or disable (following arg = off) debugging by config
//	 * 
//	 */
	/*public static void main(String[] args) {
		String argsString = "";
		for(String s : args) argsString += ", " + s;
		if(argsString.equals("")) argsString = "NONE"; 
		Logger.gdiL().log("Logger successfully initialized -> args: " + argsString);
		initGraphics();
		if(!Installer.isInstalled()) {
			Logger.gdiL().log("Asking for installation");
			int result = JOptionPane.NO_OPTION;
			try {
				result = JOptionPane.showConfirmDialog(null, LangAdapter.getString("installer_start-question").replaceAll("%installation_dir%", SnakeGame.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath().replaceAll(File.pathSeparator, "/")), LangAdapter.getString("installer_title"), JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
			} catch (HeadlessException | URISyntaxException e) {
				Logger.gdeL().log("An unexpected error occured!");
				String msg = Logger.getDefaultLogger().logException(e);
				JOptionPane.showMessageDialog(null, "An unexpected error occured, while loading installation.\n\nError:\n" + msg, "Installation-Error", JOptionPane.ERROR_MESSAGE);
			}
			if(result == JOptionPane.YES_OPTION)
				Installer.install(true);
			else {
				Logger.gdiL().log("Declined installation");
				if (result == JOptionPane.CANCEL_OPTION) {
					Logger.gdiL().log("User terminates");
					stop();
				}
			}
		}
		
		// Starts UIManager
		SwingUtilities.invokeLater(new Runnable() { @Override public void run() {UIManager.init();}});
	}
	*/
