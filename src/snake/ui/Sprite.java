/**
 * 
 */
package snake.ui;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

/**
 * @author Leo
 */
@SuppressWarnings("serial")
public class Sprite extends Rectangle implements Drawable {

	protected final BufferedImage resource;
	protected volatile double radRotation = 0;
	protected volatile int srcX1, srcY1, srcX2, srcY2, originX, originY;
	
	
	public Sprite(BufferedImage image) {
		this(image, image.getWidth(), image.getHeight());
	}
	
	public Sprite(BufferedImage image, int srcWitdh, int srcHeight) {
		this(image, 0, 0, srcWitdh, srcHeight);
	}
	
	public Sprite(BufferedImage image, int srcX, int srcY, int srcWidth, int srcHeight) {
		if(image == null) throw new NullPointerException("Image cannot be null!");
		this.resource = image;
		setRegion(srcX, srcY, srcWidth, srcHeight);
		setSize(srcWidth, srcHeight);
		setOrigin(0, 0);
	}
	
	
	
	
	public void setOrigin(int x, int y) {
		originX = x;
		originY = y;
	}
	
	public void centerOrigin() {
		setOrigin(width/2, height/2);
	}
	
	public void setRotation(double degrees) {
		this.radRotation = Math.toRadians(degrees);
	}
	
	public void rotate(double degrees) {
		radRotation += Math.toRadians(degrees);
	}
	
	public void setRegion(int srcX, int srcY, int srcWidth, int srcHeigt) {
		this.srcX1 = srcX;
		this.srcY1 = srcY;
		this.srcX2 = srcX+srcWidth;
		this.srcY2 = srcY+srcHeigt;
	}
	
	@Override
	public void render(Graphics2D g) {
		g.translate(x, y);
		g.rotate(radRotation);
		g.drawImage(resource, -originX, -originY, -originX+width, -originY+height, srcX1, srcY1, srcX2, srcY2, null);
		g.rotate(-radRotation);
		g.translate(-x, -y);
	}

}
