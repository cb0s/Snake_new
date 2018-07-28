package utils.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javax.swing.JOptionPane;

/** 
 * 	@author Cedric	
 *	@version 2.0
 *	@category util</br></br>
 *
 *	This class allows you to load and write one file using a Properties syntax.</br>
 *
 **/
public class ConfigAdapter {
	
	// ******************
	// * Private fields *
	// ******************
	private PropertiesAdapter properties;
	private String path;
	private static ConfigAdapter defaultConfig;
	
	static {
		// Sets default values - They can be updated later with own ones
		String defaultPath;
		if (Installer.isInstalled())
			defaultPath = "data/config.cfg";
		else
			defaultPath = "/data/config.cfg";
		
		defaultConfig = new ConfigAdapter(defaultPath);
	}
	
	// ****************
	// * Constructors *
	// ****************
	/**
	 * The Constructor of the ConfigAdater
	 */
	public ConfigAdapter(String path) {
		properties = new PropertiesAdapter();
		updatePath(path);
	}
	
	// *******************
	// * Private Methods *
	// *******************
	/**
	 * Updates the Config-Path of the properties. Just call this method if you changed path!
	 * 
	 * @return <b>true:</b> if updating was successful</br><b>false:</b> if an error occured while updating
	 */
	private boolean updateConfigPath() {
		try {
			if (Installer.isInstalled())
				properties.updateProperties(new FileInputStream(new File(path)), path);
			else
				properties.updateProperties(ConfigAdapter.class.getResourceAsStream("/" + path), path);
			return true;
		} catch (IOException e) {
			Logger.getDefaultLogger().logError("Loading " + path + " failed!");
			String error = Logger.getDefaultLogger().logException(e);
			JOptionPane.showMessageDialog(null, "Error while loading " + path + "!\nTrying to continue!\n\nError:\n" + error, "Error", JOptionPane.ERROR_MESSAGE);
			return false;
		}
	}
	
	// ******************
	// * Public Methods *
	// ******************
	/**
	 * Returns a value assigned to the key in the config-File.
	 * 
	 * @param key is to specify which value should be loaded
	 * @return Value which is assigned to key
	 */
	public String getConfigString(String key) {
		return properties.getProperty(key);
	}
	
	/**
	 * Sets the value assigned to key of the config-File to a new value.
	 * 
	 * @param key is to specify which value should be updated
	 * @param value new value for the assigned value linked to the key
	 */
	public void setConfigString(String key, String value) {
		properties.setProperty(key, value);
	}
	
	/**
	 * Updates the location of the Config-File.</br>
	 * 
	 * @param path the location of the config-File
	 */
	public boolean updatePath(String path) {
		this.path = path;
		return updateConfigPath();
	}
	
	/**
	 * Updates the location of the defaultConfig-File if you don't want to use the default-location.</br>
	 * <b>default-path:</b> <i>data/config.cfg</i>
	 * 
	 * @param path the location of the config-File
	 */
	public static boolean updateDefaultPath(String path) {
		defaultConfig.updatePath(path);
		return defaultConfig.updateConfigPath();
	}
	
	/**
	 * 
	 * @return
	 */
	public static ConfigAdapter getDefaultConfig() {
		return defaultConfig;
	}
}
