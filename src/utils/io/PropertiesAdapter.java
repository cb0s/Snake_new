package utils.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * 
 * @author Cedric
 * @version 1.0
 * @category util</br></br>
 * 
 * This class is an Adapter for writing or reading Files with the Properties-Syntax.
 * 
 */
public class PropertiesAdapter {
	// ******************
	// * Private fields *
	// ******************
	private Properties properties = null;
	private String path;
	
	// ****************
	// * Constructors *
	// ****************
	/**
	 * Gets a new Object of Property-Adapter.</br>
	 * <i><b>Note:</b> Just other utils-Adapters can access this Constructor.</i>
	 */
	PropertiesAdapter() {}
	
	/**
	 * Get a new Object of Property-Adapter relating to a specified file.
	 * 
	 * @param file the File with which you want to use the Adapter
	 * @throws FileNotFoundException Gets thrown if file is not accessible or does not exist
	 * @throws IOException If an Error occurs while initializing either the Properties or the FileInputStream relating to it
	 */
	public PropertiesAdapter(File file) throws FileNotFoundException, IOException {
		updateProperties(new FileInputStream(file), file.getPath());
	}
	
	// ******************
	// * Public Methods *
	// ******************
	/**
	 * Returns a value stored in a properties-File linked to the given key.
	 * 
	 * @param key which the value you want is linked to
	 * @return The value which is linked to the given key in the properties-File
	 */
	public String getProperty(String key) {
		Logger.getDefaultLogger().logInfo("Trying to load " + key + " from " + path);
		return properties.getProperty(key);
	}
	
	/**
	 * Stores a new value linked to a key in a properties-File.
	 * 
	 * @param key the key with which you can find stored values in a properties-File
	 * @param value the value which will be written into the properties-File
	 */
	public void setProperty(String key, String value) {
		Logger.getDefaultLogger().logInfo("Trying to set " + key + " to " + value + " in " + path);
		properties.setProperty(key, value);
	}
	
	/**
	 * Updates the file you want to use the Adapter with.
	 * 
	 * @param inputStream InputStream of the File with which you want to use the Adapter
	 * @throws FileNotFoundException Gets thrown if file is not accessible or does not exist
	 * @throws IOException If an Error occurs while initializing either the Properties or the FileInputStream relating to it
	 */
	public void updateProperties(InputStream inputStream, String path) throws IOException {
		this.path = path;
		properties.load(inputStream);
	}
	
	public String getPath() {
		return path;
	}
}
