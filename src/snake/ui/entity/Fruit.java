package snake.ui.entity;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class Fruit extends Item {
	
	private int size, special;
	private String name;
	
	public Fruit(String name, BufferedImage[] resources, int size, int special, float x, float y) {
		super (x, y, resources);
		this.name = name;
		this.size = size;
		this.special = special;
	}
	
	public int getSize() {
		return size;
	}
	
	public int getSpecial() {
		return special;
	}
	
	public String getName() {
		return name;
	}

	@Override
	public void update() {
		
	}

	@Override
	public void render(Graphics2D g) {
		
	}
}
