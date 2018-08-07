package snake.ui.entity;

import java.awt.Graphics2D;
import utils.Stack;

public class Snake extends Creature {

	private Stack<SnakePart> snakeParts;
	
	public Snake(float x, float y) {
		super(x, y);
		snakeParts = new Stack<SnakePart>();
		snakeParts.add(new SnakeHead(0, 0, null));
		// TODO Auto-generated constructor stub
	}

	@Override
	public void update() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void render(Graphics2D g) {
		// TODO Auto-generated method stub
		
	}

}
