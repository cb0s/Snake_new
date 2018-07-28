package snake.ui;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

import utils.io.ImageLoader;

/**	
 * @author Cedric
 * @version 1.0
 * @category ui
 **/

@SuppressWarnings("serial")
public class BackgroundImageLoader extends JPanel {
	
	private Image background;
	
	public BackgroundImageLoader(Image img) {
		updateImage(img);
	}
	
	public void updateImage(Image img) {
		background = img;
	}
	
	public BufferedImage getImg() {
		return ImageLoader.toBufferedImage(background);
	}
	
	@Override
	public void paintComponent(Graphics go) {
		Graphics2D g = (Graphics2D) go;
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.drawImage(ImageLoader.scale(ImageLoader.toBufferedImage(background), WindowAdapter.mainGetDimensions().width+4, WindowAdapter.mainGetDimensions().height+4), -2, -2, null);
	}
}
