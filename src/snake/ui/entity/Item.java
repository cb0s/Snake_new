package snake.ui.entity;

import java.awt.image.BufferedImage;

public abstract class Item extends Entity {

	protected BufferedImage[] resources;
	
	public Item(float x, float y, BufferedImage[] resources) {
		super(x, y);
		this.resources = resources;
	}
	
	public BufferedImage[] getResources() {
		return resources;
	}
	
	public void updateResources(BufferedImage[] resources) {
		this.resources = resources;
	}

}
