package snake.fx;

import java.io.File;
import java.util.HashMap;

import javax.sound.sampled.AudioInputStream;

public class SoundManager {
	private static volatile long lastSoundID;
	
	private static HashMap<Long, AudioInputStream> activeAudio;
	
	static {
		lastSoundID = 0;
	}
	
	public static int playSound(File file) {
		if (!file.exists()) throw new IllegalArgumentException("The Audiofile " + file.getAbsolutePath() + " does not exist!");
		return -1;
	}
	
	public static boolean stopSound(int id) {
		if (activeAudio.containsKey(id)) {
			return true;
		} else return false;
	}
}
