package snake.ui.entity;

import java.awt.image.BufferedImage;

@SuppressWarnings("serial")
public abstract class SnakePart extends Creature {

	private BufferedImage texture;
	
	public SnakePart(BufferedImage texture, int x, int y, int width, int height) {
		super(texture, x, y, width, height);
		this.texture = texture;
	}
	
	public BufferedImage getImage() {
		return texture;
	}
	
	public void updateTexture(BufferedImage texture) {
		this.texture = texture;
	}
	
}
