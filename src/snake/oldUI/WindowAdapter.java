package snake.oldUI;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.WindowListener;
import java.util.HashSet;

import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import snake.SnakeGame;
import utils.Maths;
import utils.io.IniAdapter;
import utils.io.LangAdapter;
import utils.io.Logger;

/**
 * 
 * @author Cedric
 * @version 1.0
 * @category ui
 * 
 * This Class exists to make dealing with Windows easier.
 * With this you should be able to customize everything important but not have to many options to get confused with
 * 
 **/

public class WindowAdapter {
	
	private static HashSet<JFrame> activeFrames;
	private static GraphicsDevice device;
	public final static IniAdapter guiIni = null;
	public final static IniAdapter guiIniMainMenu = null;
	
	static {
		activeFrames = new HashSet<JFrame>();
		device = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
//		guiIni = new IniAdapter(SnakeGame.iniPath);
//		guiIniMainMenu = new IniAdapter(SnakeGame.guiIniMainMenuPath);
	}
	
	private static boolean fullscreen;
	private static JFrame mainFrame;
	private JFrame frame;
	
	public WindowAdapter(String title, int width, int height) {
		Logger.getDefaultLogger().logInfo("Creating new frame " + frame.getTitle() + " (" + width + ", " + height + ")");
		frame = new JFrame(title);
		frame.setSize(width, height);
		frame.setLocationRelativeTo(mainFrame);
		activeFrames.add(frame);
	}
	public static void updateAllFrames() {
		for(JFrame f : activeFrames) f.update(f.getGraphics());
	}
	public void update() {
		frame.update(frame.getGraphics());
	}
	public void setSize(int width, int height) {
		Logger.getDefaultLogger().logInfo("Resizing frame " + frame.getTitle());
		frame.setSize(width, height);
	}
	public void setIcon(Image img) {
		frame.setIconImage(img);
	}
	public void setPanel(JPanel panel) {
		Logger.getDefaultLogger().logInfo("Setting new Panel for frame " + frame.getTitle());
		frame.getContentPane().add(panel);
	}
	public void setJMenuBar(JMenuBar menubar) {
		Logger.getDefaultLogger().logInfo("Setting JMenuBar for frame " + frame.getTitle());
		frame.setJMenuBar(menubar);
	}
	public void setTitle(String title) {
		Logger.getDefaultLogger().logInfo("Renaming frame " + frame.getTitle() + " to " + title);
		frame.setTitle(title);
	}
	public void showWindow() {
		Logger.getDefaultLogger().logInfo("Making frame " + frame.getTitle() + " visible");
		frame.setVisible(true);
	}
	public void hideWindow() {
		Logger.getDefaultLogger().logInfo("Making frame " + frame.getTitle() + " invisible");
		frame.setVisible(false);
	}
	public void destroy() {
		Logger.getDefaultLogger().logInfo("Destroying frame " + frame.getTitle());
		frame.dispose();
		activeFrames.remove(frame);
	}
	public void addWindowListener(WindowListener l) {
		Logger.getDefaultLogger().logInfo("Adding WindowListener for " + frame.getTitle());
		frame.addWindowListener(l);
	}
	public void addKeyListener(KeyListener l) {
		Logger.getDefaultLogger().logInfo("Adding KeyListener for frame " + frame.getTitle());
		frame.addKeyListener(l);
	}
	public void addMouseListener(MouseListener l) {
		Logger.getDefaultLogger().logInfo("Adding MouseListener for frame " + frame.getTitle());
		frame.addMouseListener(l);
	}
	public void addMouseMotionListener(MouseMotionListener l) {
		Logger.getDefaultLogger().logInfo("Adding MouseMotionListener for frame " + frame.getTitle());
		frame.addMouseMotionListener(l);
	}
	
	/**
	 * This Method initializes the MainFrame.
	 * All following static Methods are referencing to the MainFrame and cannot be used to control the instantiated Window.
	 * 
	 * @param null
	 */
	public static void initMain() {
		Logger.getDefaultLogger().logInfo("Creating Main-Frame");
		mainFrame = new JFrame(guiIniMainMenu.getString("title"), device.getDefaultConfiguration());
		
		Logger.getDefaultLogger().logInfo("Setting Main-Frame-Size");
		if((fullscreen = Boolean.parseBoolean(guiIni.getString("fullscreen")))) mainSetToFullScreen();
		else mainFrame.setSize((int) Maths.format(guiIni.getString("width").replaceAll("%screensize%", ""+Toolkit.getDefaultToolkit().getScreenSize().width)), (int) Maths.format(guiIni.getString("height").replaceAll("%screensize%", ""+Toolkit.getDefaultToolkit().getScreenSize().height)));
		if (Boolean.parseBoolean(guiIni.getString("maximized"))) mainFrame.setExtendedState(mainFrame.getExtendedState() | JFrame.MAXIMIZED_BOTH);
		
		Logger.getDefaultLogger().logInfo("Positioning Main-Frame");
		boolean center = Boolean.parseBoolean(guiIni.getString("center"));
		if(center) mainFrame.setLocationRelativeTo(null);
		else mainFrame.setLocation((int) Maths.format(guiIni.getString("x")), (int) Maths.format(guiIni.getString("y")));
		mainFrame.setLayout(new BorderLayout());
	}
		
