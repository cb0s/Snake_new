package snake.ui.entity.fruits;

import java.awt.image.BufferedImage;

import snake.ui.entity.Fruit;

@SuppressWarnings("serial")
public class Melon extends Fruit {

	public static final int DEFAULT_VALUE;
	public static final int DEFAULT_SPECIAL;
	
	static {
		DEFAULT_SPECIAL = Integer.parseInt(FRUIT_INI.getString("melon_special"));
		DEFAULT_VALUE = Integer.parseInt(FRUIT_INI.getString("melon_value"));
	}
	
	public Melon(BufferedImage resource, int x, int y) {
		this (resource, x, y, resource.getWidth(), resource.getHeight(), 0, 0);
	}
	
	public Melon(BufferedImage resource, int x, int y, int width, int height, int value, int special) {
		super(resource, x, y, width, height, "Melon", value, special);
	}

}
