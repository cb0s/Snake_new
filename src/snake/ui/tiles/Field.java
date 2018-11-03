package snake.ui.tiles;

import java.awt.image.BufferedImage;

import snake.ui.entity.Entity;
import snake.ui.entity.SnakeHead;

@SuppressWarnings("serial")
public class Field extends Tile implements Spawnable {
	private int rows, collums;
	public Field(BufferedImage background, int x, int y, int rows, int collums) {
		super(background, x, y, background.getWidth(), background.getHeight());
		this.rows = rows;
		this.collums = collums;
	}

	public int getRows() {
		return rows;
	}
	
	public int getCollums() {
		return collums;
	}
	
	public SnakeHead getSnake() {
		return null;
	}

	public Entity getEntityOnField(int x, int y) {
		return null;
	}

	@Override
	public void spawn(Entity entity) {
		
	}
}
