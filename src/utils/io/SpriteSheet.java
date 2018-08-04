package utils.io;

import java.awt.image.BufferedImage;

/**
 * 
 * @author Cedric
 * @version 1.0
 * @category util</br></br>
 * 
 * This class allows you to load Sprite-Sheets in Java.
 * 
 */
public class SpriteSheet {

	// ******************
	// * Private Fields *
	// ******************
	private BufferedImage sheet;
	
	// ****************
	// * Constructors *
	// ****************
	/**
	 * Create a new SpriteSheet with the SpriteSheet Image.
	 * 
	 * @param sheet Image of the Sprite-Sheet
	 */
	public SpriteSheet(BufferedImage sheet) {
		this.sheet = sheet;
	}
	
	// ******************
	// * Public Methods *
	// ******************
	/**
	 * Get a sprite of the given Sprite-Sheet.
	 * 
	 * @param x x-Coordinate in the Sprite-Sheet
	 * @param y y-Coordinate in the Sprite-Sheet
	 * @param size height and width of the Sprite you want to get
	 * @return specified Sprite
	 */
	public BufferedImage getSprite(int x, int y, int size) {
		return getSprite(x, y, size, size);
	}
	
	/**
	 * Get a sprite of the given Sprite-Sheet.
	 * 
	 * @param x x-Coordinate in the Sprite-Sheet
	 * @param y y-Coordinate in the Sprite-Sheet
	 * @param width Width of the Sprite
	 * @param height Height of the Sprite
	 * @return specified Sprite
	 */
	public BufferedImage getSprite(int x, int y, int width, int height) {
		return sheet.getSubimage(x, y, width, height);
	}
	
}
