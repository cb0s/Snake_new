package snake;

import java.awt.Image;
import java.awt.Point;

/**
 * 
 * @author Cedric
 * @description This class exists because if there are added more Objects like particles you can't express them with Enities
 * 
 */
public interface GameObject2D {
	public int getX();
	public int getY();
	public Image getResource();
	public void setPosition(int x, int y);
	public Point getPosition();
}
