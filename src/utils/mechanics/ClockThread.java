/**
 * 
 */
package utils.mechanics;

/**
 * Internal thread class for periodically calling a {@link Clockable}.
 * 
 * @author Leo
 * @version 1.1
 * @category engine.mechanics
 */
@SuppressWarnings("unused")
class ClockThread extends Thread {

	// *************
	// * Constants *
	// *************
	private static final float NANO_CONVERSION_FACTOR = 1000000000.0f;




	// **********
	// * Fields *
	// **********
	
	//What will be called periodically
	private final Clockable target;
	
	//How much time should be betweet calls
	private final long periodAsNanos;
	
	//Whether the execution is continously going
	private volatile boolean running;
	



	// ****************
	// * Constructors *
	// ****************
	public ClockThread(Clockable target, float period) {
		super();
		this.target = target;
		this.periodAsNanos = (long) (period * NANO_CONVERSION_FACTOR);
		this.running = false;
	}
	
	//Make unwanted constructors invisible
	private ClockThread() {this(null, 0);}
	private ClockThread(Runnable target) {this(null, 0);}
	private ClockThread(String name) {this(null, 0);}
	private ClockThread(ThreadGroup group, Runnable target) {this(null, 0);}
	private ClockThread(ThreadGroup group, String name) {this(null, 0);}
	private ClockThread(Runnable target, String name) {this(null, 0);}
	private ClockThread(ThreadGroup group, Runnable target, String name) {this(null, 0);}
	private ClockThread(ThreadGroup group, Runnable target, String name, long stackSize) {this(null, 0);}
	private ClockThread(ThreadGroup group, Runnable target, String name, long stackSize, boolean inheritThreadLocals) {this(null, 0);}




	// ******************
	// * Public methods *
	// ******************
	@Override
	public void run() {
		running = true;
		long currentTime, remainingTime;
		long lastTime = System.nanoTime();
		while(running) {
			currentTime = System.nanoTime();
			target.tick( ((float)(currentTime - lastTime)) / NANO_CONVERSION_FACTOR);
			remainingTime = periodAsNanos - (System.nanoTime() - lastTime);
			if(running && remainingTime > 0) {
				try {
					Thread.sleep(remainingTime / 1000000, (int)(remainingTime%1000000));
				} catch (InterruptedException e) {
					e.printStackTrace();
					running = false;
				}
			}
			lastTime = currentTime;
		}
		running = false;
	}
	
	public synchronized void start() {
		super.start();
		running = true;
	}
	
	public synchronized void shutdown() {
		running = false;
	}
	
	public synchronized boolean isRunning() {
		return running;
	}
	
}
