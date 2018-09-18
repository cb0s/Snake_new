package snake;

import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import snake.ui.Display;
import snake.ui.states.*;
import utils.io.Logger;
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
public class Game {

	// **********
	// * Fields *
	// **********
	private Display display;
	
	private GameState gameState;
	private MainMenuState mainMenu;
	private MenuOptionsState menuOptions;
	private LoadGameState loadGame;
	private GameStatePaused gamePausedMenu;

	private KeyManager keyManager;
	private MouseManager mouseListener;
	
	
	
	
	
	
	
	
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
	public Game(String title) {
		Logger.gdL().logInfo("Setting up Game");
		display = new Display(title);
		display.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				shutdown();
			}
		});
		gameState = new GameState(this);
		mainMenu = new MainMenuState(this);
		menuOptions = new MenuOptionsState(this);
		loadGame = new LoadGameState(this);
		// Create other States
		
		display.setCurrentState(mainMenu);

		keyManager = new KeyManager();
		keyManager.addKeyToListener(KeyEvent.VK_ESCAPE);
		keyManager.addKeyToListener(KeyEvent.VK_F1);
		display.addKeyListener(keyManager);
		
		mouseListener = new MouseManager();
		display.addMouseListener(mouseListener);

		display.setVisible(true);
		Logger.gdL().logInfo("Game successfully started");
	}
	
	
	
	
	
	
	
	
	// ******************
	// * Public Methods *
	// ******************
	public void shutdown() {
		try {
			display.shutdown();
			display.join();
			display.dispose();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		Logger.gdL().logInfo("Game stopped\n\n");
		System.exit(0);
	}
	
}
