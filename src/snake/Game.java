package snake;

import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferStrategy;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.concurrent.CopyOnWriteArraySet;

import snake.ui.Display;
import snake.ui.GameState;
import snake.ui.MainMenuState;
import snake.ui.MenuOptionsState;
import snake.ui.entity.Entity;
import utils.io.Logger;
import utils.mechanics.Clock;
import utils.mechanics.Clockable;
import utils.ui.KeyManager;

/**
 * 
 * @author Cedric, Leo
 * @version 2.0
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
	private static final float DEFAULT_UPDATES_PER_SECOND = 1.0f;
	
	// **********
	// * Fields *
	// **********
	private Display display;
	
	private State gameState;
	private State mainMenu, menuOptions;
	
	private KeyManager keyManager;

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
		super(DEFAULT_UPDATES_PER_SECOND);
		display = new Display(title, width, height, new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				try {
					stop();
				} catch (InterruptedException e1) {
					Logger.gdL().logException(e1);
				}
				System.exit(0);
			}
		});
		Logger.gdL().log("Initializing Game-Loop");
		gameState = new GameState(this);
		mainMenu = new MainMenuState(this);
		menuOptions = new MenuOptionsState(this);
		
		Logger.gdL().logInfo("Setting State to Main-Menu");
		State.setState(mainMenu);

		Logger.gdL().logInfo("Setting up Key-Listener");
		keyManager = new KeyManager();
		keyManager.addKeyToListener(KeyEvent.VK_ESCAPE);
		keyManager.addKeyToListener(KeyEvent.VK_F1);
		Logger.gdL().log("Adding Key-Listener");
		display.addKeyListener(keyManager);
		

		entities = new CopyOnWriteArraySet<>();
		
		display.setVisible(true);
		display.start();
	}
	
	
	
	
	// *******************
	// * Private Methods *
	// *******************
	
	//TODO: implement game mechanics like creating entities and adding them to the display
	
	/*
	/**
	 * Updates all the variables and so on before rendering.
	 */
	/*private void update() {
		// Update variables
		width = display.getSize().width;
		height = display.getSize().height;
		
		if (State.getState() != null)
			State.getState().update();
	}*/
	
	
	
	
	// ******************
	// * Public Methods *
	// ******************

	@Override
	public void tick(long delta) {
		float tickVariance = ((float) delta) / 1000000000.0f;
		//example for letting entities wander 1 px right every second, accounting that the time between tick calls may vary.
		for(Entity e : entities) {
			e.setLocation((int)(e.getX() + (1.0f * tickVariance)), (int)e.getY());
		}
	}
	
}
