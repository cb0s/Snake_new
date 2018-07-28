package snake.ui.adapters;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import snake.SnakeGame;
import utils.io.Logger;

/**
 * @author Cedric
 * @version 1.0
 * @category ui
 **/

public class mainCloseAdapter extends WindowAdapter {
	
	@Override
	public void windowClosing(WindowEvent event) {
		Logger.getDefaultLogger().logInfo("Main-Window-Closing is called");
		SnakeGame.stop();
	}
}
