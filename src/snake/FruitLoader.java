package snake;

import java.awt.Image;

import snake.io.ImageLoader;

public class FruitLoader {
	
	private static FruitLoader instance;
	
	private FruitLoader() {
		instance = this;
	}
	
	class Fruit extends Entity {

		private final String name;
		private final int despawnTime;	// in ticks
		private final int foodValue;	// how much the snake grows when eating
		
		public Fruit(int x, int y, String name, String imagePath, int despawnTime, int foodValue) {
			super(x, y, ImageLoader.getImage(imagePath));
			this.name = name;
			this.despawnTime = despawnTime;
			this.foodValue = foodValue;
		}
		
		public String getName() {
			return name;
		}
		
		public Image getImage() {
			return resource;
		}
		
		public int getDespawnTime() {
			return despawnTime;
		}
		
		public int getFoodValue() {
			return foodValue;
		}
		
	}
	
	public static Fruit getFruit(int x, int y, String name, String imagePath, int despawnTime, int foodValue) {
		if (instance == null) new FruitLoader();
		return instance.new Fruit(x, y, name, imagePath, despawnTime, foodValue);
	}
}
