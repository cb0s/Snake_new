package snake.ui.entity;

import java.awt.Graphics2D;
import java.awt.Point;

public abstract class Entity {
	
	protected float x, y;
	
	public Entity(float x, float y) {
		this.x = x;
		this.y = y;
	}
	
	public float getX() {
		return x;
	}
	
	public float getY() {
		return y;
	}
	
	public Point getLocation() {
		return new Point((int) x, (int) y);
	}
	
	public void setX(float x) {
		this.x = x;
	}
	
	public void setY(float y) {
		this.y = y;
	}
	
	public void setPoint(Point p) {
		x = p.x;
		y = p.y;
	}
	
	public abstract void update();
	
	public abstract void render(Graphics2D g);
	
}
