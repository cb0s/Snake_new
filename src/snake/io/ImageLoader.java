package snake.io;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

/** 
 * 	@author Cedric	
 *	@version 1.0
 *	@category io
 **/

public class ImageLoader {
	public static Image getImage(String path) {
		try {
			if(Installer.isInstalled())
				return ImageIO.read(new File(path));
			else
				return ImageIO.read(ImageLoader.class.getResource("/" + path));
		} catch (Exception e) {
			Logger.getDefaultLogger().logError("An error occured while loading image \"" + path + "\"!");
			String error = Logger.getDefaultLogger().logException(e);
			Logger.getDefaultLogger().logWarning("Exiting now!");
			JOptionPane.showMessageDialog(null, "An Error occured while loading image \"" + path + "\"\n\nError: " + error + "\n\nExiting now...", "Error", JOptionPane.ERROR_MESSAGE);
			System.exit(1);
		}
		return null;
	}

	public static BufferedImage scale(BufferedImage imgToScale, int width, int height) {
		BufferedImage dbi = null;
		if(imgToScale != null) {
			dbi = new BufferedImage(width, height, imgToScale.getType());
			Graphics2D g = dbi.createGraphics();
			AffineTransform at = AffineTransform.getTranslateInstance(0, 0);
			at.scale((double)width/imgToScale.getWidth(), (double)height/imgToScale.getHeight());
			g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
			g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
			g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			g.drawRenderedImage(imgToScale, at);
			g.dispose();
		}
		return dbi;
	}
	
	public static BufferedImage toBufferedImage(Image img) {
		if (img instanceof BufferedImage) return (BufferedImage) img;
		else {
			BufferedImage bImage = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);
			Graphics2D g = bImage.createGraphics();
			g.drawImage(img, 0, 0, null);
			g.dispose();
			return bImage;
		}
	}
	
}
