package snake.ui;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

@SuppressWarnings("serial")
public abstract class DisplayElement extends Rectangle {

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
	public DisplayElement(BufferedImage resource, float x, float y, float width, float height) throws NoSuchMethodError {
		if (resource == null) Display.getRenderLogger().logWarning("A Sprite with no was created");
		this.resource = resource;
		/*
		Display display = Game.getDisplay();
		this.x = display.formatRatioX(x);
		this.y = display.formatRatioY(y);
		this.width = display.formatRatioX(width);
		this.height = display.formatRatioY(height);*/
		throw new NoSuchMethodError("This Constructor is not implemented yet!");
	}

	public DisplayElement(BufferedImage resource, int x, int y, int width, int height) {
		super(x, y, width, height);
		this.resource = resource;
	}
	
	public abstract void update();
	
	public abstract void render(Graphics2D g);
	
}
