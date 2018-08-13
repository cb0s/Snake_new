package utils.mechanics;

/**
 * An interface defining an object that can be passed to a {@link Clock} to be periodically called.
 * 
 * @author Leo
 * @version 1.0
 * @category engine
 */
public interface Clockable {
	
	/**
	 * Method to be periodically called by a Clock. Recieves the time elapsed since the last call in nano seconds.
	 * 
	 * @param delta time since last call in nano seconds
	 * 
	 * @see Clock
	 */
	public void tick(long delta);

}
