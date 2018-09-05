package snake.ui;

import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.concurrent.CopyOnWriteArraySet;

import javax.swing.JFrame;

import snake.State;
import snake.ui.entity.Entity;
import snake.ui.tiles.Tile;
import utils.io.Logger;
import utils.mechanics.Clock;
import utils.ui.MouseManager;

/**
 * A display showing DisplayElements.
 * 
 * @author Cedric, Leo
 * @version 2.1
 * @category ui
 */
public class Display extends Clock {

	// *************
	// * Constants *
	// *************
	private static final float DEFAULT_FPS = 60.0f;
	private CopyOnWriteArraySet<Entity> entities;
	private CopyOnWriteArraySet<Tile> tiles; 
	
	

	// ******************
	// * Private Fields *
	// ******************
	// Window-Related stuff
	private JFrame frame;
	private Canvas canvas;
	
	//stored locally for performance reasons
	private Dimension size;

	//Render-related stuff
	private BufferStrategy bs;
	private Graphics2D g;
	private BufferedImage nextImage;
	/**
	 * To maintain constant fps
	 */
	private boolean nextImageRendered;

	private static Logger renderLogger;
	
	
	
	
	// ****************
	// * Constructors *
	// ****************
	public Display(String title, int width, int height) {
		this(title, width, height, DEFAULT_FPS, null);
	}

	public Display(String title, int width, int height, float fps) {
		this(title, width, height, fps, null);
	}

	public Display(String title, int width, int height, WindowAdapter closeAdapter) {
		this(title, width, height, DEFAULT_FPS, closeAdapter);
	}
	
	public Display(String title, int width, int height, float fps, WindowAdapter closeAdapter) {
		super(fps);
		Logger.gdL().logInfo("Creating Display " + title);
		try {
			renderLogger = new Logger(new OutputStream[] {System.out, new FileOutputStream("render.log")}, "[Render]");
		} catch (FileNotFoundException e) {
			Logger.gdL().logException(e);
			renderLogger = Logger.gdL();
		}
		frame = new JFrame(title);
		
		canvas = new Canvas();
		canvas.setFocusable(false);
		
		this.size = new Dimension();
		setSize(width, height);
		if (closeAdapter == null) frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		else frame.addWindowListener(closeAdapter);
		frame.setResizable(false);
		
		frame.add(canvas);
		frame.pack();
		center();

		renderLogger.logInfo("Creating BufferStrategy");
		canvas.createBufferStrategy(Integer.parseInt(MainMenuState.mainMenuIni.getString("buffering")));
		
		tiles = new CopyOnWriteArraySet<>();
		entities = new CopyOnWriteArraySet<>();
	}
	
	
	
	
	// *******************
	// * Private Methods *
	// *******************
	
	
	
	
	// ******************
	// * Public Methods *
	// ******************
	public void center() {
		frame.setLocationRelativeTo(null);
	}
	
	public void addKeyListener(KeyListener l) {
		frame.addKeyListener(l);
	}
	
	public void addMouseListener(MouseManager l) {
		canvas.addMouseListener(l);
		canvas.addMouseMotionListener(l);
	}
	
	// Setters
	public void setResizeable(boolean resizable) {
		frame.setResizable(resizable);
	}
	
	public void setLocation(int x, int y) {
		frame.setLocation(x, y);
	}
	
	public synchronized void setSize(int width, int height) {
		this.size.setSize(width, height);
		
		frame.setSize(width, height);
		
		Dimension d = new Dimension(width, height);
		canvas.setPreferredSize(d);
		canvas.setMaximumSize(d);
		canvas.setMinimumSize(d);
	}
	
	public void setTitle(String title) {
		frame.setTitle(title);
	}
	
	public void setVisible(boolean b) {
		frame.setVisible(b);
	}
	
	public void dispose() {
		frame.dispose();
	}
	
	public void setIconImage(Image image) {
		frame.setIconImage(image);
	}
	
	// Getters
	public boolean isResizable() {
		return frame.isResizable();
	}
	
	public Point getLocation() {
		return frame.getLocation();
	}
	
	public synchronized Dimension getSize() {
		return this.size.getSize();
	}
	
	public String getTitle() {
		return frame.getTitle();
	}
	
	public boolean isVisible() {
		return frame.isVisible();
	}
	
	public static Logger getRenderLogger() {
		return renderLogger;
	}
	
	public int formatRatioX(float ratioX) {
		return (int) (size.width * ratioX);
	}
	
	public int formatRatioY(float ratioY) {
		return (int) (size.height * ratioY);
	}
	
	
	//Render-related stuff
	public void addDisplayElement(DisplayElement displayElement) {
		if (displayElement instanceof Entity) entities.add((Entity) displayElement);
		else tiles.add((Tile) displayElement);
		System.out.println(tiles.toArray()[tiles.size()-1]);
	}
	
	public boolean removeDisplayElement(DisplayElement displayElement) {
		if (displayElement instanceof Entity) return entities.remove((Entity) displayElement);
		else return tiles.remove((Tile) displayElement);
	}
	
	public void removeAllDisplayElements() {
		entities = new CopyOnWriteArraySet<>();
		tiles = new CopyOnWriteArraySet<>();
	}
	
	public synchronized BufferedImage getNextRenderedImage() {
		return nextImage;
	}
	
	/**
	 * Sets the next image to render
	 */
	public synchronized void setNextRenderingImage(BufferedImage nextImage) {
		this.nextImage = nextImage;
		nextImageRendered = true;
	}
	
	/**
	 * Updates the next Image
	 */
	@Override
	public void tick(long delta) {
		if (nextImageRendered) {
			nextImageRendered = false;
		} else {
			nextImage = new BufferedImage(frame.getWidth(), frame.getHeight(), BufferedImage.TYPE_INT_ARGB);
			
			Graphics2D g = (Graphics2D) nextImage.getGraphics();
			
			// Update Tiles
			for (Tile t : tiles) {
				t.update();
			}
			// End Update Tiles
			
			// Start Draw		
			for (Tile t : tiles) {
				t.render(g);
			}
			for (Entity e : entities) {
				e.render(g);
			}
			// End Draw
			
			g.dispose();
		}
		State.getState().update();
	}
	
	/**
	 * Renders the next Image
	 */
	public void render() {
		bs = canvas.getBufferStrategy();

		g = (Graphics2D) bs.getDrawGraphics();
		g.clearRect(0, 0, getSize().width, getSize().height);
		
		g.drawImage(nextImage, 0, 0, nextImage.getWidth(), nextImage.getHeight(), 0, 0, nextImage.getWidth(), nextImage.getHeight(), null);
		
		State.getState().render(g);
		
		bs.show();
		g.dispose();
	}
}
