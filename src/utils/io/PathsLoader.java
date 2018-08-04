package utils.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javax.swing.JOptionPane;

/**
 * 
 * @author Cedric
 * @version 1.0
 * @category util</br></br>
 * 
 * Loads paths for the utils-classes.
 *
 */
public class PathsLoader {
	private static PropertiesAdapter prop;
	
	private static String path;
	
	static {
		path = "data/paths.prop";
		prop = new PropertiesAdapter();
		try {
			prop.updateProperties(Installer.isInstalled() ? new FileInputStream(new File('/' + path)) : PathsLoader.class.getClassLoader().getResourceAsStream(path), path);
		} catch (IOException e) {
			if (!Logger.isInitialized()) {
				e.printStackTrace();
				JOptionPane.showMessageDialog(null, "An Error occured!\n\n" + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
			} else {
				Logger.getDefaultLogger().logError("An error occured while trying to initialize PathLoader! Exiting...");
				String error = Logger.getDefaultLogger().logException(e);
				JOptionPane.showMessageDialog(null, "An Error occured!\n\n" + error, "Error", JOptionPane.ERROR_MESSAGE);
			}
			System.exit(1);
		}
	}
	
	public static void updateProperties(PropertiesAdapter prop, String path) {
		PathsLoader.prop = prop;
		PathsLoader.path = path;
	}
	
	public static String getPath() {
		return path;
	}
	
	public static String getSavedPath(String key) {
		System.out.println(key);
		return prop.getProperty(key);
	}
}
