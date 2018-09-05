package snake.oldUI;

import utils.Maths;
import utils.io.ConfigAdapter;
import utils.io.Logger;

/**
 * 
 * @author Cedric
 * @version 1.0
 * @category ui
 * 
 */

public class GameClock {
	
	private static boolean running;
	private static int tick;
//	private static int ups = 60;
	private static int fps = 60;
	private static Logger renderLog;
	
	static {
		running = true;
//		ups = Integer.parseInt(ConfigAdapter.getConfigString("max_updates"));
		fps = Integer.parseInt(ConfigAdapter.getDefaultConfig().getConfigString("max_frames"));
		tick = (int) Maths.format(ConfigAdapter.getDefaultConfig().getConfigString("renderlog_tick").replaceAll("%frames%", ""+fps));
		// TODO: Fix ini Access
//		renderLog = new Logger(Boolean.parseBoolean(ConfigAdapter.getDefaultConfig().getConfigString("logging")) ? new File(Logger.ini.getString("path").replace("*", "render.log")) : null);
//		renderLog.logPlain("----Render-Logger--initialized----");
	}
	
	static Logger getRenderLogger() {
		return renderLog;
	}
	
	// 
	public static void start() {
		Thread gameThread = new Thread(new Runnable() {
			@Override
			public void run () {
				long lastTime = System.nanoTime();
				long curTime = 0;
				long diff = 0;
				
//				double ns = 1000000000 / ups;
				double dfps = 1000000000 / fps;
//				double delta = 0.0;
				double d = 0.0;
				
				long timer = System.currentTimeMillis();
				long lastPrint = System.currentTimeMillis();
				int ups = 0;
				int fps = 0;
				
				while(running) {
					curTime = System.nanoTime();
					diff = curTime - lastTime; 
//					delta += diff / ns;
					d += diff / dfps;
					lastTime = curTime;
//					
//					while(delta >= 1.0) {
//						input();
//						update();
//						ups++;
//						delta--;
//					}
					
					if (d >= 1.0) {
						WindowAdapter.mainUpdate();
						WindowAdapter.updateAllFrames();
						fps++;
						d = 0.0;
					}
					
					if(System.currentTimeMillis() > timer + 1000) {
						if ((System.currentTimeMillis()-lastPrint)%(100*tick) < 50) {
							renderLog.logInfo("Ups: " + ups + "    |    Fps: " + fps);
						}
						ups = 0;
						fps = 0;
						timer += 1000;
					}
				}
			}
		});
		gameThread.start();
	}
	
	public static void stop() {
		running = false;
	}
	
	public static boolean isRunning() {
		return running;
	}
	
}
