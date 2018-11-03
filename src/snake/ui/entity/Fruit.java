package snake.ui.entity;

import java.awt.image.BufferedImage;

import snake.io.Files;
import utils.io.IniAdapter;

@SuppressWarnings("serial")
public abstract class Fruit extends Item {
	
	private int value, special;
	private String name;
	
	protected final static IniAdapter FRUIT_INI;
	
	static {
		FRUIT_INI = new IniAdapter(Files.internal.DATA_PATH + "ini/game/fruits.ini");
	}
	
	public Fruit(BufferedImage resource, int x, int y, int width, int height, String name, int value, int special) {
		super (resource, x, y, width, height);
		this.name = name;
		this.value = value;
		this.special = special;
	}
	
	public int getValue() {
		return value;
	}
	
	public int getSpecial() {
		return special;
	}
	
	public String getName() {
		return name;
	}

	@Override
	protected void onSpawn() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void onDespawn() {
		// TODO Auto-generated method stub
		
	}

}
