package snake.oldUI;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Shape;

import javax.swing.JPanel;

/**
 * @author Cedric
 * @version 1.0
 * @category ui
 * 
 * A JPanel which can contain any shapes as background in a set opacity while the background will be transparent
 */

public class ClearPanel extends JPanel {
	private static final long serialVersionUID = 706731555521208974L;

	private Shape shape = null;
	private Color color = null;
	
	public ClearPanel() {
		setOpaque(false);
	}
	
	public ClearPanel(Color color) {
		this();
		setColor(color);
	}
	
	public ClearPanel(Shape shape, Color color) {
		this();
		setShape(shape);
		setColor(color);
	}
	
	public Shape getShape() {
		return shape;
	}

	public Color getColor() {
		return color;
	}
	
	public void setShape(Shape shape) {
		this.shape = shape;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	
	@Override
	public void paintComponent(Graphics go) {
		Graphics2D g = (Graphics2D) go;
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		if(shape == null) shape = g.getClipBounds();
		g.setColor(color);
		g.fill(shape);
		super.paintComponent(g);
	}
	
}
