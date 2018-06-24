package snake.io;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

import javax.swing.JOptionPane;

/** 
 * 	@author Cedric	
 *	@version 1.0
 *	@category io
 **/

public class IniAdapter {
	
	private Properties lastIni;
	private String lastIniPath;
	
	public IniAdapter() {
		lastIni = new Properties();
	}
	
	public String getString(String path, String key) {
		return getString(path, key, false);
	}
	
	public String getString(String path, String key, boolean suppressErrors) {
		/** Da normalerweise mehr als ein Attribut aus einem INI-File gezogen wird,
		 *  habe ich es hier so implementiert, dass auch mehr als ein Attribut herausgezogen
		 *	werden kann, ohne nocheinmal das File neuladen zu müssen -> hoffentlich schneller!
		 */
		boolean notFromLogger = true;
		
		for(StackTraceElement st : Thread.currentThread().getStackTrace())
			notFromLogger = (!st.getClassName().contains("snake.io.Logger") && notFromLogger);
		
		if(notFromLogger) {
			Logger.getDefaultLogger().logInfo("Loading " + key + " from " + path);
		}
		
		if(path != lastIniPath) {
			try {
				if (Installer.isInstalled())
					lastIni.load(new FileInputStream(new File(path)));
				else
					lastIni.load(this.getClass().getResourceAsStream("/" + path));
				lastIniPath = path;
			} catch (Exception exception) {
				// This Error can't be suppressed
				if(notFromLogger) {
					Logger.getDefaultLogger().logError("Could't load " + key + " from " + path);
					String error = Logger.getDefaultLogger().logException(exception);
					JOptionPane.showMessageDialog(null, "Error while loading " + key + " from " + path + "!\nTrying to continue!\n\nError:\n" + error, "Warning", JOptionPane.WARNING_MESSAGE);
				}
				return null;
			}
		}
		String result = lastIni.getProperty(key);
		try {
			if(notFromLogger) {
				while(result.contains("%lang%")) {
					result = result.replace("%lang", "");
					result = result.substring(0, result.indexOf("%")) + LangAdapter.getString(result.substring(result.indexOf("%")+1, result.indexOf("%", result.indexOf("%")+1))) + result.substring(result.indexOf("%", result.indexOf("%")+1)+1);
				}
			}
		} catch(Exception exception) {
			Logger.getDefaultLogger().logError("Could't load " + key + " from " + path);
			if (!suppressErrors) {
				String error = Logger.getDefaultLogger().logException(exception);
				JOptionPane.showMessageDialog(null, "Error while loading " + key + " from " + path + "!\nTrying to continue!\n\nError: " + error, "Error", JOptionPane.ERROR_MESSAGE);
			}
		}

		return result;
	}
	
}
