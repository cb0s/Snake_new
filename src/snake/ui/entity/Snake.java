package snake.ui.entity;

import utils.Stack;

@SuppressWarnings("serial")
public class Snake extends Creature {

	private Stack<SnakePart> snakeParts;
	
	public Snake(float x, float y) {
		super(null, x, y, 0, 0);
		snakeParts = new Stack<SnakePart>();
		snakeParts.add(new SnakeHead(0, 0, null));
		// TODO Auto-generated constructor stub
	}

	@Override
	public void update() {
		
	}

	@Override
	protected void onStartLife() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void onDie() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void onDespawn() {
		// TODO Auto-generated method stub
		
	}

}
