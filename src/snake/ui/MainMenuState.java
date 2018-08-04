package snake.ui;

import java.awt.Graphics2D;

import snake.Game;
import snake.State;
import utils.io.IniAdapter;
import utils.io.PathsLoader;

public class MainMenuState extends State {

	public final static IniAdapter mainMenuIni;
	
	static {
		mainMenuIni = new IniAdapter(PathsLoader.getSavedPath("main_menu_ini"));
	}
	
	public MainMenuState(Game game) {
		super(game);
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
