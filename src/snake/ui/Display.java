package snake.ui;

import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.WindowAdapter;

import javax.swing.JFrame;

import utils.io.Logger;

public class Display {
	
	// ******************
	// * Private Fields *
	// ******************
	// Window-Related stuff
	private JFrame frame;
	private Canvas canvas;
	
	private String title;
	private int width, height;
	
	private WindowAdapter closeAdapter;
	
	// ****************
	// * Constructors *
	// ****************
	public Display(String title, int width, int height) {
		this.title = title;
		this.width = width;
		this.height = height;
	}
	
	public Display(String title, int width, int height, WindowAdapter closeAdapter) {
		this(title, width, height);
		this.closeAdapter = closeAdapter;
		
		createDisplay();
	}
	
	// *******************
	// * Private Methods *
	// *******************
	private void createDisplay() {
		Logger.getDefaultLogger().logInfo("Creating Display " + title);
		frame = new JFrame(title);
		canvas = new Canvas();
		
		setSize(width, height);
		if (closeAdapter == null) frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		else frame.addWindowListener(closeAdapter);
		frame.setResizable(false);
		
		frame.add(canvas);
		frame.pack();
	}
	
	// ******************
	// * Public Methods *
	// ******************
	public void center() {
		frame.setLocationRelativeTo(null);
	}
	
	// Setters
	public void setResizeable(boolean resizable) {
		frame.setResizable(resizable);
	}
	
	public void setLocation(int x, int y) {
		frame.setLocation(x, y);
	}
	
	public void setSize(int width, int height) {
		this.width = width;
		this.height = height;
		
		frame.setSize(width, height);
		
		canvas.setFocusable(false);
		
		Dimension d = new Dimension(width, height);
		canvas.setPreferredSize(d);
		canvas.setMaximumSize(d);
		canvas.setMinimumSize(d);
	}
	
	public void setTitle(String title) {
		this.title = title;
		frame.setTitle(title);
	}
	
	public void setVisible(boolean b) {
		frame.setVisible(b);
	}
	
	public void setIconImage(Image image) {
		frame.setIconImage(image);
	}
	
	// Getters
	public boolean isResizable() {
		return frame.isResizable();
	}
	
	public Point getLocation() {
		return frame.getLocation();
	}
	
	public Dimension getSize() {
		return frame.getSize();
	}
	
	public String getTitle() {
		return title;
	}
	
	public boolean isVisible() {
		return frame.isVisible();
	}
	
	public Canvas getCanvas() {
		return canvas;
	}
	
	public JFrame getFrame() {
		return frame;
	}
}
