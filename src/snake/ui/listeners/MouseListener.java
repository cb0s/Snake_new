package snake.ui.listeners;

import java.awt.IllegalComponentStateException;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashSet;

import snake.io.Logger;
import snake.ui.Button;
import snake.ui.Button.buttonStates;
import snake.ui.WindowAdapter;

public class MouseListener implements java.awt.event.MouseMotionListener, java.awt.event.MouseListener  {
	
	private static HashSet<Button> buttons;
	private static HashSet<Integer> notUsedButton;
	
	static {
		buttons = new HashSet<Button>();
		notUsedButton = new HashSet<Integer>();
	}
	
	public static void addButton(Button b) {
		buttons.add(b);
	}
	
	public static void deleteButton(Button b) {
		buttons.remove(b);
		notUsedButton.remove(b.getID());
	}
	
	
	@Override
	public void mouseMoved(MouseEvent event) {
		// for Buttons
		ArrayList<Button> list = new ArrayList<Button>(buttons);
		Button button = null;
		for (int i = 0; i < buttons.size(); i++) {
			button = list.get(i);
			boolean bool = false;
			// Checks for buttons at the same place
			for (int j = i; j < buttons.size(); j++) {
				Button button2 = list.get(j);
				if ((button2.getX() > button.getX() && button2.getX() < button.getX() + button.getWidth() && button2.getY() > button.getY() && button2.getY() < button.getY()+button.getWidth() && button.getLayerIndex() < button2.getLayerIndex()) || (button2.getX()+button2.getWidth() > button.getX() && button2.getX()+button2.getWidth() < button.getX()+button.getWidth() && button2.getY()+button2.getHeight() > button.getY() && button2.getY()+button2.getHeight() < button.getY()+button.getHeight() && button.getLayerIndex() < button2.getLayerIndex()) || (button.getX() == button2.getX() && button.getY() == button2.getY() && button.getWidth() == button2.getWidth() && button.getHeight() == button2.getHeight() && button.getLayerIndex() < button2.getLayerIndex())) {
					bool = true;
					break;
				}
			}
			if (!bool) {
				try {
					int eX = event.getX(), eY = event.getY();
					int x = (int) (button.getLocationOnScreen().getX()-WindowAdapter.mainGetLocation().getX()), y = (int) (button.getLocationOnScreen().getY()-WindowAdapter.mainGetLocation().getY());
					if (((eX >= x && eX <= x+button.getSize().getWidth())) && (eY >= y && eY <= y+button.getSize().getHeight())) {
						if (button.getButtonState() != buttonStates.PRESSED.id)
							button.setButtonState(buttonStates.FOCUSSED.id);
					} else button.setButtonState(buttonStates.RELEASED.id);
					if (notUsedButton.contains(button.getID())) {
						notUsedButton.remove(button.getID());
						Logger.getDefaultLogger().logInfo("Button " + button.getID() + " with the label " + button.getLabel() + " is now used");
					}
				} catch(IllegalComponentStateException e) {
					if (!notUsedButton.contains(button.getID())) {
						Logger.getDefaultLogger().logWarning("Button " + button.getID() + " with the label \"" + button.getLabel() + "\" is not used");
						notUsedButton.add(button.getID());
					}
				}
			}
		}
	}
	
	@Override
	public void mouseDragged(MouseEvent event) {
		mouseMoved(event);
	}

	@Override
	public void mouseClicked(MouseEvent event) {
		
	}

