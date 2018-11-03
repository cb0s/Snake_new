package snake.ui.tiles;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import snake.ui.Sprite;

@SuppressWarnings("serial")
public abstract class Tile extends Sprite {
	
	// NOT IMPLEMENTED YET
	public Tile(BufferedImage resource, float xRatio, float yRatio, float widthRatio, float heightRatio) throws NoSuchMethodError {
		super(resource, 0, 0, 0, 0);
		throw new NoSuchMethodError("This Constructor is not implemented yet.");
	}
	
	public Tile(BufferedImage resource, int x, int y, int width, int height) {
		super(resource, x, y, width, height);
	}
	
	public void render(Graphics2D g) {
		g.drawImage(resource, x, y, x+width, y+height, 0, 0, width, height, null);
	}
}
