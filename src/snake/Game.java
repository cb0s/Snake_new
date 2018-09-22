package snake;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import snake.io.Logger;
import snake.ui.DialogManager;
import snake.ui.Display;
import snake.ui.states.*;

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
	private GameConfig config;
	
	private Display display;
	
	private LoadGameState loadGame;
	private MainMenuState mainMenu;
	private MenuOptionsState menuOptions;
	private GameState gameState;
	private GameStatePaused gamePausedMenu;

	
	
	
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
	public Game(GameConfig config) {
		Logger.gdL().logInfo("Setting up Game");
		this.config = config;
		display = new Display(config);
		display.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				shutdown();
			}
		});
		loadGame = new LoadGameState(this);
		display.setCurrentState(loadGame);
		display.setVisible(true);
		
		mainMenu = new MainMenuState(this);
		mainMenu.prepare();
		menuOptions = new MenuOptionsState(this);
		menuOptions.prepare();
		gameState = new GameState(this);
		//prepare game state when play was clicked
		gamePausedMenu = new GameStatePaused(this);
		gamePausedMenu.prepare();
		
		//Simulate loading time
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		
		display.setCurrentState(mainMenu);
		Logger.gdL().logInfo("Game successfully started");
	}
	
	
	
	
	// ******************
	// * Public Methods *
	// ******************
	public GameConfig getConfig() {
		return config;
	}
	
	public void showMainMenu() {
		Logger.gdL().logInfo("Returning to main menu");
		display.setCurrentState(mainMenu);
	}
	
	public void startNewGame() {
		Logger.gdL().logInfo("Starting new game");
		display.setCurrentState(loadGame);
		gameState.prepare();
		//Simulate loading time
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		display.setCurrentState(gameState);
		Logger.gdL().logInfo("New game started");
	}
	
	public void openPauseMenu() {
		Logger.gdL().logInfo("Pausing game and opening pause menu");
		display.setCurrentState(gamePausedMenu);
	}
	
	public void closePauseMenu() {
		Logger.gdL().logInfo("Closing pause menu and resuming game");
		display.setCurrentState(gameState);
	}
	
	public void showOptions() {
		Logger.gdL().logInfo("Opening options menu");
		display.setCurrentState(menuOptions);
	}
	
	public void shutdown() {
		try {
			display.shutdown();
			display.join();
			display.dispose();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		Logger.gdL().logInfo("Game stopped");
		Logger.gdL().shutdown();
		try {
			Logger.gdL().join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		//System.exit(0);
	}
	
	
	

	
	
	
	
	/**
	 * This method starts the program with the arguments args.
	 * 
	 * @param args Arguments when for starting program
	 */
	public static void main(String[] args) {
		try {
			Logger.getDefaultLogger().logInfo("Starting Snake");
			
			GameConfig config = new GameConfig();
			
			config.title = "Snake";
			
			new Game(config);
		
		} catch (Error | Exception e) {
			try {
				Logger.gdL().logError("A fatal error occured while running Snake! Error could not be identified!\nError:");
				Logger.gdL().logException(e);
				DialogManager.showExeptionDialog(null, e, "A fatal error occured while running Snake! Error could not be identified!\n\nError:\n%exception%\n\nExiting", "Fatal Error!", true);
			} catch(Exception e2) {
				System.err.println("FATAL ERROR! THIS COULD NOT BE LOGGED!");
				e.printStackTrace();
				System.err.println("LOGGING ERROR:");
				e2.printStackTrace();
			}
			System.exit(2);
		}	
	}
	
}
