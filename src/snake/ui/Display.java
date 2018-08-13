package snake.ui;

import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.image.BufferStrategy;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.concurrent.CopyOnWriteArraySet;

import javax.swing.JFrame;

import snake.ui.entity.Entity;
import utils.io.Logger;
import utils.mechanics.Clock;

/**
 * A display showing Entities.
 * 
 * @author Cedric, Leo
 * @version 2.0
 * @category ui
 */
public class Display extends Clock {

	// *************
	// * Constants *
	// *************
	private static final float DEFAULT_FPS = 60.0f;
	
	
	

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
	private CopyOnWriteArraySet<Entity> entities;

	private Logger renderLogger;
	
	
	
	
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
			renderLogger = new Logger(new OutputStream[] {System.out, new FileOutputStream("render.log")});
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

		renderLogger.log("Creating BufferStrategy");
		canvas.createBufferStrategy(3);
		
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
	
	
	
	//Render-related stuff
	public void addEntity(Entity entity) {
		entities.add(entity);
	}
	
	public boolean removeEntity(Entity entity) {
		return entities.remove(entity);
	}
	
	@Override
	public void tick(long delta) {
		bs = canvas.getBufferStrategy();
		
		g = (Graphics2D) bs.getDrawGraphics();
		g.clearRect(0, 0, getSize().width, getSize().height);

		// Start Draw
		for(Entity e : entities) {
			e.render(g);
		}
		// End Draw
		
		bs.show();
		g.dispose();
	}
}
