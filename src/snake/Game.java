package snake;

import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.image.BufferStrategy;
import java.io.File;

import javax.swing.JOptionPane;

import snake.ui.Display;
import snake.ui.GameState;
import snake.ui.MainMenuState;
import snake.ui.MenuOptionsState;
import utils.io.Logger;
import utils.ui.KeyManager;

/**
 * 
 * @author Cedric
 * @version 1.0
 * @category main</br></br>
 * 
 * This class lets you create a new Game.</br>
 * It already handles a lot by default so you don't need to do everything on your own.
 *
 */
public class Game implements Runnable {
	
	private boolean running;
	private int width, height;
	private int fpsCount=0, actFps=0;
	
	private Display display;
	private Thread thread;
	
	private BufferStrategy bs;
	private Graphics2D g;
	
	private State gameState;
	private State mainMenu, menuOptions;
	
	private KeyManager keyManager;
	
	private static Logger renderLogger;
	
	static {
		renderLogger = new Logger(Logger.fileLogging ? new File(Logger.ini.getString("path") + "render.log") : null);
	}
	
	// ****************
	// * Constructors *
	// ****************
	/**
	 * Creates a new Game.
	 * 
	 * @param title The title of the Game
	 * @param width The width of the Game
	 * @param height The height of the Game
	 * @param closeAdapter The WindowAdapter which will be called after closing
	 */
	public Game(String title, int width, int height, WindowAdapter closeAdapter) {
		display = new Display(title, width, height, closeAdapter);
		keyManager = new KeyManager();
	}
	
	// *******************
	// * Private Methods *
	// *******************
	/**
	 * Initializes everything before the Game-Loop starts.
	 */
	private void init() {
		renderLogger.logInfo("Initializing Game-Loop");
		renderLogger.logInfo("Creating BufferStrategy");
		display.getCanvas().createBufferStrategy(3);
		renderLogger.logInfo("Adding Key-Listener");
		display.getFrame().addKeyListener(keyManager);
		
		gameState = new GameState(this);
		mainMenu = new MainMenuState(this);
		menuOptions = new MenuOptionsState(this);
		
		renderLogger.logInfo("Setting State to Main-Menu");
		State.setState(mainMenu);

		renderLogger.logInfo("Setting up Key-Listener");
		keyManager.addKeyToListener(KeyEvent.VK_ESCAPE);
		keyManager.addKeyToListener(KeyEvent.VK_F1);
		// TODO make loading-Screen
	}
	
	/**
	 * Updates all the variables and so on before rendering.
	 */
	private void update() {
		// Update variables
		width = display.getSize().width;
		height = display.getSize().height;
		
		if (State.getState() != null)
			State.getState().update();
	}
	
	/**
	 * Renders everything to the screen.
	 */
	private void render() {
		bs = display.getCanvas().getBufferStrategy();
		
		g = (Graphics2D) bs.getDrawGraphics();
		g.clearRect(0, 0, width, height);

		// Start Draw

		if (State.getState() != null)
			State.getState().render(g);
		
		// End Draw
		
		bs.show();
		g.dispose();
		fpsCount++;
	}
	
	
	// ******************
	// * Public Methods *
	// ******************

	// * Thread-related stuff *
	/**
	 * Runs the Game-Loop.
	 */
	@Override
	public void run() {
		init();

		int fps = 120  ;
		double timePerUpdate = 1000000000 / fps;
		double delta = 0;
		long now;
		long lastTime = System.nanoTime();
		long timer = 0;
		
		display.setVisible(true);
				
		while (running) {
			now = System.nanoTime();
			delta += (now - lastTime) / timePerUpdate;
			timer += now - lastTime;
			lastTime = now;
			
			if (delta >= 1) {
				update();
				render();
				delta--;
			}
			
			if (timer >= 1000000000) {
				actFps = fpsCount;
				fpsCount = 0;
				timer = 0;
				renderLogger.logInfo("FPS | UPS: " + actFps);
			}

		}
	}

	/**
	 * Stops the Game.
	 */
	public synchronized void start() {
		// Safety //
		if (running) {
			Logger.getDefaultLogger().logError("You cannot start the Game-Loop more than once!");
			return;
		}
		
		renderLogger.logInfo("Starting Game-Loop");
		running = true;
		thread = new Thread(this);
		thread.start();
	}
	/**
	 * Starts the Game.
	 */
	public synchronized void stop() {
		// Safety //
		if (!running) {
			Logger.getDefaultLogger().logError("You cannot stop the Game-Loop if it is not running!");
			return;
		}
		
		renderLogger.logInfo("Stopping Game-Loop");
		running = false;
		try {
			thread.join();
		} catch (InterruptedException e) {
			renderLogger.logError("An Error occured while shutting down the Game-Loop!");
			renderLogger.logException(e);
			Logger.getDefaultLogger().logError("An Error occured while shutting down the Game-Loop! See the Render-Log for more information!");
			JOptionPane.showMessageDialog(null, "An Error occured while shutting down the Game-Loop.\n\nSee the Render-Log for more information!", "Error", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	// *********************
	// * Setters & Getters *
	// *********************
	/**
	 * Returns the Display of the game.
	 * 
	 * @return The Display of the game
	 */
	public Display getDisplay() {
		return display;
	}
	/**
	 * Returns whether the game is running or not.
	 * 
	 * @return <b>true:</b> if the game is running</br><b>false:</b> if the game is not running
	 */
	public boolean isRunning() {
		return running;
	}
	
	/**
	 * Returns the KeyManager used in the Game.
	 * 
	 * @return The KeyManager of the game
	 */
	public KeyManager getKeyManager() {
		return keyManager;
	}
	
	/**
	 * Returns the Render-Logger.
	 * 
	 * @return The Render-Logger
	 */
	public static Logger getRenderLogger() {
		return renderLogger;
	}
}
