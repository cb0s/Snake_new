package snake.ui.listeners;

import java.awt.Dimension;

import javax.swing.JFrame;

import snake.SnakeGame;
import snake.io.IniAdapter;
import snake.io.Logger;
import snake.ui.GameClock;
import snake.ui.UIManager;

public class ResizeListener {
	
	private static IniAdapter guiIni;
	
	static {
		guiIni = new IniAdapter();
	}
	
	public ResizeListener(JFrame frame) {
		final int tick = Integer.parseInt(guiIni.getString(SnakeGame.iniPath, "own_listener_tick"));
		Thread t = new Thread(new Runnable() {
			Dimension size;
			@Override
			public void run () {
				while(GameClock.isRunning()) {
					size = frame.getSize();
					try { Thread.sleep(tick); } catch (InterruptedException e) { Logger.getDefaultLogger().logException(e); }
					if (!frame.getSize().equals(size)) UIManager.updateLayout();
				}
			}
		});
		t.start();
	}
	
}
