package utils.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import utils.ui.DialogManager;

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
		String defaultPath = PathsLoader.getSavedPath("config");;
		if (!Installer.isInstalled())
			defaultPath = '/' + defaultPath;
		
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
			if (Installer.isInstalled()) properties.updateProperties(new FileInputStream(new File(path)), path);
			else properties.updateProperties(this.getClass().getResourceAsStream(path), path);
			return true;
		} catch (IOException e) {
			Logger.getDefaultLogger().logError("Loading " + path + " failed!");
			Logger.gdL().logException(e);
			DialogManager.showExeptionDialog(null, e, "Error while loading " + path + "!\n\nError:\n%exception%\n\nExiting now...", "Error", true);
			return false;
		}
	}
	
	// ******************
	// * Public Methods *
	// ******************
	/**
	 * Returns a value assigned to the key in the Config-File.
	 * 
	 * @param key is to specify which value should be loaded
	 * @return Value which is assigned to key
	 */
	public String getConfigString(String key) {
		String s = properties.getProperty(key);
		if (s != null) while (s.contains("%lang%")) s = s.substring(0, s.indexOf("%lang%")) + LangAdapter.getString(s.substring(s.indexOf("%lang%")+6, s.indexOf('%', s.indexOf("%lang%")+7))) + s.substring(s.indexOf('%', s.indexOf("%lang%")+7)+1);
		return s;
	}
	
	/**
	 * Sets the value assigned to key of the Config-File to a new value.
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
	 * @param path the location of the Config-File
	 */
	public boolean updatePath(String path) {
		this.path = path;
		return updateConfigPath();
	}
	
	/**
	 * Updates the location of the defaultConfig-File if you don't want to use the default-location.</br>
	 * <b>default-path:</b> <i>data/config.cfg</i>
	 * 
	 * @param path the location of the Config-File
	 */
	public static boolean updateDefaultPath(String path) {
		defaultConfig.updatePath(path);
		return defaultConfig.updateConfigPath();
	}
	
	/**
	 * Returns the default Config-Adapter
	 * 
	 * @return the default ConfigAdapter
	 */
	public static ConfigAdapter getDefaultConfig() {
		return defaultConfig;
	}
}
