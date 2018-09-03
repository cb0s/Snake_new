package snake;

import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.concurrent.CopyOnWriteArraySet;

import snake.ui.Display;
import snake.ui.GameState;
import snake.ui.LoadGameState;
import snake.ui.MainMenuState;
import snake.ui.MenuOptionsState;
import snake.ui.entity.Entity;
import utils.io.Logger;
import utils.mechanics.Clock;
import utils.ui.KeyManager;
import utils.ui.MouseManager;

/**
 * 
 * @author Cedric, Leo
 * @version 2.1
 * @category main</br></br>
 * 
 * This class lets you create a new Game.</br>
 * It already handles a lot by default so you don't need to do everything on your own.
 *
 */
public class Game extends Clock {

	// *************
	// * Constants *
	// *************
	/**
	 * The rate, at which the elements in the game change their position or state.
	 */
	private static final float DEFAULT_TICKS_PER_SECOND = 1.0f;
	
	// **********
	// * Fields *
	// **********
	private Display display;
	
	private State gameState;
	private State mainMenu, menuOptions, loadGame;
	private State gamePausedMenu;

	private KeyManager keyManager;
	private MouseManager mouseListener;
	
	private CopyOnWriteArraySet<Entity> entities;

	
	// ****************
	// * Constructors *
	// ****************
	/**
	 * Creates a new Game.
	 * 
	 * @param title The title of the Game
	 * @param width The width of the Game
	 * @param height The height of the Game
	 */
	public Game(String title, int width, int height) {
		super(DEFAULT_TICKS_PER_SECOND);
		Logger.gdL().logInfo("Setting up Game");
		display = new Display(title, width, height, new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				stopGame();
			}
		});
		Display.getRenderLogger().logInfo("Initializing Game-Loop");
		Display.getRenderLogger().logInfo("Creating States");
		gameState = new GameState(this);
		mainMenu = new MainMenuState(this);
		menuOptions = new MenuOptionsState(this);
		loadGame = new LoadGameState(this);
		// Create other States
		
		State.setState(mainMenu);

		Display.getRenderLogger().logInfo("Setting up Key-Listener");
		keyManager = new KeyManager();
		keyManager.addKeyToListener(KeyEvent.VK_ESCAPE);
		keyManager.addKeyToListener(KeyEvent.VK_F1);
		Display.getRenderLogger().logInfo("Adding Key-Listener");
		display.addKeyListener(keyManager);
		
		mouseListener = new MouseManager();
		Display.getRenderLogger().logInfo("Adding Mouse-Listener");
		display.addMouseListener(mouseListener);

		entities = new CopyOnWriteArraySet<>();
		
		display.setVisible(true);
		display.start();
		Logger.gdL().logInfo("Game successfully started");
	}
	
	
	
	// *******************
	// * Private Methods *
	// *******************
	
	
	// ******************
	// * Public Methods *
	// ******************

	public void stopGame() {
		Display.getRenderLogger().logInfo("Disposing Display");
		getDisplay().dispose();
		try {
			shutdownAll();
			Logger.stopAll();
		} catch (InterruptedException e1) {
			Logger.gdL().logException(e1);
		}
		Logger.gdL().logInfo("Game stopped\n\n");
		System.exit(0);
	}
	
	public State getGameState() {
		return gameState;
	}

	public State getMainMenu() {
		return mainMenu;
	}

	public State getMenuOptions() {
		return menuOptions;
	}

	public State getLoadGame() {
		return loadGame;
	}

	public State getGamePausedMenu() {
		return gamePausedMenu;
	}

	public Display getDisplay() {
		return display;
	}
	
	@Override
	public void tick(long delta) {
//		float tickVariance = ((float) delta) / 1000000000.0f;
		//example for letting entities wander 1 px right every second, accounting that the time between tick calls may vary.
		for(Entity e : entities) {
			e.update();
//			e.setLocation((int)(e.getX() + (1.0f * tickVariance)), (int)e.getY());
		}
	}
	
}
