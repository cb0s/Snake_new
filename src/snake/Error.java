package snake;

import snake.io.Logger;

/**
 * 
 * @author Cedric
 * @version 1.0
 * @category main
 *
 */
public class Error {	
	public static String printError(Exception exception) {
		String msg = "";
		for (StackTraceElement ste : exception.getStackTrace()) {
			msg+=ste.toString()+'\n';
		}
		SnakeGame.log(Logger.LoggingType.ERROR.type + "Error-Message: " + msg);
		return msg;
	}
}
