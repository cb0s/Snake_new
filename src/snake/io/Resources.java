/**
 * 
 */
package snake.io;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import javax.imageio.ImageIO;

/**
 * @author leo
 *
 */
public class Resources {
	
	private static HashMap<File, BufferedImage> imageCache = new HashMap<>();
	
	public static BufferedImage getImage(File source) {
		Logger.gdL().logInfo("Loading image file: " + source.getPath());
		try {
			return ImageIO.read(source);
		} catch (IOException e) {
			Logger.gdL().logError("Exception while loading image file: " + source.getPath());
			Logger.gdL().logException(e);
			return null;
		}
	}
	
	public static BufferedImage getImage(File source, boolean cache) {
		if(cache && imageCache.containsKey(source)) {
			Logger.gdL().logInfo("Loading image from cache: " + source.getPath());
			return imageCache.get(source);
		}
		BufferedImage res = getImage(source);
		if(cache) {
			imageCache.put(source, res);
		}
		return res;
	}

}
