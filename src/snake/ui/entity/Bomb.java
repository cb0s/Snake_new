package snake.ui.entity;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class Bomb extends Item {

	private int tickTime;
	
	public Bomb(float x, float y, BufferedImage[] resources, int tickTime) {
		super(x, y, resources);
		this.tickTime = tickTime;
	}

	public int getTickTime() {
		return tickTime;
	}
	
	@Override
	public void update() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void render(Graphics2D g) {
		// TODO Auto-generated method stub
		
	}

}
