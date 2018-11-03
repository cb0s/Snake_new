package snake.ui.entity;

import java.awt.Point;
import java.util.Random;

import snake.Game;
import snake.io.Logger;
import snake.ui.tiles.Field;
import utils.io.Vector2D;

public abstract class SpawnManager {
	
	private Vector2D<Class<Entity>, Integer>[] spawnClasses;
	
	private final Random spawnRandom;
	private final Field field;
	
	public static final int SPAWN_ATTEMPTS;
	
	static {
		SPAWN_ATTEMPTS = Integer.parseInt(Game.GAME_INI.getString("spawn_attempts"));
	}
	
	public SpawnManager(Field field, Vector2D<Class<Entity>, Integer>[] spawnClasses) {
		this.spawnClasses = spawnClasses;
		this.field = field;
		spawnRandom = new Random();
	}
	
	private boolean checkBombLocation(int x, int y) {
		for (int x1 = -1; x1 < 2; x1++)
			for (int y1 = -1; y1 < 2; y1++)
				if (field.getEntityOnField(x+x1, y+y1) != null) return false;
		return true;
	}
	
	private Point getSpawnPoint() {
		return new Point(spawnRandom.nextInt()%field.getRows(), spawnRandom.nextInt()% field.getCollums());
	}
	
	private boolean checkSpawnPoint(Point p, Entity entity) {
		int x = p.x, y = p.y;
		
		if (entity instanceof Bomb) {
			if (!checkBombLocation(x, y)) {
				return false;
			}
		} else if (field.getEntityOnField(x, y) != null) {
			return false;
		}
		return true;
	}
	
	protected void manageSpawns() {
		int entNum = spawnRandom.nextInt()%spawnClasses.length;
		Class<Entity> ent = spawnClasses[entNum].data1;
		
		if (spawnRandom.nextInt()%spawnClasses[entNum].data2 != 0) return;
		
		Entity entity = Entity.produceNewEntity(ent);
		
		Point p = getSpawnPoint();
		for (int i = 0; i < SPAWN_ATTEMPTS && !checkSpawnPoint(p, entity); i++)
			p = getSpawnPoint();
		if (checkSpawnPoint(p, entity)) {
			Logger.gdL().logWarning("Could not spawn new Entity");
			return;
		}
		entity.setLocation(p.x , p.y);
		
		spawn(entity);
	}
	
	public Vector2D<Class<Entity>, Integer>[] getSpawnClasses() {
		return spawnClasses;
	}
	
	public void setSpawnClasses(Vector2D<Class<Entity>, Integer>[] spawnClasses) {
		this.spawnClasses = spawnClasses;
	}
	
	public abstract void spawn(Entity entity);
}
