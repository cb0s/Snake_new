package snake.ui.entity.fruits;

import java.awt.image.BufferedImage;

import snake.ui.entity.Fruit;

@SuppressWarnings("serial")
public class Orange extends Fruit {

	public static final int DEFAULT_VALUE;
	public static final int DEFAULT_SPECIAL;
	
	static {
		DEFAULT_SPECIAL = Integer.parseInt(FRUIT_INI.getString("orange_special"));
		DEFAULT_VALUE = Integer.parseInt(FRUIT_INI.getString("orange_value"));
	}
	
	public Orange(BufferedImage resource, int x, int y) {
		this (resource, x, y, resource.getWidth(), resource.getHeight(), 0, 0);
	}
	
	public Orange(BufferedImage resource, int x, int y, int width, int height, int value, int special) {
		super(resource, x, y, width, height, "Orange", value, special);
	}

}
