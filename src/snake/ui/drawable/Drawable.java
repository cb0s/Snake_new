package snake.ui.drawable;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

/**
 * @author leo
 *
 */
@SuppressWarnings("serial")
public abstract class Drawable extends Rectangle {

	protected BufferedImage resource;
	
	/**
	 * Not implemented yet!
	 * 
	 * @param resource
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 * @throws NoSuchMethodError
	 */
	public Drawable(BufferedImage resource, float x, float y, float width, float height) throws NoSuchMethodError {
		if (resource == null) {
			//TODO: Change
			//Display.getRenderLogger().logWarning("A Sprite with no was created");
		}
		this.resource = resource;
		/*
		Display display = Game.getDisplay();
		this.x = display.formatRatioX(x);
		this.y = display.formatRatioY(y);
		this.width = display.formatRatioX(width);
		this.height = display.formatRatioY(height);*/
		throw new NoSuchMethodError("This Constructor is not implemented yet!");
	}

	public Drawable(BufferedImage resource, int x, int y, int width, int height) {
		super(x, y, width, height);
		this.resource = resource;
	}
	
	public abstract void render(Graphics2D g);
	
}