package snake.oldUI;

import java.awt.Image;
import java.awt.Point;

public abstract class Entity implements GameObject2D {
	protected int x = -1;
	protected int y = -1;
	protected Image resource = null;
	
	public Entity(int x, int y, Image resource) {
		this.x = x;
		this.y = y;
		this.resource = resource;
	}
	
	public void setPosition(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
	public Point getPosition() {
		return new Point(x, y);
	}
	
	public Image getResource() {
		return resource;
	}
}
