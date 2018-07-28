package utils.io;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import javax.swing.JOptionPane;

/** 
 * 	@author Cedric	
 *	@version 1.0
 *	@category io
 **/

public class Installer {
	
	private static int progress;
	private static ArrayList<String> installationProcess = new ArrayList<String>();
	
	private final static boolean installed;
	static {
		installed = new File("data/installed.0").exists();
	}
	public static boolean isInstalled() {
		return installed;
	}
	public static int getProgress() {
		return progress;
	}
	
	private static void log(String s) {
		if (s.substring(0, 4).equals("INFO")) Logger.getDefaultLogger().logInfo(s.substring(4));
		else if (s.substring(0, 7).equals("WARNING")) Logger.getDefaultLogger().logWarning(s.substring(7));
		else if (s.substring(0, 5).equals("ERROR")) Logger.getDefaultLogger().logError(s.substring(5));
		installationProcess.add(Logger.getTime() + " " + s);
	}
	
	private enum LoggingType {
		INFO,
		WARNING,
		ERROR;
	}
	
	public static void install(boolean replace) {
		Thread install = new Thread(new Runnable() {

			@Override
			public void run() {
				
				ArrayList<String> filesInstalled = new ArrayList<String>();
				ArrayList<String> files = new ArrayList<String>();
				
				try {
					log(LoggingType.INFO + "Starting installation");
					if(installed) {
						log(LoggingType.WARNING + "Game is already installed!");
						if(!replace) {
							progress = 100;
							return;
						}
						log(LoggingType.WARNING + "Reinstalling...");
					}
					
					String jarPath = Installer.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath().replaceAll(File.pathSeparator, "/");
					
					log(LoggingType.INFO + "Loading installation script");	
					InputStream script = Installer.class.getResourceAsStream("/data/installer.script");
					String line = "";
					while(script.available() > 0) {
						char c = (char) script.read();
						if (c != '\n')
							line += c;
						else {
							if (line.charAt(0) != '#')
								files.add(line);
							line = "";
						}
					}
					script.close();
					log(LoggingType.INFO + "Script loaded. Installation will start now");
					
					progress = 0;
					
					JarFile jar = new JarFile(new File(jarPath));
					Enumeration<JarEntry> entries = jar.entries();
					while(entries.hasMoreElements()) {
						JarEntry entry = entries.nextElement();
						String fileName = entry.getName();
						boolean b = false;
						for(String s : files) {
							if (s.contains(fileName)) {
								b = true;
								break;
							}
						}
						if(!b) continue;
						File f = new File(fileName);
						if(fileName.endsWith("/")) {
							f.mkdirs();
							filesInstalled.add(f.getAbsolutePath());
							log(LoggingType.INFO + f.getAbsolutePath() + " created");
							progress = (int) filesInstalled.size()/files.size()*100;
							continue;
						}
						f.createNewFile();
						InputStream in = jar.getInputStream(entry);
						FileOutputStream out = new FileOutputStream(f);
						while(in.available() > 0) {
							out.write(in.read());
						}
						in.close();
						out.close();
						log(LoggingType.INFO + f.getAbsolutePath() + " installed");
						progress = (int) filesInstalled.size()/files.size()*100;
					}
					jar.close();
					log(LoggingType.INFO + "Successfully installed. Restart the game now");
					JOptionPane.showMessageDialog(null, "Successfully installed!\nRestart the game now to finish installation", "Installation completed", JOptionPane.INFORMATION_MESSAGE);
					PrintWriter w = new PrintWriter(new BufferedWriter(new FileWriter(new File("data/installed.0"))));
					w.println("Successfully installed, " + Logger.getTime());
					w.close();
				} catch (IOException | URISyntaxException e) {
					for(int i = filesInstalled.size()-1; i > -1; i--) {
						File delFile = new File(filesInstalled.get(i));
						if (delFile.exists()) delFile.delete();
						filesInstalled.remove(i);
						log(LoggingType.WARNING + delFile.getAbsolutePath() + " deleted");
					}
					log(LoggingType.ERROR + "Installing failed! Exiting installer...");
					String error = Logger.getDefaultLogger().logException(e);
					installationProcess.add("Error: " + error);
					JOptionPane.showMessageDialog(null, LangAdapter.getString("installer_failed").replace("%error%", error), LangAdapter.getString("installer_title_F"), JOptionPane.ERROR_MESSAGE);
				} finally {
					try {
						Logger.getDefaultLogger().logInfo("Trying to create installation-process file");
						File processFile = new File("installation.txt");
						if(processFile.exists()) processFile.delete();
						PrintWriter w = new PrintWriter(new BufferedWriter(new FileWriter(processFile)));
						for (String s : installationProcess) {
							w.println(s);
						}
						w.close();
						Logger.getDefaultLogger().logInfo("Installation-process file successfully created");
						JOptionPane.showMessageDialog(null, LangAdapter.getString("installer_procF_s"), LangAdapter.getString("installer_title"), JOptionPane.ERROR_MESSAGE);
					} catch(Exception e) {
						Logger.getDefaultLogger().logError("Couldn't create installation-process file");
						JOptionPane.showMessageDialog(null, LangAdapter.getString("installer_procF_f"), LangAdapter.getString("installer_title_F"), JOptionPane.ERROR_MESSAGE);
					}
				}
			}
		});
		install.start();
	}
}
