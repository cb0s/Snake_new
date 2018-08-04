package snake;

import java.awt.Graphics2D;

public abstract class State {
	
	private static State currentState = null;
	protected Game game;
	
	public State(Game game) {
		this.game = game;
	}
	
	public static State getState() {
		return currentState;
	}
	
	public static void setState(State state) {
		currentState = state;
	}
	
	public abstract void update();
	
	public abstract void render(Graphics2D g);
	
}
