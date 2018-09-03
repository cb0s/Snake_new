package snake.ui;

import java.awt.Color;
import java.awt.Graphics2D;

import snake.Game;
import snake.State;

public class GameState extends State {

	public GameState(Game game) {
		super(game, "Game");
	}
	
	@Override
	public void update() {
		game.resume();
		super.update();
	}

	@Override
	public void render(Graphics2D g) {

	}

	@Override
	public void onSet() {
	}

}
