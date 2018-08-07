package snake.ui.entity;

import java.awt.image.BufferedImage;

public abstract class SnakePart extends Entity {

	private BufferedImage texture;
	
	public SnakePart(float x, float y, BufferedImage texture) {
		super(x, y);
		this.texture = texture;
	}
	
	public BufferedImage getImage() {
		return texture;
	}
	
	public void updateTexture(BufferedImage texture) {
		this.texture = texture;
	}
	
}
