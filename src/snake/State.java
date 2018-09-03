package snake;

import java.awt.Graphics2D;
import java.util.ArrayList;

import snake.ui.Display;
import snake.ui.tiles.Button;

public abstract class State {
	
	private static State currentState = null;
	protected final String name;
	protected final Game game;
	protected final Display display;
	protected final ArrayList<Button> buttons;
	protected int width, height;
	
	public State(Game game,  String name) {
		Display.getRenderLogger().logInfo("Creating " + name + "-State");
		this.game = game;
		this.display = game.getDisplay();
		this.name = name;
		buttons = new ArrayList<Button>();
	}
	
	/**
	 * This method is called with every Display-Tick
	 * 
	 * @param display The Display which ticks
	 */
	public void update() {
		SnakeGame.getSnake().getDisplay().render();
	}
	
	/**
	 * This method gets called on the active State when the Display renders
	 * 
	 * @param g
	 */
	public void render(Graphics2D g) {}
	
	public abstract void onSet();
	
	public static State getState() {
		return currentState;
	}
	
	public static void setState(State state) {
		Display.getRenderLogger().logInfo("Setting State to " + state.name);
		currentState = state;
		currentState.display.removeAllDisplayElements();
		currentState.width = currentState.display.getSize().width;
		currentState.height = currentState.display.getSize().height;
		currentState.onSet();
	}
	
	public Button[] getButtons() {
		Button[] buffer = new Button[buttons.size()];
		buttons.toArray(buffer);
		return buffer;
	}
	
}
