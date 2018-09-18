package snake.ui.drawable.entity;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;

import snake.ui.drawable.Drawable;

@SuppressWarnings("serial")
public abstract class Entity extends Drawable {
	
	/**
	 * Whether an Entity is on the Field or not
	 */
	private boolean spawned;
	/**
	 * Position on the field
	 */
	private Point pos;
	protected BufferedImage currentImage;
	
	/**
	 * This constructor is not implemented yet!
	 * 
	 * @param resource
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 * @throws NoSuchMethodError
	 */
	public Entity(BufferedImage resource, float x, float y, float width, float height) throws NoSuchMethodError{
		super(resource, x, y, width, height);
		currentImage = resource;
		throw new NoSuchMethodError("This Constructor is not implemented yet.");
	}
	
	public Entity(BufferedImage resource, int x, int y, int width, int height) {
		super(resource, x, y, width, height);
	}
	
	protected void setCurrentImage(BufferedImage image) {
		this.currentImage = image;
	}
	
	public Point getPos() {
		return pos;
	}
	
	public Point updatePos(Point pos) {
		Point p = this.pos;
		this.pos = pos;
		return p;
	}
	
	public boolean isSpawned() {
		return spawned;
	}
	
	public void spawn(Point pos) {
		this.pos = pos;
		spawned = true;
		onSpawn();
	}
	
	protected abstract void onSpawn();
	
	public void despawn() {
		spawned = false;
		onDespawn();
	}
	
	protected abstract void onDespawn();
	
	public abstract void update();

	public void render(Graphics2D g) {
		g.drawImage(currentImage, x, y, x + width, y + height, 0, 0, currentImage.getWidth(null), currentImage.getHeight(null), null);
	}
	
}
