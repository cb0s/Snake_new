package utils.mechanics;

/**
 * A clock periodically calls a {@link Clockable} with a preferably fixed frequency and keeps track of the actual current frequency.
 * 
 * @author Leo, Cedric
 * @version 1.3
 * @category engine.mechanics
 */
@SuppressWarnings("unused")
public class Clock extends Thread implements Clockable {

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
	/**
	 * Creates a new clock, that will run with the given periodic time (in seconds) between ticks, using itself as the {@link Clockable}.
	 * 
	 * @param period the periodic time between ticks in seconds
	 * 
	 * @throws IllegalArgumentException if the given periodic time is negative
	 */
	public Clock(float period) {
		this(null, period);
	}

	/**
	 * Creates a new clock, that will run with the given periodic time (in seconds) between ticks, for the given {@link Clockable} (or itself if clockable is null).
	 * 
	 * @param clockable the clockable to call periodically
	 * @param period the periodic time between ticks in seconds
	 * 
	 * @throws IllegalArgumentException if the given periodic time is negative
	 */
	public Clock(Clockable clockable, float period) {
		super();
		if(period < 0) {
			throw new IllegalArgumentException("Periodic time less than 0.");
		}
		this.target = (clockable!=null) ? clockable : this;
		this.periodAsNanos = (long) (period * NANO_CONVERSION_FACTOR);
		this.running = false;
	}

	
	//Make unwanted Thread constructors invisible
	private Clock() {this(null, 0);}
	private Clock(Runnable target) {this(null, 0);}
	private Clock(String name) {this(null, 0);}
	private Clock(ThreadGroup group, Runnable target) {this(null, 0);}
	private Clock(ThreadGroup group, String name) {this(null, 0);}
	private Clock(Runnable target, String name) {this(null, 0);}
	private Clock(ThreadGroup group, Runnable target, String name) {this(null, 0);}
	private Clock(ThreadGroup group, Runnable target, String name, long stackSize) {this(null, 0);}
	private Clock(ThreadGroup group, Runnable target, String name, long stackSize, boolean inheritThreadLocals) {this(null, 0);}




	// ******************
	// * Public methods *
	// ******************
	/**
	 * Method that executes the tick-loop.
	 * 
	 * @see java.lang.Thread#run()
	 */
	@Override
	public final void run() {
		running = true;
		long timeAtLoopStart, remainingTimeAfterTick;
		long lastTimeAtLoopStart = System.nanoTime();
		while(running) {
			timeAtLoopStart = System.nanoTime();
			target.tick( ((float)(timeAtLoopStart - lastTimeAtLoopStart)) / NANO_CONVERSION_FACTOR);
			remainingTimeAfterTick = periodAsNanos - (System.nanoTime() - timeAtLoopStart);
			if(running && remainingTimeAfterTick > 0) {
				try {
					Thread.sleep(remainingTimeAfterTick / 1000000, (int)(remainingTimeAfterTick%1000000));
				} catch (InterruptedException e) {
					e.printStackTrace();
					running = false;
				}
			}
			lastTimeAtLoopStart = timeAtLoopStart;
		}
		running = false;
	}
	
	/**
	 * Starts this clock.<br>
	 * Calling this on a clock, that has been started before, will cause an exception.
	 */
	public synchronized void start() {
		super.start();
		running = true;
	}

	/**
	 * Shuts down the clock without waiting until has stopped. Calling this on a stopped clock has no effect.<br>
	 * Note that {@link #isRunning()} will return false, even if the execution of the current tick has not finished yet.
	 */
	public synchronized void shutdown() {
		running = false;
	}

	/**
	 * Returns whether the clock is running.
	 * 
	 * @return whether the clock is running
	 */
	public synchronized boolean isRunning() {
		return running;
	}

    /**
     * If this Clock was constructed using a separate
     * <code>Clockable</code> run object, then that
     * <code>Clockable</code> object's <code>tick</code> method is called;
     * otherwise, this method does nothing and returns.
     * <p>
     * Subclasses of <code>Clock</code> should override this method.
     *
     * @see     #start()
     * @see     #shutdown()
     * @see		#Clock(Clockable, float)
     */
	@Override
	public void tick(float delta) {
		//Override in subclass.
	}

}
