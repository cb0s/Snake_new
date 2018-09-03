package utils.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Locale;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

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
			Logger.gdL().logExceptionGraphical(e, "Loading Language failed...\nUsing default language de_DE now!", "Language-Error", false);
			Logger.getDefaultLogger().logWarning("Using default language instead");
			try {
				if (Installer.isInstalled())
					resource = new PropertyResourceBundle(LangAdapter.class.getClassLoader().getResourceAsStream(path + "_snake_de_DE.lang"));
				else
					resource = new PropertyResourceBundle(new FileInputStream(new File(path + "_snake_de_DE.lang")));
			} catch (IOException e1) {
				Logger.getDefaultLogger().logError("Loading Backup-Language failed! Exiting...");
				Logger.gdL().logExceptionGraphical(e1, "Loading languages failed!\\n\\nError:\n%exception%\n\nExiting...", "Language Error!", true);
			}
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
