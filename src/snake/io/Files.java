/**
 * 
 */
package snake.io;

import java.io.File;

/**
 * @author leo
 *
 */
public class Files {
	
	public static class internal {
		
		// Will have to change in the roll-out to just /res/
		public static final String RES_PATH = "res/res/";
		
		public static final String RES_UI_PATH = RES_PATH+"ui/";
		
		public static final String DATA_PATH = "data/";
		
		public static final File background = new File(RES_PATH+"ui/background_main_menu.png");

		public static final File logo = new File(RES_PATH+"ui/snake_logo_transparent.png");

		public static final File menuButtonSheet = new File(RES_PATH+"ui/buttons/mainMenuButtons.png");
		
	}

}
