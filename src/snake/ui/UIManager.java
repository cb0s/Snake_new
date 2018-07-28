package snake.ui;

import java.awt.Component;
import java.awt.Image;
import java.awt.Toolkit;

import javax.swing.JPanel;

import snake.SnakeGame;
import snake.ui.adapters.mainCloseAdapter;
import snake.ui.listeners.MouseListener;
import utils.Maths;
import utils.io.ImageLoader;
import utils.io.LangAdapter;
import utils.io.Logger;
import utils.ui.Button;

/** 
 * 	@author Cedric	
 *	@version 1.0
 *	@category ui
 */


// TODO: Write UI new
public class UIManager {
	
	private static Layout mainLayout;
	
	private static Button exit, minimize, maximize;
	
	private static Image backgroundImgMainMenu;
	
	// There should be no instance of UIManager
	private UIManager() {}
	
	public static void init() {
		backgroundImgMainMenu = ImageLoader.getImage(WindowAdapter.guiIniMainMenu.getString(SnakeGame.guiIniMainMenuPath, "backgroundImagePath"));
		
		WindowAdapter.initMain();
		WindowAdapter.mainAddWindowListener(new mainCloseAdapter());
		WindowAdapter.mainAddMouseListener(new MouseListener());
		WindowAdapter.mainAddMouseMotionListener(new MouseListener());
		WindowAdapter.setMainIcon(ImageLoader.getImage(WindowAdapter.guiIni.getString(SnakeGame.iniPath, "icon_path")));
		mainLayout = new Layout(WindowAdapter.mainGetDimensions());
		if (WindowAdapter.isMainFullscreen()) setUpFullscreenControl();
		setMainMenu();
		displayUpdatedLayout();
		GameClock.start();
		WindowAdapter.showMainWindow();
	}
	
	public static void displayUpdatedLayout() {
		JPanel panel = new JPanel();
		panel.setLayout(null);
		for (int i = 0; i < mainLayout.getLayerAmount(); i++) for (int j = 0; j < mainLayout.getLayer(i).length; j++) panel.add(mainLayout.getLayer(i)[j]);
		WindowAdapter.setMainPanel(panel);
	}
	
	public static void updateLayout() {
		mainLayout.updateDimension(WindowAdapter.mainGetDimensions());
	}
	
	public static void setUpFullscreenControl() {
		// TODO: Rewrite the Layout --> Button-Listener has to listen just for the Button on top!
		// TODO: Rewrite button resize --> if button hits an other one it mustn't get put below/on top an other one if Layout is the same
		Component[] controlButtonFullscreen_layer = new Component[3];
		exit = Button.loadButtonFromIni("data/ini/gui/gui.ini", "exit_button");
		exit.setLocation(WindowAdapter.mainGetDimensions().width-exit.getPreferredSize().width, 0);
		exit.setBounds(exit.getLocation().x, exit.getLocation().y, exit.getPreferredSize().width, exit.getPreferredSize().height);
		exit.addActionListener(e -> {
			Logger.getDefaultLogger().logInfo("Close Button has been pressed in full-screen");
			SnakeGame.stop();
		});
		exit.setResizeable(false);
		controlButtonFullscreen_layer[0] = exit;
		
		maximize = Button.loadButtonFromIni("data/ini/gui/gui.ini", "max_button");
		maximize.setLocation(WindowAdapter.mainGetDimensions().width-exit.getPreferredSize().width-maximize.getPreferredSize().width, 0);
		maximize.setBounds(maximize.getLocation().x, maximize.getLocation().y, maximize.getPreferredSize().width, maximize.getPreferredSize().height);
		maximize.addActionListener(e -> {
		if (WindowAdapter.isMainFullscreen()) WindowAdapter.mainSetToNormalScreen();
		else Logger.getDefaultLogger().logWarning("Maximize-Button was pressed although it should not be present!");
			mainLayout.remove(controlButtonFullscreen_layer);
			displayUpdatedLayout();
		});
		controlButtonFullscreen_layer[2] = maximize;
		
		minimize = Button.loadButtonFromIni("data/ini/gui/gui.ini", "min_button");
		minimize.setLocation(WindowAdapter.mainGetDimensions().width-exit.getPreferredSize().width-maximize.getPreferredSize().width-minimize.getPreferredSize().width, 0);
		minimize.setResizeable(false);
		minimize.setBounds(minimize.getLocation().x, minimize.getLocation().y, minimize.getPreferredSize().width, minimize.getPreferredSize().height);
		if(Boolean.parseBoolean(WindowAdapter.guiIni.getString(SnakeGame.iniPath, "min_button_border"))) minimize.setBorderWH((int) Maths.format(WindowAdapter.guiIni.getString(SnakeGame.iniPath, "min_button_borderW").replaceAll("%default%", ""+minimize.getBorderW()).replaceAll("%screensize%", ""+Toolkit.getDefaultToolkit().getScreenSize().width)), (int) Maths.format(WindowAdapter.guiIni.getString(SnakeGame.iniPath, "min_button_borderH").replaceAll("%default%", ""+minimize.getBorderH()).replaceAll("%screensize%", ""+Toolkit.getDefaultToolkit().getScreenSize().height)));
		else minimize.setBorderWH(0,0);
		minimize.addActionListener(e -> {
			WindowAdapter.mainMinimize();
		});
		controlButtonFullscreen_layer[1] = minimize;
		mainLayout.add(controlButtonFullscreen_layer);
	}
	
	public static void setMainMenu() {
		BackgroundImageLoader background = new BackgroundImageLoader(backgroundImgMainMenu);
		background.setPreferredSize(WindowAdapter.mainGetDimensions());
		background.setLocation(0,0);
		background.setBounds(background.getLocation().x, background.getLocation().y, background.getPreferredSize().width, background.getPreferredSize().width);
		Component[] backgroundLayer = {background};
//		mainLayout.add(backgroundLayer, 0);
		
		Button play_new = new Button(LangAdapter.getString("play-new_button"));
		play_new.addActionListener(e -> {
			Logger.getDefaultLogger().logInfo("Play-New-Game-Button was pressed");
			setGame();
		});
		
		Button load_game = new Button(LangAdapter.getString("load-game_button"));
		load_game.setLocation(0,0);
		load_game.addActionListener(e -> {System.out.println("load_game pressed");});
		load_game.setBounds(0, 0, 200, 200);
		
		Button test = new Button("test");
		test.setLocation(0, 0);
		test.addActionListener(e -> {System.out.println("test");});
		test.setBounds(10, 10, 200, 200);
		
		Component[] layer = {load_game};
		Component[] layer2 = {test};
		mainLayout.add(layer);
		mainLayout.add(layer2);
	}
	
	public static void setGame() {

	}
}
