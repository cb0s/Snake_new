package snake.ui.tiles;

@SuppressWarnings("serial")
public class Field extends Tile {
	private int rows, collums;
	public Field(int rows, int collums) {
		super(null, 0, 0, 0, 0);
		this.rows = rows;
		this.collums = collums;
	}

	public int getRows() {
		return rows;
	}
	
	public int getCollums() {
		return collums;
	}
	
	@Override
	public void update() {
		
	}
}
