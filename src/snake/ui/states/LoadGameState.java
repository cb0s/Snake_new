package snake.ui.states;

import java.awt.Graphics2D;
import snake.Game;
import snake.io.Files;
import snake.io.Resources;
import snake.mechanics.Clock;
import snake.mechanics.Clockable;
import snake.ui.Sprite;

public class LoadGameState extends State implements Clockable {
	
	Sprite backgroundImage, transparentSnakeLogo;
	Clock rotClock;
	
	
	

	public LoadGameState(Game parent) {
		super(parent);
	}
	
	
	
	
	@Override
	public synchronized void prepare() {
		super.prepare();
		backgroundImage = new Sprite(Resources.getImage(Files.internal.background, true));
		backgroundImage.setBounds(0, 0, parent.getConfig().width, parent.getConfig().height);
		transparentSnakeLogo = new Sprite(Resources.getImage(Files.internal.logo, true));
		transparentSnakeLogo.setBounds(parent.getConfig().width/2, parent.getConfig().height/2, 300, 300);
		transparentSnakeLogo.centerOrigin();
		rotClock = new Clock(this, 1.0f/120.0f);
	}
	
	@Override
	public synchronized void onApply() {
		super.onApply();
		rotClock.start();
	}
	
	@Override
	public synchronized void onDetach() {
		super.onDetach();
		rotClock.shutdown();
	}

	@Override
	public void render(Graphics2D g) {
		backgroundImage.render(g);
		transparentSnakeLogo.render(g);
	}

	@Override
	public void tick(float delta) {
		//half rotation per second
		transparentSnakeLogo.rotate(delta*180);
	}
	
}
