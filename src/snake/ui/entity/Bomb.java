package snake.ui.entity;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

@SuppressWarnings("serial")
public class Bomb extends Item {

	/**
	 * Tick Times in ticks.
	 */
	private int tickTime, timePassed;
	private boolean tick;
	
	private BufferedImage resourceExplode;
	private BufferedImage resourceToRender;
	
	public Bomb(BufferedImage resource, BufferedImage resourceExplode, int x, int y, int width, int height, int tickTime) {
		super (resource, x, y, width, height);
		this.tickTime = tickTime;
		this.timePassed = 0;
		this.tick = false;
		this.resourceExplode = resourceExplode;
		this.resourceToRender = this.resource;
	}

	public int getTickTime() {
		return tickTime;
	}
	
	public int getRemainingTime() {
		return tickTime-timePassed;
	}
	
	public void startTicking() {
		tick = true;
	}
	
	public void freeze() {
		tick = false;
	}
	
	public boolean tickingStarted() {
		return tick;
	}
	
	public void explode() {
		resourceToRender = resourceExplode;
	}
	
	@Override
	public void update() {
		timePassed++;
		if (tickTime-timePassed == 0) explode();
	}

	@Override
	public void render(Graphics2D g) {
		g.drawImage(resourceToRender, x, y, width, height, x, y, width, height, null);
	}

	@Override
	protected void onSpawn() {
		startTicking();
	}

	@Override
	protected void onDespawn() {
		// TODO Auto-generated method stub
		
	}

}
