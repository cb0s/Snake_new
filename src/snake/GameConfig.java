package snake;

import java.awt.Image;
import java.io.File;

import snake.io.Files;
import snake.io.Resources;

public class GameConfig {
	
	public String title = "";
		
	public int width = 1280;
	public int height = 720;
	public boolean resizable = false;
	public boolean undecorated = false;
	
	public String renderLogfile = "render.log";
	public float fps = 60.0f;
	public int bufferSize = 3;

	public Image image = Resources.getImage(new File(Files.internal.RES_UI_PATH + "snake_logo.jpg"));
	
}
