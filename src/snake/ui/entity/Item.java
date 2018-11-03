package snake.ui.entity;

import java.awt.image.BufferedImage;

import snake.io.Files;
import utils.io.IniAdapter;

public abstract class Item extends Entity {

//	private int currentImage;
//	private boolean curentImageIncrease;
//	protected BufferedImage[] resources;
	
	protected final static IniAdapter ITEM_INI;
	
	static {
		ITEM_INI = new IniAdapter(Files.internal.DATA_PATH + "ini/game/item.ini");
	}
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Not implemented yet!
	 * 
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 * @param resources
	 */
	public Item(int x, int y, int width, int height, BufferedImage[] resources) throws NoSuchMethodException {
		super(resources[0], x, y, width, height);
//		this.resources = resources;
//		currentImage = 0;
//		currentImageIncrease = true;
		throw new NoSuchMethodException("This Constructor is not implemented yet!");
	}
	
	public Item(BufferedImage resource, int x, int y) {
		super (resource, x, y);
	}
	
	public Item(BufferedImage resource, int x, int y, int width, int height) {
		super(resource, x, y, width, height);
	}
	
	
	/*
	public BufferedImage[] getResources() {
		return resources;
	}
	
	public void updateResources(BufferedImage[] resources) {
		this.resources = resources;
	}
	
	public BufferedImage getCurrentImage() {
		return resources[currentImage];
	}*/
	
	@Override
	public void update() {
		/* // This is not implemented yet!
		// Update Resources --> Needs at least 2 Images --> Maybe this ImageUpdating is better for Creatures
		if (resources.length > 1) {
			if (currentImageIncrease) {
				if (currentImage+2 == resources.length) currentImageIncrease = false;
				currentImage++;
			} else {
				if (currentImage == 1) currentImageIncrease = true;
				currentImage--;
			}
			setCurrentImage(resources[currentImage]);
		}*/	// This is not implemented yet!
	}

}
