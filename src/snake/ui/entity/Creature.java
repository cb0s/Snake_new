package snake.ui.entity;

import java.awt.image.BufferedImage;

@SuppressWarnings("serial")
public abstract class Creature extends Entity {
	
	/**
	 * Whether a Creature is alive or not
	 */
	private boolean alive;
	
	/**
	 * Not implemented yet!
	 * 
	 * 
	 * @param resources
	 * @param x
	 * @param y
	 * @param height
	 * @param width
	 */
	public Creature(BufferedImage resources, float x, float y, int width, int height) throws NoSuchMethodError {
		super(resources, x, y, width, height);
		throw new NoSuchMethodError("This Constructor is not implemented yet");
	}
	
	public Creature(BufferedImage resource, int x, int y, int width, int height) {
		super(resource, x, y, width, height);
	}
	
	public boolean isAlive() {
		return alive;
	}

	/**
	 * That there is a differentiation between being alive and spawned is more for later updates
	 */
	@Override
	protected void onSpawn() {
		startLife();
	}
	
	public void startLife() {
		alive = true;
		onStartLife();
	}
	
	protected abstract void onStartLife();
	
	public void die() {
		alive = false;
		onDie();
	}
	
	protected abstract void onDie();
}
