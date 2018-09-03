package snake.oldUI;

import java.awt.Dimension;

import javax.swing.JFrame;

import utils.io.IniAdapter;
import utils.io.Logger;

public class ResizeListener {
	
	private static IniAdapter guiIni;
	
	static {
//		guiIni = new IniAdapter(SnakeGame.iniPath);
	}
	
	public ResizeListener(JFrame frame) {
		final int tick = Integer.parseInt(guiIni.getString("own_listener_tick"));
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
