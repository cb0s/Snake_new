package snake.ui;

import java.awt.Color;
import java.awt.Graphics2D;

import snake.Game;
import snake.State;

public class GameState extends State {

	public GameState(Game game) {
		super(game);
	}
	
	@Override
	public void update() {
		
	}

	@Override
	public void render(Graphics2D g) {
		g.setColor(Color.GREEN);
		g.fillRect(0, 0, 200, 200);
	}

}
