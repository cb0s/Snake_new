package snake.ui.states;

import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import snake.Game;
import snake.ui.Drawable;

public abstract class State implements Drawable, KeyListener, MouseListener, MouseMotionListener {
	
	protected Game parent;
	protected volatile boolean prepared;
	
	/**
	 * Creates a new State with the given {@link Game} as parent.
	 * 
	 * @param parent this State's parent
	 */
	public State(Game parent) {
		this.parent = parent;
		this.prepared = false;
	}
	
	/**
	 * Prepares this State for being shown on a Display.<br>
	 * Operations like loading graphics, (re)setting positions, etc should not be done in the constructor but rather in this method.<br>
	 * This method can be explicitly called, especially on larger States, for example while showing a loading screen.<br>
	 * However it doesen't have to be called explicitly, which is why {@link #onApply()} should call it if it hasn't been called before.<br>
	 * This method should be overridden by subclasses.
	 */
	public synchronized void prepare() {
		prepared = true;
		//...
	}
	
	/**
	 * Returns whether this State is ready to be applied to a Display (whether {@link #prepare()} has been run yet).
	 * 
	 * @return whether this State is ready to be applied to a Display
	 */
	public boolean isReady() {
		return prepared;
	}
	
	/**
	 * This method is called by a Display when being applied to it.<br>
	 * It should always check whether this State is prepared to render itself.
	 */
	public synchronized void onApply() {
		if(!prepared) {
			prepare();
		}
		//...
	}
	
	/**
	 * This method is called by a Display when being detached from it.
	 */
	public synchronized void onDetach() {
		//...
	}
	
	/**
	 * Makes this State draw itself onto the given Graphics2D.<br>
	 * This method must be overridden by subclasses.
	 * 
	 * @param g the Graphics on which to draw
	 */
	public abstract void render(Graphics2D g);

	
	

	@Override public void keyTyped(KeyEvent e) {}
	@Override public void keyPressed(KeyEvent e) {}
	@Override public void keyReleased(KeyEvent e) {}
	@Override public void mouseClicked(MouseEvent e) {}
	@Override public void mousePressed(MouseEvent e) {}
	@Override public void mouseReleased(MouseEvent e) {}
	@Override public void mouseEntered(MouseEvent e) {}
	@Override public void mouseExited(MouseEvent e) {}
	@Override public void mouseDragged(MouseEvent e) {}
	@Override public void mouseMoved(MouseEvent e) {}

}
