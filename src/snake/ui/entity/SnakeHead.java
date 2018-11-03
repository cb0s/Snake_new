package snake.ui.entity;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.Stack;

@SuppressWarnings("serial")
public class SnakeHead extends SnakePart {

	private Stack<SnakePart> snakeTails;
	
	public SnakeHead(BufferedImage texture, int x, int y) {
		super(texture, x, y, texture.getWidth(), texture.getHeight());
		
		snakeTails = new Stack<SnakePart>();
	}

	@Override
	public void update() {
		
	}

	@Override
	public void render(Graphics2D g) {
		
	}

	@Override
	protected void onSpawn() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void onDespawn() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void onStartLife() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void onDie() {
		// TODO Auto-generated method stub
		
	}

}
