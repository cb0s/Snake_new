package snake;

public class ObjectLoader {
	
	private static ObjectLoader instance;
	
	private ObjectLoader() {
		instance = this;
	}
	
	public class Bomb extends Entity {

		public Bomb(int x, int y) {
			super(x, y, null);
		}
		
		public void getName() {
			
		}
		
	}
	
	public class VulcanEruptionParticle extends Entity {

		public VulcanEruptionParticle(int x, int y) {
			super(x, y, null);
		}
		
	}
	
	public static Bomb getBomb(int x, int y) {
		if(instance == null) new ObjectLoader();
		return instance.new Bomb(x, y);
	}
	
	public static VulcanEruptionParticle getVulcanEruptionParticle(int x, int y) {
		if(instance == null) new ObjectLoader();
		return instance.new VulcanEruptionParticle(x, y);
	}
	
}
