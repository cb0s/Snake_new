package snake.ui.entity;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;

import snake.io.Files;
import snake.io.Resources;
import snake.ui.Sprite;
import snake.ui.entity.fruits.Apple;
import snake.ui.entity.fruits.Banana;
import snake.ui.entity.fruits.Cherry;
import snake.ui.entity.fruits.Melon;
import snake.ui.entity.fruits.Orange;
import snake.ui.entity.fruits.Pear;
import snake.ui.entity.fruits.Pineapple;
import snake.ui.entity.fruits.Strawberry;

@SuppressWarnings("serial")
public abstract class Entity extends Sprite {
	
	/**
	 * Whether an Entity is on the Field or not
	 */
	private boolean spawned;
	/**
	 * Position on the field
	 */
	private Point pos;
	protected BufferedImage currentImage;
	
	/**
	 * This constructor is not implemented yet!
	 * 
	 * @param resource
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 * @throws NoSuchMethodError
	 */
	public Entity(BufferedImage resource, float x, float y, float width, float height) throws NoSuchMethodError{
		super(resource);
		currentImage = resource;
		throw new NoSuchMethodError("This Constructor is not implemented yet.");
	}
	
	public Entity(BufferedImage resource, int x, int y) {
		super(resource, x, y, resource.getWidth(), resource.getHeight());
	}
	
	public Entity(File file, int x, int y, int width, int height) {
		super(Resources.getImage(file, true), x, y, width, height);
	}
	
	public Entity(BufferedImage resource, int x, int y, int width, int height) {
		super(resource, x, y, width, height);
	}
	
	protected void setCurrentImage(BufferedImage image) {
		this.currentImage = image;
	}
	
	public Point getPos() {
		return pos;
	}
	
	public Point updatePos(Point pos) {
		Point p = this.pos;
		this.pos = pos;
		return p;
	}
	
	public boolean isSpawned() {
		return spawned;
	}
	
	public void spawn(Point pos) {
		this.pos = pos;
		spawned = true;
		onSpawn();
	}
	
	protected abstract void onSpawn();
	
	public void despawn() {
		spawned = false;
		onDespawn();
	}
	
	protected abstract void onDespawn();
	
	public abstract void update();

	public void render(Graphics2D g) {
		g.drawImage(currentImage, x, y, x + width, y + height, 0, 0, currentImage.getWidth(null), currentImage.getHeight(null), null);
	}

	public static Entity produceNewEntity(Class<Entity> entity) {
		String name = entity.getSimpleName();
		Entity returnEntity = null;
		switch (name) {
		case "Bomb":
			BufferedImage bombNormal = Resources.getImage(new File(Files.internal.RES_UI_PATH + "entities/bombNormal.png"), true);
			BufferedImage bombExploding = Resources.getImage(new File(Files.internal.RES_UI_PATH + "entities/bombExploding.png"), true);
			// TODO: Load this from config
			int tickTime = Bomb.DEFAULT_TICKTIME;
			returnEntity = new Bomb(bombNormal, bombExploding, 0, 0, tickTime);
			break;
		case "Apple":
			returnEntity = new Apple(Resources.getImage(new File(Files.internal.RES_UI_PATH + "entities/apple.png"), true), 0, 0);
			break;
		case "Banana":
			returnEntity = new Banana(Resources.getImage(new File(Files.internal.RES_UI_PATH + "entities/banana.png"), true), 0, 0);
			break;
		case "Cherry":
			returnEntity = new Cherry(Resources.getImage(new File(Files.internal.RES_UI_PATH + "entities/cherry.png"), true), 0, 0);
			break;
		case "Melon":
			returnEntity = new Melon(Resources.getImage(new File(Files.internal.RES_UI_PATH + "entities/melon.png"), true), 0, 0);
			break;
		case "Orange":
			returnEntity = new Orange(Resources.getImage(new File(Files.internal.RES_UI_PATH + "entities/orange.png"), true), 0, 0);
			break;
		case "Pear":
			returnEntity = new Pear(Resources.getImage(new File(Files.internal.RES_UI_PATH + "entities/pear.png"), true), 0, 0);
			break;
		case "Pineapple":
			returnEntity = new Pineapple(Resources.getImage(new File(Files.internal.RES_UI_PATH + "entities/pineapple.png"), true), 0, 0);
			break;
		case "Strawberry":
			returnEntity = new Strawberry(Resources.getImage(new File(Files.internal.RES_UI_PATH + "entities/strawberry.png"), true), 0, 0);
			break;
		case "SnakeHead":
			// Usually you don't need more than one Snake-Head so this is not saved in the buffer
			returnEntity = new SnakeHead(Resources.getImage(new File(Files.internal.RES_UI_PATH + "entities/snake_head.png")), 0, 0);
			break;
		case "SnakeTailPart":
			returnEntity = new SnakeTailPart(Resources.getImage(new File(Files.internal.RES_UI_PATH + "entities/snake_tailpart.png"), true), 0, 0);
			break;
		default:
			throw new IllegalArgumentException("The given entity is either not implemented or does not part of the Entity-Factory");
		}
		return returnEntity;
	}
	
}
