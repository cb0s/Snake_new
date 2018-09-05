package utils.ui;

import java.awt.event.MouseEvent;

import snake.State;
import snake.ui.tiles.Button;

public class MouseManager implements java.awt.event.MouseListener, java.awt.event.MouseMotionListener {
	
	@Override
	public void mouseClicked(MouseEvent e) {
	}

	@Override
	public void mousePressed(MouseEvent e) {
		int x = e.getX(), y = e.getY();
		for (Button b : State.getState().getButtons()) {
			if (checkAllignment(x, y, b)) {
				if (!b.isPressed())
					b.press();
				continue;
			}
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		int x = e.getX(), y = e.getY();
		for (Button b : State.getState().getButtons()) {
			if (checkAllignment(x, y, b)) {
				b.fireClickEvent(e);
			}
			if (b.isPressed()) {
				b.unpress();
			}
		}
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		mouseMoved(e);
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		int x = e.getX(), y = e.getY();
		for (Button b : State.getState().getButtons()) {
			if (checkAllignment(x, y, b)) {
				if (!b.isFocussed())
					b.focus();
				continue;
			}
			b.unfocus();
		}
	}
	
	private boolean checkAllignment(int x, int y, Button b) {
		if (b.getX() < x && b.getX() + b.getWidth() > x)
			if (b.getY() < y && b.getY() + b.getHeight() > y)
				return true;
		return false;
	}

}
