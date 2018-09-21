package snake.ui;

import java.awt.Component;

import javax.swing.JOptionPane;

/**
 * Class for managing UI Dialogs.
 * 
 * @author Cedric, Leo
 */
public class DialogManager {

	/**
	 * 
	 * 
	 * @param exception
	 * @param msg The Error-Message where <i>%exception%</i> gets replaced by the message of the exception.
	 * @param title The title of the Error-Message
	 * @param exit Whether the program should exit after that log or not
	 * @return 
	 */
	public static String showExeptionDialog(Component parent, Throwable exception, String msg, String title, boolean exit) {
		String exMsg = exception.toString() + '\n';
		for (StackTraceElement ste : exception.getStackTrace()) {
			exMsg+="\tat "+ste.toString()+'\n';
		}
		JOptionPane.showMessageDialog(null, msg.replace("%exception%", exMsg), title, JOptionPane.ERROR_MESSAGE);
		if (exit) System.exit(1);
		return exMsg;
	}

	
}
