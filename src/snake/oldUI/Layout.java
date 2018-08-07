package snake.oldUI;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Point;
import java.util.LinkedList;

import javax.swing.JComponent;

import utils.ui.Button;

/**
 * 
 * @author Cedric
 * @version 1.0
 * @category ui
 * 
 */

public class Layout {
	
	private Dimension frameSize;
	private LinkedList<Component[]> layers;
	
	public Layout(Dimension frameSize) {
		layers = new LinkedList<Component[]>();
		this.frameSize = frameSize;
	}
	
	public void add(Component[] layer) {
		for (int i = 0; i < layer.length; i++) if (layer[i] instanceof Button) ((Button)layer[i]).setLayerIndex(layers.size());
		layers.add(layer);
	}
	
	public void add(Component[] layer, int i) {
		for (int j = 0; j < layer.length; j++) if (layer[j] instanceof Button) ((Button)layer[j]).setLayerIndex(i);
		layers.add(i, layer);
	}
	
	public Component[] getLayer(int index) {
		return layers.get(index);
	}
	
	public int getLayerAmount() {
		return layers.size();
	}
	
	public Point[] getDimension(int index) {
		int x0=-1,x1=-1;
		int y0=-1,y1=-1;
		for (JComponent j : (JComponent[]) layers.toArray()) {
			if (j.getLocation().getX() < x0 || x0 == -1) x0 = (int) j.getLocation().getX();
			if (j.getLocation().getY() < y0 || y0 == -1) y0 = (int) j.getLocation().getY();
			if (j.getLocation().getX()+j.getSize().getWidth() > x1) x1 = (int) (j.getLocation().getX()+j.getSize().getWidth());
			if (j.getLocation().getY()+j.getSize().getHeight() > y1) y1 = (int) (j.getLocation().getY()+j.getSize().getHeight());
		}
		return new Point[] {new Point(x0, y0), new Point(x1, y1)};
	}
	
	public void updateDimension(Dimension d) {
		float changeX = frameSize.width / d.width;
		float changeY = frameSize.height / d.height;
		for (int i = 0; i < layers.size(); i++) {
			Component[] cs = new Component[layers.get(i).length];
			for (int j = 0; j < cs.length; j++) {
				Component c = layers.get(i)[j];
				if (c instanceof Button) {
					if (!((Button)c).resizeable()) continue;
					else ((Button)c).setFont(new Font(((Button)c).getFont().getName(), ((Button)c).getFont().getStyle(), (int)(((Button)c).getFont().getSize()/((double)c.getSize().height / changeY))));
				}
				c.setSize((int) (c.getSize().width / changeX), (int) (c.getSize().height / changeY));
				c.setLocation((int) (c.getLocation().x / changeX), (int) (c.getLocation().y / changeY));
				cs[j] = c;
				GameClock.getRenderLogger().logInfo("Component " + j + " of Layer " + i + " was changed");
			}
			layers.set(i, cs);
		}
		frameSize = d;
	}
	
	public void remove(Component[] layer) {
		layers.remove(layer);
	}
	
}
