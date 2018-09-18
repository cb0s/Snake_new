package snake.ui;

import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.WindowAdapter;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.concurrent.CopyOnWriteArraySet;

import javax.swing.JFrame;

import snake.ui.states.State;
import utils.io.Logger;
import utils.mechanics.Clock;
import utils.mechanics.Clockable;
import utils.ui.MouseManager;

/**
 * A display showing a {@link State} with either a fixed or unlimited framerate.
 * 
 * @author Cedric, Leo
 * @version 2.1
 * @category ui
 */
@SuppressWarnings({"serial","unused"})
public class Display extends JFrame {

	// *************
	// * Constants *
	// *************
	private static final float DEFAULT_FPS = 60.0f;
	private static final int DEFAULT_WIDTH = 1280;
	private static final int DEFAULT_HEIGHT = 720;
	
	private static final int DEFAULT_BUFFER_SIZE = 3;
	private static final String RENDER_LOGGER_OUTPUT_FILE = "render.log";








	// ******************
	// * Private Fields *
	// ******************
	private Canvas canvas;
	private volatile State currentState;
	private final float renderPeriod;
	private Clock renderWorker;
	private Logger renderLogger;








	// ****************
	// * Constructors *
	// ****************

	//Overriding inherited constructors
	private Display(GraphicsConfiguration gc) {this();}
	private Display(String title, GraphicsConfiguration gc) {this();}


	public Display() {
		this("", DEFAULT_WIDTH, DEFAULT_HEIGHT, DEFAULT_FPS);
	}

	public Display(String title) {
		this(title, DEFAULT_WIDTH, DEFAULT_HEIGHT, DEFAULT_FPS);
	}


	//New constructors
	public Display(String title, int width, int height) {
		this(title, width, height, DEFAULT_FPS);
	}

	public Display(String title, int width, int height, float fps) {
		super(title);
		Logger.gdL().logInfo("Creating Display with title \"" + title + "\"");
		try {
			renderLogger = new Logger(new OutputStream[] {System.out, new FileOutputStream(RENDER_LOGGER_OUTPUT_FILE)}, "[Render]");
		} catch (FileNotFoundException e) {
			Logger.gdL().logException(e);
			renderLogger = Logger.gdL();
		}

		canvas = new Canvas();
		canvas.setFocusable(false);

		setSize(width, height);
		setResizable(false);

		add(canvas);
		center();

		renderLogger.logInfo("Creating BufferStrategy");
		canvas.createBufferStrategy(DEFAULT_BUFFER_SIZE);

		renderPeriod = 1.0f/fps;
	}








	// ******************
	// * Public Methods *
	// ******************
	/**
	 * Centers this Display on the screen.
	 */
	public void center() {
		setLocationRelativeTo(null);
	}

	/**
	 * Adds a {@link MouseManager} (a combination of {@link MouseListener} and {@link MouseMotionListener}) to this Display.
	 * 
	 * @param manager the MouseManager to add
	 */
	public void addMouseManager(MouseManager manager) {
		renderLogger.logInfo("Adding Mouse-Manager");
		super.addMouseListener(manager);
		canvas.addMouseListener(manager);
		canvas.addMouseMotionListener(manager);
	}

	@Override
	public synchronized void setSize(int width, int height) {
		super.setSize(width, height);

		Dimension d = new Dimension(width, height);
		canvas.setPreferredSize(d);
		canvas.setMaximumSize(d);
		canvas.setMinimumSize(d);
	}

	/**
	 * Sets the State currently shown by this Display.
	 * 
	 * @param newState the new State to show
	 */
	public void setCurrentState(State newState) {
		if(currentState != null) {
			currentState.onDetach();
		}
		if(newState != null) {
			newState.onApply();
		}
		currentState = newState;
	}

	/**
	 * Returns the State currently shown by this Display.
	 * 
	 * @return the currently shown State
	 */
	public State getCurrentState() {
		return currentState;
	}

	/**
	 * Shows or hides this {@code Display} depending on the value of parameter
	 * {@code b}.<br>
	 * Rendering only happens while the display is visible.
	 * 
	 * @param b  if {@code true}, makes the {@code Window} visible,
	 * otherwise hides the {@code Window}.
	 * If the {@code Window} and/or its owner
	 * are not yet displayable, both are made displayable.  The
	 * {@code Window} will be validated prior to being made visible.
	 * If the {@code Window} is already visible, this will bring the
	 * {@code Window} to the front.<p>
	 * If {@code false}, hides this {@code Window}, its subcomponents, and all
	 * of its owned children.
	 * The {@code Window} and its subcomponents can be made visible again
	 * with a call to {@code #setVisible(true)}.
	 */
	@Override
	public synchronized void setVisible(boolean b) {
		if(b && !isVisible()) {
			renderLogger.logInfo("Initializing Render-Worker");
			renderWorker = new Clock(new Clockable() {
				BufferStrategy bs;
				Graphics2D g;
				@Override
				public void tick(float delta) {
					//Preparation
					bs = canvas.getBufferStrategy();
					g = (Graphics2D) bs.getDrawGraphics();

					//Clear canvas
					g.clearRect(0, 0, getSize().width, getSize().height);

					//Draw current State on canvas
					if(currentState != null) {
						currentState.render(g);
					}

					//Cleanup
					bs.show();
					g.dispose();
				}
			}, renderPeriod);
			renderLogger.logInfo("Making Display visible");
			super.setVisible(true);
			renderLogger.logInfo("Starting Render-Worker");
			renderWorker.start();
		} else if(!b && isVisible()) {
			renderLogger.logInfo("Stopping Render-Worker");
			renderWorker.shutdown();
			renderLogger.logInfo("Making Display invisible");
			super.setVisible(false);
		}
	}

	/**
	 * Shuts down this Display by making it invisible and stopping the rendering and render-logger.<br>
	 * Calling this on a stopped display has no effect.
	 */
	public synchronized void shutdown() {
		//Only shut down render-logger if it isn't the default logger
		if(renderLogger != Logger.gdL()) {
			renderLogger.shutdown();
		}
		setVisible(false);
	}

	/**
	 * Waits for this display's components to stop.
	 * 
	 * @throws  InterruptedException
	 *          if any thread has interrupted the current thread. The
	 *          <i>interrupted status</i> of the current thread is
	 *          cleared when this exception is thrown.
	 */
	public synchronized void join() throws InterruptedException {
		if(!renderLogger.isRunning()) {
			renderLogger.join();
		}
		renderWorker.join();
	}

}
