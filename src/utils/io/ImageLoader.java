package utils.io;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

import utils.ui.DialogManager;

/** 
 * 	@author Cedric	
 *	@version 1.0
 *	@category util</br></br>
 *
 *	This class allows you to load images from an image-file and do certain things with it.</br></br>
 *	Your possibilities are:</br>
 *		- Create an Image-Object</br>
 *		- Scaling</br>
 *		- Rotating (not implemented yet) -> TODO: create this method</br>
 *		- Mirroring (not implemented yet) -> TODO: create this method</br>
 *		- Convert normal Image-Objects into BufferedImages</br>
 *		- Apply Filters:</br>
 *			- Gaussian
 *
 **/
public class ImageLoader {
	
	// ******************
	// * Public Methods *
	// ******************
	/**
	 * Loads the image from the given path and returns it.</br>
	 * Returns null if an error occurs
	 * 
	 * @param path to the image which should be loaded
	 * @return the loaded Image
	 */
	public static Image getImage(String path) {
		try {
			Logger.getDefaultLogger().logInfo("Trying to load image " + path);
			if(Installer.isInstalled())
				return ImageIO.read(new File(path));
			else
				return ImageIO.read(ImageLoader.class.getResource("/" + path));
		} catch (Exception e) {
			Logger.gdL().log("An error occured while loading image \"" + path + "\"!");
			Logger.gdL().logException(e);
			DialogManager.showExeptionDialog(null, e, "An Error occured while loading image \"" + path + "\"\n\nError:\n%exception%\n\nExiting now!", "IO-Error", true);
		}
		return null;
	}
	
	/**
	 * Scales the given Image to the width and the height and returns it.</br>
	 * This returns null if the input Image is null.
	 * 
	 * @param imgToScale Image to be scaled
	 * @param width new width of the Image
	 * @param height new height of the Image
	 * @return the given Image with a new width and height
	 */
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
	
	@Deprecated
	public static BufferedImage rotate(BufferedImage img) throws NoSuchMethodException {
		throw new NoSuchMethodException("This method is not implemented yet!");
	}
	
	@Deprecated
	public static BufferedImage mirror(BufferedImage img) throws NoSuchMethodException {
		throw new NoSuchMethodException("This method is not implemented yet!");
	}
	
	/**
	 * Makes an Image to a BufferedImage.
	 * 
	 * @param img Image to be converted
	 * @return Image <i>img</i> as a BufferedImage
	 */
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
	
	public static BufferedImage applyGaussian(BufferedImage img, int radius) {
		if (img == null) throw new NullPointerException();
		if (radius < 0) throw new NumberFormatException("Radius cannot be smaller than 0");
		// TODO!
		return img;
	}
	
}
