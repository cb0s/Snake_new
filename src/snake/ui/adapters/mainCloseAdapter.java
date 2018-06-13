package snake.ui.adapters;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import snake.SnakeGame;
import snake.io.Logger;

/**
 * @author Cedric
 * @version 1.0
 * @category ui
 **/

public class mainCloseAdapter extends WindowAdapter {
	
	@Override
	public void windowClosing(WindowEvent event) {
		SnakeGame.log(Logger.LoggingType.INFO.type + "Main-Window-Closing is called");
		SnakeGame.stop();
	}
}
