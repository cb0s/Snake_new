package snake.ui.states;

import java.awt.Graphics2D;

public abstract class State {
	
	protected volatile boolean prepared;
	
	/**
	 * Prepares this State for being shown on a Display.<br>
	 * Operations like loading graphics, (re)setting positions, etc should not be done in the constructor but rather in this method.<br>
	 * This method can be explicitly called, especially on larger States, for example while showing a loading screen.<br>
	 * However it doesen't have to be called explicitly, which is why {@link #onApply()} should call it if it hasn't been called before.<br>
	 * This method should be overridden by subclasses.
	 */
	public synchronized void prepare() {
		//...
		prepared = true;
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
	
}
