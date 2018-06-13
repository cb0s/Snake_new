package snake.ui;

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

import snake.Error;
import snake.Maths;
import snake.SnakeGame;
import snake.io.IniAdapter;
import snake.io.LangAdapter;
import snake.io.Logger;

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
	public final static IniAdapter guiIni;
	public final static IniAdapter guiIniMainMenu;
	
	static {
		activeFrames = new HashSet<JFrame>();
		device = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
		guiIni = new IniAdapter();
		guiIniMainMenu = new IniAdapter();
	}
	
	private static boolean fullscreen;
	private static JFrame mainFrame;
	private JFrame frame;
	
	public WindowAdapter(String title, int width, int height) {
		SnakeGame.log(Logger.LoggingType.INFO.type + "Creating new frame " + frame.getTitle() + " (" + width + ", " + height + ")");
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
		SnakeGame.log(Logger.LoggingType.INFO.type + "Resizing frame " + frame.getTitle());
		frame.setSize(width, height);
	}
	public void setIcon(Image img) {
		frame.setIconImage(img);
	}
	public void setPanel(JPanel panel) {
		SnakeGame.log(Logger.LoggingType.INFO.type + "Setting new Panel for frame " + frame.getTitle());
		frame.getContentPane().add(panel);
	}
	public void setJMenuBar(JMenuBar menubar) {
		SnakeGame.log(Logger.LoggingType.INFO.type + "Setting JMenuBar for frame " + frame.getTitle());
		frame.setJMenuBar(menubar);
	}
	public void setTitle(String title) {
		SnakeGame.log(Logger.LoggingType.INFO.type + "Renaming frame " + frame.getTitle() + " to " + title);
		frame.setTitle(title);
	}
	public void showWindow() {
		SnakeGame.log(Logger.LoggingType.INFO.type + "Making frame " + frame.getTitle() + " visible");
		frame.setVisible(true);
	}
	public void hideWindow() {
		SnakeGame.log(Logger.LoggingType.INFO.type + "Making frame " + frame.getTitle() + " invisible");
		frame.setVisible(false);
	}
	public void destroy() {
		SnakeGame.log(Logger.LoggingType.INFO.type + "Destroying frame " + frame.getTitle());
		frame.dispose();
		activeFrames.remove(frame);
	}
	public void addWindowListener(WindowListener l) {
		SnakeGame.log(Logger.LoggingType.INFO.type + "Adding WindowListener for " + frame.getTitle());
		frame.addWindowListener(l);
	}
	public void addKeyListener(KeyListener l) {
		SnakeGame.log(Logger.LoggingType.INFO.type + "Adding KeyListener for frame " + frame.getTitle());
		frame.addKeyListener(l);
	}
	public void addMouseListener(MouseListener l) {
		SnakeGame.log(Logger.LoggingType.INFO.type + "Adding MouseListener for frame " + frame.getTitle());
		frame.addMouseListener(l);
	}
	public void addMouseMotionListener(MouseMotionListener l) {
		SnakeGame.log(Logger.LoggingType.INFO.type + "Adding MouseMotionListener for frame " + frame.getTitle());
		frame.addMouseMotionListener(l);
	}
	
	/**
	 * This Method initializes the MainFrame.
	 * All following static Methods are referencing to the MainFrame and cannot be used to control the instantiated Window.
	 * 
	 * @param null
	 */
	public static void initMain() {
		SnakeGame.log(Logger.LoggingType.INFO.type + "Creating Main-Frame");
		mainFrame = new JFrame(guiIniMainMenu.getString(SnakeGame.guiIniMainMenuPath, "title"), device.getDefaultConfiguration());
		
		SnakeGame.log(Logger.LoggingType.INFO.type + "Setting Main-Frame-Size");
		if((fullscreen = Boolean.parseBoolean(guiIni.getString(SnakeGame.iniPath, "fullscreen")))) mainSetToFullScreen();
		else mainFrame.setSize((int) Maths.format(guiIni.getString(SnakeGame.iniPath, "width").replaceAll("%screensize%", ""+Toolkit.getDefaultToolkit().getScreenSize().width)), (int) Maths.format(guiIni.getString(SnakeGame.iniPath, "height").replaceAll("%screensize%", ""+Toolkit.getDefaultToolkit().getScreenSize().height)));
		if (Boolean.parseBoolean(guiIni.getString(SnakeGame.iniPath, "maximized"))) mainFrame.setExtendedState(mainFrame.getExtendedState() | JFrame.MAXIMIZED_BOTH);
		
		SnakeGame.log(Logger.LoggingType.INFO.type + "Positioning Main-Frame");
		boolean center = Boolean.parseBoolean(guiIni.getString(SnakeGame.iniPath, "center"));
		if(center) mainFrame.setLocationRelativeTo(null);
		else mainFrame.setLocation((int) Maths.format(guiIni.getString(SnakeGame.iniPath, "x")), (int) Maths.format(guiIni.getString(SnakeGame.iniPath, "y")));
		mainFrame.setLayout(new BorderLayout());
	}
		
	public static void mainUpdate() {
		mainFrame.repaint();
	}
	public static void setMainIcon(Image img) {
		SnakeGame.log(Logger.LoggingType.INFO.type + "Setting Icon for Main-Frame");
		mainFrame.setIconImage(img);
	}
	public static void setMainMenuBar(JMenuBar menubar) {
		SnakeGame.log(Logger.LoggingType.INFO.type + "Setting MenuBar for Main-Frame");
		mainFrame.setJMenuBar(menubar);
	}
	public static void setMainPanel(JPanel mainMenu) {
		SnakeGame.log(Logger.LoggingType.INFO.type + "Adding new content to Main-Frame");
		mainFrame.add(mainMenu, BorderLayout.CENTER);
	}
	public static void showMainWindow() {
		SnakeGame.log(Logger.LoggingType.INFO.type + "Making Main-Frame visible");
		if (isMainFullscreen()) {
			mainUpdate();
		} else {
			mainFrame.setVisible(true);
		}
	}
	public static void hideMainWindow() {
		SnakeGame.log(Logger.LoggingType.INFO.type + "Making Main-Frame invisible");
		mainFrame.setVisible(false);
	}
	public static void mainDestroy() {
		SnakeGame.log(Logger.LoggingType.INFO.type + "Destroying Main-Frame");
		mainFrame.dispose();
		
		// This is to destroy all Frames manually and not by System.exit() with the possibility to control it
		try{
			JFrame[] frames = (JFrame[]) activeFrames.toArray();
			for (JFrame f : frames) {
				f.dispose();
				SnakeGame.log(Logger.LoggingType.INFO.type + "Destroying Frame " + f.getTitle());
				activeFrames.remove(f);
			}
		} catch(ClassCastException exception) {
			SnakeGame.log(Logger.LoggingType.WARNING.type + "No active Frames apart from Main-Frame found");
		}

	}
	public static void mainAddWindowListener(WindowListener l) {
		SnakeGame.log(Logger.LoggingType.INFO.type + "Adding WindowListener to Main-Frame");
		mainFrame.addWindowListener(l);
	}
	public static void mainAddKeyListener(KeyListener l) {
		SnakeGame.log(Logger.LoggingType.INFO.type + "Adding KeyListener to Main-Frame");
		mainFrame.addKeyListener(l);
	}
	public static void mainAddMouseListener(MouseListener l) {
		SnakeGame.log(Logger.LoggingType.INFO.type + "Adding MouseListener to Main-Frame");
		mainFrame.addMouseListener(l);
	}
	public static void mainAddMouseMotionListener(MouseMotionListener l) {
		SnakeGame.log(Logger.LoggingType.INFO.type + "Adding MouseMotionListener to Main-Frame");
		mainFrame.addMouseMotionListener(l);
	}
	public static void mainSetSize(int width, int height) {
		SnakeGame.log(Logger.LoggingType.INFO.type + "Resizing Main-Frame to " + width + "/" + height);
		mainFrame.setSize(width, height);
	}
	public static void mainMinimize() {
		SnakeGame.log(Logger.LoggingType.INFO.type + "Minimizing Main-Frame");
		mainFrame.setState(JFrame.ICONIFIED);
	}
	public static void mainSetToFullScreen() {
		SnakeGame.log(Logger.LoggingType.INFO.type + "Showing Main-Frame in fullscreen");
		try {
			mainFrame.dispose();
			mainFrame.setExtendedState(JFrame.MAXIMIZED_BOTH); 
			mainFrame.setUndecorated(true);
			mainFrame.setResizable(false);
			device.setFullScreenWindow(mainFrame);
		} catch(Exception exception) {
			SnakeGame.log(Logger.LoggingType.ERROR.type + "An error occured while trying to switch on Fullscreen-Mode");
			String error = Error.printError(exception);
			SnakeGame.log(Logger.LoggingType.ERROR.type + "Error:\n"+error);
			JOptionPane.showMessageDialog(null, LangAdapter.getString("ui_error_fullscreen_switchOn").replace("%error%", error), "ui_error_title", JOptionPane.ERROR_MESSAGE);
		}

	}
	public static void mainSetToNormalScreen() {
		SnakeGame.log(Logger.LoggingType.INFO.type + "Switching from Full-Screen to Normal-Screen");
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
			SnakeGame.log(Logger.LoggingType.ERROR.type + "An error occured while trying to switch off Fullscreen-Mode");
			String error = Error.printError(exception);
			SnakeGame.log(Logger.LoggingType.ERROR.type + "Error:\n"+error);
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
