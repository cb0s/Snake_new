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
		//TODO: change, now that resume doesen't exist anymore
		//game.resume();
		super.update();
	}

	@Override
	public void render(Graphics2D g) {

	}

	@Override
	public void onSet() {
	}

}
