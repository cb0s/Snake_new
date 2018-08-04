package utils.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Locale;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

import javax.swing.JOptionPane;

/**
 * 	@author Cedric	
 *	@version 1.0
 *	@category util</br></br>
 *
 *	Lets you load language Strings in your project by defining a locale.
 */
public class LangAdapter {
	
	// ******************
	// * Private fields *
	// ******************
	private static ResourceBundle resource;
	// TODO!
	private final static String path = "data/lang/snake";
	private final static Locale locale;
	
	static {
		locale = new Locale(ConfigAdapter.getDefaultConfig().getConfigString("language"), ConfigAdapter.getDefaultConfig().getConfigString("country"));
		try {
			if (Installer.isInstalled())
				resource = new PropertyResourceBundle(new FileInputStream(new File(path + "_" + locale.getLanguage() + "_" + locale.getCountry().toUpperCase() + ".lang")));
			else
				resource = new PropertyResourceBundle(LangAdapter.class.getResourceAsStream("/" + path + "_" + locale.getLanguage() + "_" + locale.getCountry().toUpperCase() + ".lang"));
		} catch (IOException e) {
			Logger.getDefaultLogger().logError("Couldn't load language!");
			String error = Logger.getDefaultLogger().logException(e);
			Logger.getDefaultLogger().logWarning("Using default language instead");
			try {
				if (Installer.isInstalled())
					resource = new PropertyResourceBundle(LangAdapter.class.getClassLoader().getResourceAsStream(path + "_snake_de_DE.lang"));
				else
					resource = new PropertyResourceBundle(new FileInputStream(new File(path + "_snake_de_DE.lang")));
			} catch (IOException e1) {
				Logger.getDefaultLogger().logError("Loading Backup-Language failed! Exiting...");
				error = Logger.getDefaultLogger().logException(e1);
				JOptionPane.showMessageDialog(null, "Loading languages failed!\n\nError: " + error + "\n\nExiting...", "Language Error!", JOptionPane.ERROR_MESSAGE);
				System.exit(1);
			}
			JOptionPane.showMessageDialog(null, "Loading Language failed...\nUsing default language de_DE now!", "Warning",JOptionPane.WARNING_MESSAGE);
		}
	}
	
	public static String getString(String key) {
		if(resource.containsKey(key)) {
			Logger.getDefaultLogger().logInfo("Loading " + key + " from Lang-File " + path + "_"+ locale.toString() + ".lang");
			return resource.getString(key);
		}
		Logger.getDefaultLogger().logWarning("Couldn't load " + key + " from Lang-File " + path + "_"+ locale.toString() + ".lang");
		return null;
	}
}
