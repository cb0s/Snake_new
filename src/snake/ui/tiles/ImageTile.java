package snake.ui.tiles;

import java.awt.image.BufferedImage;

@SuppressWarnings("serial")
public class ImageTile extends Tile {

	/**
	 * This Constructor is not implemented yet!
	 * 
	 * @param resource
	 * @param xRatio
	 * @param yRatio
	 * @param widthRatio
	 * @param heightRatio
	 * @throws NoSuchMethodError
	 */
	public ImageTile(BufferedImage resource, float xRatio, float yRatio, float widthRatio, float heightRatio) throws NoSuchMethodError{
		super(resource, xRatio, yRatio, widthRatio, heightRatio);
		// TODO Auto-generated constructor stub
		throw new NoSuchMethodError("This Constructor is not implemented yet!");
	}

	public ImageTile(BufferedImage resource, int x, int y, int width, int height) {
		super(resource, x, y, width, height);
	}
	
	@Override
	public void update() {
		
	}

}
