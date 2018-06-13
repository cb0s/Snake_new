package snake.io;

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

import snake.Error;
import snake.SnakeGame;

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
		SnakeGame.log(s);
		installationProcess.add(Logger.getTime() + " " + s);
	}
	
	public static void install(boolean replace) {
		Thread install = new Thread(new Runnable() {

			@Override
			public void run() {
				
				ArrayList<String> filesInstalled = new ArrayList<String>();
				ArrayList<String> files = new ArrayList<String>();
				
				try {
					log(Logger.LoggingType.INFO.type + "Starting installation");
					if(installed) {
						log(Logger.LoggingType.WARNING.type + "Game is already installed!");
						if(!replace) {
							progress = 100;
							return;
						}
						log(Logger.LoggingType.WARNING.type + "Reinstalling...");
					}
					
					String jarPath = Installer.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath().replaceAll(File.pathSeparator, "/");
					
					log(Logger.LoggingType.INFO.type + "Loading installation script");	
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
					log(Logger.LoggingType.INFO.type + "Script loaded. Installation will start now");
					
					progress = (int) 100 / files.size();
					
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
							log(Logger.LoggingType.INFO.type + f.getAbsolutePath() + " created");
							progress = (int) (100 / files.size())*filesInstalled.size();
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
						log(Logger.LoggingType.INFO.type + f.getAbsolutePath() + " installed");
						progress = (int) (100 / files.size())*filesInstalled.size();
					}
					jar.close();
					log(Logger.LoggingType.INFO.type + "Successfully installed. Restart the game now");
					JOptionPane.showMessageDialog(null, "Successfully installed!\nRestart the game now to finish installation", "Installation completed", JOptionPane.INFORMATION_MESSAGE);
					PrintWriter w = new PrintWriter(new BufferedWriter(new FileWriter(new File("data/installed.0"))));
					w.println("Successfully installed, " + Logger.getTime());
					w.close();
				} catch (IOException | URISyntaxException e) {
					for(int i = filesInstalled.size()-1; i > -1; i--) {
						File delFile = new File(filesInstalled.get(i));
						if (delFile.exists()) delFile.delete();
						filesInstalled.remove(i);
						log(Logger.LoggingType.WARNING.type + delFile.getAbsolutePath() + " deleted");
					}
					log(Logger.LoggingType.ERROR.type + "Installing failed! Exiting installer...");
					String error = Error.printError(e);
					installationProcess.add("Error: " + error);
					JOptionPane.showMessageDialog(null, LangAdapter.getString("installer_failed").replace("%error%", error), LangAdapter.getString("installer_title_F"), JOptionPane.ERROR_MESSAGE);
				} finally {
					try {
						SnakeGame.log(Logger.LoggingType.INFO.type + "Trying to create installation-process file");
						File processFile = new File("installation.txt");
						if(processFile.exists()) processFile.delete();
						PrintWriter w = new PrintWriter(new BufferedWriter(new FileWriter(processFile)));
						for (String s : installationProcess) {
							w.println(s);
						}
						w.close();
						SnakeGame.log(Logger.LoggingType.INFO.type + "Installation-process file successfully created");
						JOptionPane.showMessageDialog(null, LangAdapter.getString("installer_procF_s"), LangAdapter.getString("installer_title"), JOptionPane.ERROR_MESSAGE);
					} catch(Exception e) {
						SnakeGame.log(Logger.LoggingType.ERROR.type + "Couldn't create installation-process file");
						JOptionPane.showMessageDialog(null, LangAdapter.getString("installer_procF_f"), LangAdapter.getString("installer_title_F"), JOptionPane.ERROR_MESSAGE);
					}
				}
			}
		});
		install.start();
	}
}
