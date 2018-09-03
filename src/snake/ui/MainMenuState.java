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
	private Button start, load, options;
	// END TILES
	
	static {
		mainMenuIni = new IniAdapter(PathsLoader.getSavedPath("main_menu_ini"));
	}
	
	public MainMenuState(Game game) {
		super(game, "Main-Menu");
	}

	@Override
	public void onSet() {
		int width = display.getSize().width, height = display.getSize().height;
		
		// LOAD RESOURCES
		BufferedImage backgroundImage = ImageLoader.scale(ImageLoader.toBufferedImage(ImageLoader.getImage(mainMenuIni.getString("backgroundImagePath"))), width, height);
		
		BufferedImage buttonsSpriteSheet = ImageLoader.toBufferedImage(ImageLoader.getImage("res/ui/buttons/mainMenuButtons.png"));
		SpriteSheet mainMenuButtons = new SpriteSheet(buttonsSpriteSheet);
		
		BufferedImage[] startButtonResources = {mainMenuButtons.getSprite(0, 0, 250, 100), mainMenuButtons.getSprite(250, 0, 250, 100), mainMenuButtons.getSprite(500, 0, 250, 100)};
		// END LOAD RESOURCES
		
		// CREATE TILES
		background = new ImageTile(backgroundImage, 0, 0, width, height);
		start = new Button(startButtonResources, 100, 100, 250, 100);
		// END CREATE TILES
		
		// ADD TILES
		display.addDisplayElement(background);
		display.addDisplayElement(start);
		buttons.add(start);
		// END ADD TILES
	}

}