	public static void mainUpdate() {
		mainFrame.repaint();
	}
	public static void setMainIcon(Image img) {
		Logger.getDefaultLogger().logInfo("Setting Icon for Main-Frame");
		mainFrame.setIconImage(img);
	}
	public static void setMainMenuBar(JMenuBar menubar) {
		Logger.getDefaultLogger().logInfo("Setting MenuBar for Main-Frame");
		mainFrame.setJMenuBar(menubar);
	}
	public static void setMainPanel(JPanel mainMenu) {
		Logger.getDefaultLogger().logInfo("Adding new content to Main-Frame");
		mainFrame.add(mainMenu, BorderLayout.CENTER);
	}
	public static void showMainWindow() {
		Logger.getDefaultLogger().logInfo("Making Main-Frame visible");
		if (isMainFullscreen()) {
			mainUpdate();
		} else {
			mainFrame.setVisible(true);
		}
	}
	public static void hideMainWindow() {
		Logger.getDefaultLogger().logInfo("Making Main-Frame invisible");
		mainFrame.setVisible(false);
	}
	public static void mainDestroy() {
		Logger.getDefaultLogger().logInfo("Destroying Main-Frame");
		mainFrame.dispose();
		
		// This is to destroy all Frames manually and not by System.exit() with the possibility to control it
		try{
			JFrame[] frames = (JFrame[]) activeFrames.toArray();
			for (JFrame f : frames) {
				f.dispose();
				Logger.getDefaultLogger().logInfo("Destroying Frame " + f.getTitle());
				activeFrames.remove(f);
			}
		} catch(ClassCastException exception) {
			Logger.getDefaultLogger().logWarning("No active Frames apart from Main-Frame found");
		}

	}
	public static void mainAddWindowListener(WindowListener l) {
		Logger.getDefaultLogger().logInfo("Adding WindowListener to Main-Frame");
		mainFrame.addWindowListener(l);
	}
	public static void mainAddKeyListener(KeyListener l) {
		Logger.getDefaultLogger().logInfo("Adding KeyListener to Main-Frame");
		mainFrame.addKeyListener(l);
	}
	public static void mainAddMouseListener(MouseListener l) {
		Logger.getDefaultLogger().logInfo("Adding MouseListener to Main-Frame");
		mainFrame.addMouseListener(l);
	}
	public static void mainAddMouseMotionListener(MouseMotionListener l) {
		Logger.getDefaultLogger().logInfo("Adding MouseMotionListener to Main-Frame");
		mainFrame.addMouseMotionListener(l);
	}
	public static void mainSetSize(int width, int height) {
		Logger.getDefaultLogger().logInfo("Resizing Main-Frame to " + width + "/" + height);
		mainFrame.setSize(width, height);
	}
	public static void mainMinimize() {
		Logger.getDefaultLogger().logInfo("Minimizing Main-Frame");
		mainFrame.setState(JFrame.ICONIFIED);
	}
	public static void mainSetToFullScreen() {
		Logger.getDefaultLogger().logInfo("Showing Main-Frame in fullscreen");
		try {
			mainFrame.dispose();
			mainFrame.setExtendedState(JFrame.MAXIMIZED_BOTH); 
			mainFrame.setUndecorated(true);
			mainFrame.setResizable(false);
			device.setFullScreenWindow(mainFrame);
		} catch(Exception exception) {
			Logger.getDefaultLogger().logError("An error occured while trying to switch on Fullscreen-Mode");
			String error = Logger.getDefaultLogger().logException(exception);
			Logger.getDefaultLogger().logError("Error:\n"+error);
			JOptionPane.showMessageDialog(null, LangAdapter.getString("ui_error_fullscreen_switchOn").replace("%error%", error), "ui_error_title", JOptionPane.ERROR_MESSAGE);
		}

	}
	public static void mainSetToNormalScreen() {
		Logger.getDefaultLogger().logInfo("Switching from Full-Screen to Normal-Screen");
		try {
			mainFrame.dispose();
			mainFrame.setResizable(true);
			mainFrame.setUndecorated(false);
			device.setFullScreenWindow(null);
			mainFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
			mainFrame.setLocationRelativeTo(null);
			mainFrame.setVisible(true);
			fullscreen = false;
		} catch (Exception exception) {
			Logger.getDefaultLogger().logError("An error occured while trying to switch off Fullscreen-Mode");
			String error = Logger.getDefaultLogger().logException(exception);
			Logger.getDefaultLogger().logError("Error:\n"+error);
			JOptionPane.showMessageDialog(null, LangAdapter.getString("ui_error_fullscreen_switchOff").replace("%error%", error), "ui_error_title", JOptionPane.ERROR_MESSAGE);
		}
	}
	public static Dimension mainGetDimensions() {
		return mainFrame.getSize();
	}
	public static Point mainGetLocation() {
		return mainFrame.getLocationOnScreen();
	}
	public static boolean isVisible() {
		return mainFrame.isVisible();
	}
	public static boolean isMainFullscreen() {
		return fullscreen;
	}
}
