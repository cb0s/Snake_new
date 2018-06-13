package snake.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Locale;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

import javax.swing.JOptionPane;

import snake.Error;
import snake.SnakeGame;

/**
 * 	@author Cedric	
 *	@version 1.0
 *	@category io
 */

public class LangAdapter {
	
	private static ResourceBundle resource;
	private final static String path = "data/lang/snake";
	private final static Locale locale;
	
	static {
		locale = new Locale(ConfigAdapter.getConfigString("language"), ConfigAdapter.getConfigString("country"));
		try {
			if (Installer.isInstalled())
				resource = new PropertyResourceBundle(new FileInputStream(new File(path + "_" + locale.getLanguage() + "_" + locale.getCountry().toUpperCase() + ".lang")));
			else
				resource = new PropertyResourceBundle(LangAdapter.class.getResourceAsStream("/" + path + "_" + locale.getLanguage() + "_" + locale.getCountry().toUpperCase() + ".lang"));
		} catch (IOException e) {
			SnakeGame.log(Logger.LoggingType.ERROR.type + "Couldn't load language!");
			String error = Error.printError(e);
			SnakeGame.log(Logger.LoggingType.WARNING.type + "Using default language instead");
			try {
				if (Installer.isInstalled())
					resource = new PropertyResourceBundle(LangAdapter.class.getClassLoader().getResourceAsStream(path + "_snake_de_DE.lang"));
				else
					resource = new PropertyResourceBundle(new FileInputStream(new File(path + "_snake_de_DE.lang")));
			} catch (IOException e1) {
				SnakeGame.log(Logger.LoggingType.ERROR.type + "Loading Backup-Language failed! Exiting...");
				error = Error.printError(e1);
				JOptionPane.showMessageDialog(null, "Loading languages failed!\n\nError: " + error + "\n\nExiting...", "Language Error!", JOptionPane.ERROR_MESSAGE);
				System.exit(1);
			}
			JOptionPane.showMessageDialog(null, "Loading Language failed...\nUsing default language de_DE now!", "Warning",JOptionPane.WARNING_MESSAGE);
		}
	}
	
	public static String getString(String key) {
		if(resource.containsKey(key)) {
			SnakeGame.log(Logger.LoggingType.INFO.type + "Loading " + key + " from Lang-File " + path + "_"+ locale.toString() + ".lang");
			return resource.getString(key);
		}
		SnakeGame.log(Logger.LoggingType.WARNING.type + "Couldn't load " + key + " from Lang-File " + path + "_"+ locale.toString() + ".lang");
		return null;
	}
}
