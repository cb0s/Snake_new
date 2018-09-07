package utils.mechanics;

/**
 * Internal runnable class for periodically calling a {@link Clockable}.
 * 
 * @author Leo
 * @version 1.2
 * @category engine.mechanics
 */
class ClockWorker implements Runnable {

	// *************
	// * Constants *
	// *************
	private static final int NANO_CONVERSION_FACTOR = 1000000000;




	// **********
	// * Fields *
	// **********
	private final Clockable clockable;
	/**
	 * The Clock's periodic time in nano seconds.
	 */
	private final long periodAsNanos;

	private volatile boolean running;
	private long lastTime, currentTime, remainingTime;




	// ****************
	// * Constructors *
	// ****************
	public ClockWorker(Clockable clockable, float period) {
		this.clockable = clockable;
		this.periodAsNanos = (long) (period * NANO_CONVERSION_FACTOR);

		this.lastTime = -1;
	}




	// ******************
	// * Public methods *
	// ******************
	@Override
	public void run() {
		running = true;
		if(lastTime == -1)
			lastTime = System.nanoTime();
		while(running) {
			currentTime = System.nanoTime();
			clockable.tick( (currentTime - lastTime) / (float)NANO_CONVERSION_FACTOR );
			remainingTime = periodAsNanos - (System.nanoTime() - lastTime);
			if(running && remainingTime > 0) {
				try {
					Thread.sleep(remainingTime/1000000, (int)(remainingTime%1000000));
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			lastTime = currentTime;
		}
		running = false;
	}
	
	public synchronized boolean isRunning() {
		return running;
	}
	
	public synchronized void shutdown() {
		running = false;
	}
}