	@Override
	public void mousePressed(MouseEvent event) {
		// for Buttons
		ArrayList<Button> list = new ArrayList<Button>(buttons);
		Button button = null;
		for (int i = 0; i < buttons.size(); i++) {
			button = list.get(i);
			boolean bool = false;
			// Checks for buttons at the same place
			for (int j = i; j < buttons.size(); j++) {
				Button button2 = list.get(j);
				if ((button2.getX() > button.getX() && button2.getX() < button.getX() + button.getWidth() && button2.getY() > button.getY() && button2.getY() < button.getY()+button.getWidth() && button.getLayerIndex() < button2.getLayerIndex()) || (button2.getX()+button2.getWidth() > button.getX() && button2.getX()+button2.getWidth() < button.getX()+button.getWidth() && button2.getY()+button2.getHeight() > button.getY() && button2.getY()+button2.getHeight() < button.getY()+button.getHeight() && button.getLayerIndex() < button2.getLayerIndex()) || (button.getX() == button2.getX() && button.getY() == button2.getY() && button.getWidth() == button2.getWidth() && button.getHeight() == button2.getHeight() && button.getLayerIndex() < button2.getLayerIndex())) {
					bool = true;
					break;
				}
			}
			if (!bool) {
				try {
					if (button.getButtonState() == buttonStates.FOCUSSED.id) {
						button.setButtonState(buttonStates.PRESSED.id);
						if (notUsedButton.contains(button.getID())) {
							notUsedButton.remove(button.getID());
							Logger.getDefaultLogger().logInfo("Button " + button.getID() + " with the label " + button.getLabel() + " is now used");
						}
					}
				} catch (IllegalStateException e) {
					if (!notUsedButton.contains(button.getID())) {
						Logger.getDefaultLogger().logWarning("Button " + button.getID() + " with the label \"" + button.getLabel() + "\" is not used");
						notUsedButton.add(button.getID());
					}
				}
			}
		}

	}

	@Override
	public void mouseReleased(MouseEvent event) {
		// For Buttons
		ArrayList<Button> list = new ArrayList<Button>(buttons);
		Button button = null;
		for (int i = 0; i < buttons.size(); i++) {
			button = list.get(i);
			boolean bool = false;
			// Checks for buttons at the same place
			for (int j = i; j < buttons.size(); j++) {
				Button button2 = list.get(j);
				if ((button2.getX() > button.getX() && button2.getX() < button.getX() + button.getWidth() && button2.getY() > button.getY() && button2.getY() < button.getY()+button.getWidth() && button.getLayerIndex() < button2.getLayerIndex()) || (button2.getX()+button2.getWidth() > button.getX() && button2.getX()+button2.getWidth() < button.getX()+button.getWidth() && button2.getY()+button2.getHeight() > button.getY() && button2.getY()+button2.getHeight() < button.getY()+button.getHeight() && button.getLayerIndex() < button2.getLayerIndex()) || (button.getX() == button2.getX() && button.getY() == button2.getY() && button.getWidth() == button2.getWidth() && button.getHeight() == button2.getHeight() && button.getLayerIndex() < button2.getLayerIndex())) {
					bool = true;
					break;
				}
			}
			if (!bool) {
				try {
					int eX = event.getX(), eY = event.getY();
					int x = (int) (button.getLocationOnScreen().getX()-WindowAdapter.mainGetLocation().getX()), y = (int) (button.getLocationOnScreen().getY()-WindowAdapter.mainGetLocation().getY());
					if (button.getButtonState() == buttonStates.PRESSED.id || button.getButtonState() == buttonStates.FOCUSSED.id) {
						if ((eX >= x && eX <= x+button.getSize().getWidth()) && (eY >= y && eY <= y+button.getSize().getHeight())) {
							button.setButtonState(buttonStates.FOCUSSED.id);
							button.fireActionEvent();
						}
						else button.setButtonState(buttonStates.RELEASED.id);
						if (notUsedButton.contains(button.getID())) {
							notUsedButton.remove(button.getID());
							Logger.getDefaultLogger().logInfo("Button " + button.getID() + " with the label " + button.getLabel() + " is now used");
						}
					}
				} catch (IllegalStateException e) {
					if (!notUsedButton.contains(button.getID())) {
						Logger.getDefaultLogger().logWarning("Button " + button.getID() + " with the label \"" + button.getLabel() + "\" is not used");
						notUsedButton.add(button.getID());
					}
				}
			}
		}
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		
	}
	
}
