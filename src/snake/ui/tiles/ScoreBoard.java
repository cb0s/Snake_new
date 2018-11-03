package snake.ui.tiles;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import snake.mechanics.Clock;
import snake.mechanics.Clockable;

@SuppressWarnings("serial")
public class ScoreBoard extends Tile implements Clockable {

	private int score, level;
	private volatile long passedTime;
	private final Clock timeClock;
	private final int MAX_LEVEL;
	
	public ScoreBoard(BufferedImage background, int x, int y, int widht, int height, int maxLevel) {
		super(background, x, y, widht, height);
		
		this.MAX_LEVEL = maxLevel;
		
		score = 0;
		level = 0;
		
		timeClock = new Clock(this, 1);
		timeClock.start();
	}
	
	private void drawScorePart(Graphics2D g, Color c, String attribute, Color c2, String value, int y) {
		int resourceW = resource.getWidth();
		
		g.setColor(c);
		int scoreW = g.getFontMetrics().stringWidth(attribute + " " + value);
		g.drawString(attribute, x+(resourceW-scoreW)/2, y);
		g.setColor(c2);
		g.drawString(value, x+(resourceW-scoreW)/2+g.getFontMetrics().stringWidth(attribute + " "), y);		
	}

	private String formatTime() {
		LocalTime time = LocalTime.ofSecondOfDay(passedTime);
		return DateTimeFormatter.ofPattern(time.getHour() > 0 ? "HH:mm:ss" : "mm:ss").format(time);
	}
	
	public void stopTime() {
		timeClock.shutdown();
	}
	
	public void resumeTime() {
		timeClock.start();
	}
	
	public void incrementScore() {
		incrementScore(1);
	}
	
	public void incrementScore(int amount) {
		score += amount;
	}
	
	public void incrementLevel() {
		level++;
	}
	
	public int getScore() {
		return score;
	}
	
	public int getLevel() {
		return level;
	}
	
	public long getPassedTime() {
		return passedTime;
	}
	
	@Override
	public void render(Graphics2D g) {
		// TODO: Change Y
		super.render(g);
		
		g.drawImage(resource, x, y, null);
		
		int fontSize = (int) ((y+getHeight())/4);
		
		g.setFont(new Font("Time New Roman", Font.BOLD, fontSize));
		
		Color black = new Color(0, 0, 0);
		Color green = new Color(0, 255, 0);
		
		drawScorePart(g, black, "Score:", green, score+"", y+20+fontSize/4);
		drawScorePart(g, black, "Time:", green, formatTime(), y+20+fontSize/2+g.getFontMetrics().getHeight());
		if (level < MAX_LEVEL)
			drawScorePart(g, black, "Level", green, level+"/"+MAX_LEVEL, y+20+fontSize/4*3+g.getFontMetrics().getHeight()*2);
		else
			g.drawString("Infinite Mode", g.getFontMetrics().stringWidth("Infinite Mode"), y+20+fontSize/4*3+g.getFontMetrics().getHeight()*2);
		
	}

	@Override
	public void tick(float delta) {
		passedTime++;
	}

}
