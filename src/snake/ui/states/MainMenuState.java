package snake.ui;

import java.awt.image.BufferedImage;

import snake.Game;
import snake.State;
import snake.ui.tiles.Button;
import snake.ui.tiles.ImageTile;
import utils.io.ImageLoader;
import utils.io.IniAdapter;
import utils.io.PathsLoader;
import utils.ui.SpriteSheet;

public class MainMenuState extends State {

	public final static IniAdapter mainMenuIni;
	
	// TILES
	private ImageTile background;
	private Button start, load, options, stop;
	// END TILES
	
	static {
		mainMenuIni = new IniAdapter(PathsLoader.getSavedPath("main_menu_ini"));
	}
	
	public MainMenuState(Game game) {
		super(game, "Main-Menu");
	}

	@Override
	public void onSet() {
		// LOAD RESOURCES
		BufferedImage backgroundImage = ImageLoader.scale(ImageLoader.toBufferedImage(ImageLoader.getImage(mainMenuIni.getString("backgroundImagePath"))), width, height);
		
		BufferedImage buttonsSpriteSheet = ImageLoader.toBufferedImage(ImageLoader.getImage("res/ui/buttons/mainMenuButtons.png"));
		SpriteSheet mainMenuButtons = new SpriteSheet(buttonsSpriteSheet);
		
		BufferedImage[] startButtonResources = {mainMenuButtons.getSprite(0, 0, 250, 100), mainMenuButtons.getSprite(250, 0, 250, 100), mainMenuButtons.getSprite(500, 0, 250, 100)};
		BufferedImage[] loadButtonResources = {mainMenuButtons.getSprite(0, 100, 250, 100), mainMenuButtons.getSprite(250, 100, 250, 100), mainMenuButtons.getSprite(500, 100, 250, 100)};
		BufferedImage[] optionsButtonResources = {mainMenuButtons.getSprite(0, 200, 250, 100), mainMenuButtons.getSprite(250, 200, 250, 100), mainMenuButtons.getSprite(500, 200, 250, 100)};
		BufferedImage[] stopButtonResources = {mainMenuButtons.getSprite(0, 300, 250, 100), mainMenuButtons.getSprite(250, 300, 250, 100), mainMenuButtons.getSprite(500, 300, 250, 100)};
		// END LOAD RESOURCES
		
		// CREATE TILES
		background = new ImageTile(backgroundImage, 0, 0, width, height);
		start = new Button(startButtonResources, 515, 120, 250, 100);
		start.addActionListener(e -> {
			State.setState(game.getGameState());
		});
		
		load = new Button(loadButtonResources, 515, 245, 250, 100);
		load.addActionListener(e -> {
			State.setState(game.getLoadGame());
		});
		
		options = new Button(optionsButtonResources, 515, 370, 250, 100);
		options.addActionListener(e -> {
			State.setState(game.getMenuOptions());
		});
		
		stop = new Button(stopButtonResources, 515, 495, 250, 100);
		stop.addActionListener(e -> {
			game.stop();
		});
		
		// END CREATE TILES
		
		// ADD TILES
		display.addDisplayElement(background);
		display.addDisplayElement(start);
		display.addDisplayElement(load);
		display.addDisplayElement(options);
		display.addDisplayElement(stop);
		buttons.add(start);
		buttons.add(load);
		buttons.add(options);
		buttons.add(stop);
		// END ADD TILES
	}

}
