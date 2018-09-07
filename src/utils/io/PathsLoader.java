package utils.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import utils.ui.DialogManager;

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
			Logger.gdL().logError("An error occured while trying to initialize PathLoader! Exiting...");
			Logger.gdL().logException(e);
			DialogManager.showExeptionDialog(null, e, "An Error occured!\n\n%exception%", "IO-Error", true);
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
		return prop.getProperty(key);
	}
}
