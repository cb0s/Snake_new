package snake.ui.states;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;

import snake.Game;
import snake.io.Files;
import snake.io.Resources;
import snake.ui.tiles.Field;
import snake.ui.tiles.ScoreBoard;

public class GameState extends State {

	private Field gameField;
	private ScoreBoard scoreBoard;
	
	private volatile boolean running;
	
	public GameState(Game parent) {
		super(parent);
		running = false;
		gameField = null;
		scoreBoard = null;
	}

	private synchronized void startGame() {
		running = true;
	}
	
	private synchronized void stopGame() {
		running = false;
	}
	
	public int getLevel() {
		return scoreBoard.getLevel();
	}
	
	public void reset() {
		// This image needn't be safed in the buffer as you just have one instance per game
		BufferedImage img = Resources.getImage(new File(Files.internal.RES_UI_PATH + "tiles/field.png"));
		int collums = Integer.parseInt(Game.GAME_INI.getString("field_collums"));
		int rows = Integer.parseInt(Game.GAME_INI.getString("field_rows"));
		gameField = new Field(img, (int)(parent.getConfig().width-img.getWidth())/2, (int)(parent.getConfig().height-img.getHeight())/2, rows, collums);
		
		img = Resources.getImage(new File(Files.internal.RES_UI_PATH + "tiles/scoreboard.png"));
		scoreBoard = new ScoreBoard(img, parent.getConfig().width-img.getWidth(), 20, img.getWidth(), img.getHeight(), Integer.parseInt(Game.GAME_INI.getString("level_max"))); 
		scoreBoard.setBounds(parent.getConfig().width-img.getWidth(), 20, img.getWidth(), img.getHeight());
	}
	
	@Override
	public void prepare() {
		if (!isReady()) {
			reset();
			super.prepare();
		}
	}
	
	@Override
	public void onApply() {
		prepare();
		startGame();
	}
	
	@Override
	public void onDetach() {
		scoreBoard.stopTime();
		stopGame();
	}
	
	@Override
	public void render(Graphics2D g) {
		gameField.render(g);
		scoreBoard.render(g);
	}

}
