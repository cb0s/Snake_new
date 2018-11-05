package snake.ui;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.Insets;
import java.awt.dnd.DragGestureRecognizer;
import java.awt.image.BufferStrategy;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;

import javax.swing.JFrame;

import snake.GameConfig;
import snake.io.Logger;
import snake.mechanics.Clock;
import snake.mechanics.Clockable;
import snake.ui.states.State;

/**
 * A display showing a {@link State} with either a fixed or unlimited framerate.
 * 
 * @author Cedric, Leo
 * @version 2.1
 * @category ui
 */
@SuppressWarnings({"serial","unused"})
public class Display extends JFrame {

	// **********
	// * Fields *
	// **********
	private Canvas canvas;
	private volatile State currentState;
	private final float renderPeriod;
	private final Clockable renderWorker;
	private Clock renderWorkerClock;
	private Logger renderLogger;




	// ****************
	// * Constructors *
	// ****************

	//Overriding inherited constructors
	private Display() {this(new GameConfig());}
	private Display(String title) {this(new GameConfig());}
	private Display(GraphicsConfiguration gc) {this(new GameConfig());}
	private Display(String title, GraphicsConfiguration gc) {this(new GameConfig());}



	//New constructors
	public Display(GameConfig config) {
		super(config.title);
		Logger.gdL().logInfo("Creating Display with title \"" + config.title + "\"");
		try {
			renderLogger = new Logger(new OutputStream[] {System.out, new FileOutputStream(config.renderLogfile)}, "[Render]");
		} catch (FileNotFoundException e) {
			Logger.gdL().logException(e);
			renderLogger = Logger.gdL();
		}

		setIconImage(config.image);
		
		//setUndecorated(true);
		canvas = new Canvas();
		canvas.setFocusable(false);
		//canvas.setBackground(Color.BLACK);

		setSize(config.width, config.height);
		setResizable(config.resizable);
		setUndecorated(config.undecorated);
		
		add(canvas);
		pack();
		center();

		renderLogger.logInfo("Creating BufferStrategy");
		canvas.createBufferStrategy(config.bufferSize);
		
		renderWorker = new RenderWorker();
		
		renderPeriod = 1.0f/config.fps;
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
			super.removeKeyListener(currentState);
			super.removeMouseListener(currentState);
			canvas.removeMouseListener(currentState);
			canvas.removeMouseMotionListener(currentState);
		}
		if(newState != null) {
			newState.onApply();
			super.addKeyListener(newState);
			super.addMouseListener(newState);
			canvas.addMouseListener(newState);
			canvas.addMouseMotionListener(newState);
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
			renderWorkerClock = new Clock(renderWorker, renderPeriod);
			renderWorker.tick(0);
			renderWorker.tick(0);
			renderLogger.logInfo("Making Display visible");
			super.setVisible(true);
			renderLogger.logInfo("Starting Render-Worker");
			renderWorkerClock.start();
		} else if(!b && isVisible()) {
			renderLogger.logInfo("Stopping Render-Worker");
			renderWorkerClock.shutdown();
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
		setCurrentState(null);
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
		renderWorkerClock.join();
	}




	// *****************
	// * Inner classes *
	// *****************
	public class RenderWorker implements Clockable {
		private BufferStrategy bs;
		private Graphics2D g;
		
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
	}

}
