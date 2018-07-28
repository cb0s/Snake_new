package snake;

import java.awt.Image;
import java.util.Random;

import utils.Maths;
import utils.Stack;
import utils.io.ImageLoader;
import utils.io.IniAdapter;

/**
 * @author Cedric
 * @version 1.0
 */
public class Snake {

	private static Snake instance = null;
	private IniAdapter snakeIni = null;
	private int size = 0;
	private Stack<SnakePart> snakeParts;
	private int eatenFruits = 0;
	private SnakePart head;
	private SnakePart tail;
	
	class SnakePart extends Entity {
		public SnakePart(Image defaultImage) {
			super(0, 0, defaultImage);
		}
		public void updateImage(Image newImage) {
			resource = newImage;
		}
		public Image getImage() {
			return resource;
		}
	}
	
	private Snake(int x, int y) {
		snakeParts = new Stack<>();
		head = new SnakePart(ImageLoader.getImage(snakeIni.getString(SnakeGame.snakeIniPath, "head")));
		tail = new SnakePart(ImageLoader.getImage(snakeIni.getString(SnakeGame.snakeIniPath, "tail")));
		instance = this;
		snakeIni = new IniAdapter();
		size = (int) Maths.format(snakeIni.getString(SnakeGame.snakeIniPath, "defaultSize"));
		for(int i = 0; i < size; i++)
			grow();
	}

	// TODO: Don't instant grow but on next run -> otherwise tail maybe hits border although it should not!
	private void grow() {
		snakeParts.add(new SnakePart(ImageLoader.getImage(snakeIni.getString(SnakeGame.snakeIniPath, "defaultPart" + new Random().nextInt(Integer.parseInt(snakeIni.getString(SnakeGame.snakeIniPath, "differentParts"))) + ".png"))));
	}
	
	public int getSize() {
		return size;
	}
	
	public int getEatenFruits() {
		return eatenFruits;
	}
	
	public void eatFruit(int fruitValue) {
		eatenFruits++;
		size += fruitValue;
		for(int i = 0; i < fruitValue; i++)
			grow();
	}
	
	public SnakePart[] getSnake() {
		SnakePart[] snakeReturn = new SnakePart[snakeParts.toArray().length + 2];
		snakeReturn[0] = head;
		Object[] snakeP = snakeParts.toArray();
		for(int i = 0; i < snakeP.length; i++)
			snakeReturn[i+1] = (SnakePart) snakeP[i];
		snakeReturn[snakeReturn.length-1] = tail;
		return snakeReturn;
	}
	
	public void setCorners(int x, int y) {
		
	}
	
	/**
	 * @param x
	 * @param y
	 * @return If no snake ever has been created, it returns a new Snake, else just the SnakeObject created before.
	 */
	public static Snake createSnake(int x, int y) {
		if (instance == null) new Snake(x, y);
		return instance;
	}
	
}
