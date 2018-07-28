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
 *	Allows you to read files in the properties syntax.</br>
 *	As you can just read files this is usually used for INI-Files.
 *
 **/

public class IniAdapter {
	
	// **********************
	// * Private Attributes *
	// **********************
	private String path;
	private PropertiesAdapter properties;
	
	/**
	 * Creates a new IniAdapter relating to path.
	 * 
	 * @param path specifies the file which needs to be loaded
	 */
	public IniAdapter(String path) {
		properties = new PropertiesAdapter();
		updatePath(path);
	}
	
	/**
	 * Returns a value stored in a properties-File linked to the given key.
	 * 
	 * @param key which the value you want is linked to
	 * @return The value which is linked to the given key in the properties-File
	 */
	public String getString(String key) {
		String s = properties.getProperty(key);
		if (s != null) while (s.contains("%lang%")) s = s.substring(0, s.indexOf("%lang%")) + LangAdapter.getString(s.substring(s.indexOf("%lang%")+6, s.indexOf('%', s.indexOf("%lang%")+7))) + s.substring(s.indexOf('%', s.indexOf("%lang%")+7)+1);
		return s;
	}
	
	/**
	 * Updates the file you want to use the Adapter with.
	 * 
	 * @param inputStream InputStream of the File with which you want to use the Adapter
	 * @return <b>true:</b> if updating was successful</br><b>false:</b> if an error occurs
	 */
	public boolean updatePath(String path) {
		this.path = path;
		try {
			if (Installer.isInstalled())
				properties.updateProperties(new FileInputStream(new File(path)), path);
			else
				properties.updateProperties(this.getClass().getResourceAsStream("/" + path), path);
			return true;
		} catch (IOException e) {
			String error = e.getMessage();
			if (!properties.checkStackTrace()) {
				Logger.getDefaultLogger().logError("Loading " + path + " failed!");
				error = Logger.getDefaultLogger().logException(e);
			}
			JOptionPane.showMessageDialog(null, "Error while loading " + path + "!\nTrying to continue!\n\nError:\n" + error, "Error", JOptionPane.ERROR_MESSAGE);
			return false;
		}
	}
	
	/**
	 * Returns the Path from which IniAdapter loads values.
	 * 
	 * @return the path from which IniAdapter loads values
	 */
	public String getPath() {
		return path;
	}
	
}
