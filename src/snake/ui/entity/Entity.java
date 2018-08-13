package snake.ui.entity;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;

@SuppressWarnings("serial")
public abstract class Entity extends Rectangle {
	
	protected Image currentImage;
	
	public Entity(Image image, int x, int y, int width, int height) {
		super(x, y, width, height);
		currentImage = image;
	}
	
	protected synchronized void setCurrentImage(Image image) {
		this.currentImage = image;
	}
	
	public abstract void update();

	public void render(Graphics2D g) {
		g.drawImage(currentImage, x, y, x + width, y + height, 0, 0, currentImage.getWidth(null), currentImage.getHeight(null), null);
	}
	
}
