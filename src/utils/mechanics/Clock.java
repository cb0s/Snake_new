package utils.mechanics;

import java.util.LinkedList;

import utils.io.Logger;

/**
 * A clock periodically calls a {@link Clockable} with a preferably fixed frequency and keeps track of the actual current frequency.
 * 
 * @author Leo, Cedric
 * @version 1.2
 * @category engine.mechanics
 */
public class Clock implements Clockable {

	// **********
	// * Fields *
	// **********
	private final Clockable clockable;
	/**
	 * The Clock's periodic time in nano seconds.
	 */
	private final long period;
	private volatile Thread clockThread;
	private volatile boolean running, paused;
	private volatile float currentFrequency;
	private static LinkedList<Clock> runningClocks;


	static {
		runningClocks = new LinkedList<Clock>();
	}
	
	

	// ****************
	// * Constructors *
	// ****************
	/**
	 * Creates a new clock, that will run with the given frequency for ticks, using itself as the {@link Clockable}.
	 * 
	 * @param frequency the frequency for ticks
	 * @throws IllegalArgumentException if the given frequency is negative
	 */
	public Clock(float frequency) {
		this((long) (1000000000/frequency));
	}
	/**
	 * Creates a new clock, that will run with the given periodic time (in nano seconds) between ticks, using itself as the {@link Clockable}.
	 * 
	 * @param period the periodic time between ticks in nano seconds
	 * @throws IllegalArgumentException if the given periodic time is negative
	 */
	public Clock(long period) {
		if(period < 0) {
			throw new IllegalArgumentException("Periodic time and frequency less than 0.");
		}
		this.clockable = this;
		this.period = period;
		running = false;
		paused = false;
	}


	/**
	 * Creates a new clock, that will run with the given frequency for ticks, for the given {@link Clockable}.
	 * 
	 * @param clockable the clockable to call periodically
	 * @param frequency the frequency for ticks
	 * @throws NullPointerException if the given clockable is null
	 * @throws IllegalArgumentException if the given frequency is negative
	 */
	public Clock(Clockable clockable, float frequency) {
		this(clockable, (long) (1000000000/frequency));
	}
	/**
	 * Creates a new clock, that will run with the given periodic time (in nano seconds) between ticks, for the given {@link Clockable}.
	 * 
	 * @param clockable the clockable to call periodically
	 * @param period the periodic time between ticks in nano seconds
	 * @throws NullPointerException if the given clockable is null
	 * @throws IllegalArgumentException if the given periodic time is negative
	 */
	public Clock(Clockable clockable, long period) {
		if(clockable == null) {
			throw new NullPointerException("Clockable argument is null.");
		}
		if(period < 0) {
			throw new IllegalArgumentException("Periodic time and frequency less than 0.");
		}
		this.clockable = clockable;
		this.period = period;
		running = false;
	}




	// ******************
	// * Public methods *
	// ******************
	/**
	 * Starts or restarts this clock. Calling this on a running clock has no effect.
	 */
	public synchronized void start() {
		if(!running) {
			running = true;
			
			if (paused) paused = false;

			clockThread = new Thread(new Runnable() {
				@Override
				public void run() {

					long lastTime = System.nanoTime();
					long currentTime, remainingTime;
					while(running) {
						if (paused) {
							try {
								wait();
							} catch (InterruptedException e) {
								Logger.getDefaultLogger().logError("A Clock-Error occured!");
								Logger.getDefaultLogger().logException(e);
							}
							paused = false;
						}
						currentTime = System.nanoTime();
						clockable.tick(currentTime - lastTime);
						currentFrequency = ((float)1000000000)/((float)currentTime-lastTime);
						remainingTime = period - (System.nanoTime() - lastTime);
						if(remainingTime > 0) {
							try {
								Thread.sleep(remainingTime/1000000, (int)remainingTime%1000000);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
						}
						lastTime = currentTime;
					}
				}
			});
			clockThread.start();
			runningClocks.add(this);
		}
	}

	/**
	 * Pauses the clock. This has no effect on an already paused clock.
	 * @throws InterruptedException 
	 */
	public synchronized void pause() {
		paused = true;
	}
	
	/**
	 * Resumes the clock. This has no effect on a running clock.
	 */
	public synchronized void resume() {
		notify();
	}
	
	/**
	 * Shuts down the clock and waits until it has stopped. Calling this on a stopped clock has no effect.
	 * 
	 * @throws InterruptedException
	 */
	public synchronized void stop() throws InterruptedException {
		if(running) {
			shutdown();
			if (clockThread.isAlive())
				clockThread.join();
		}
	}
	
	/**
	 * Shuts down any running clock and waits until they have been stopped. Calling this with no running Clock has no effect.
	 * 
	 * @throws InterruptedException
	 */
	public static void stopAll() throws InterruptedException {
		Logger.gdL().logInfo("Stopping all running Clocks");
		if (runningClocks.size() > 0) for (Clock c : runningClocks) c.stop();
	}
	
	/**
	 * Shuts down the clock without waiting until has stopped. Calling this on a stopped clock has no effect.
	 */
	public synchronized void shutdown() {
		Logger.getDefaultLogger().log("Shutting down Clock with " + currentFrequency);
		running = false;
		if (paused) resume();
		runningClocks.remove(this);
	}
	
	/**
	 * Shuts down all the running clocks without waiting until they have stopped. Calling this with no active clock has no effect.
	 */
	public static void shutdownAll() {
		Logger.gdL().logInfo("Shutting down all running Clocks");
		if (runningClocks.size() > 0) for (Clock c : runningClocks) c.shutdown();
	}
	
	/**
	 * Returns whether the clock is running.
	 * 
	 * @return whether the clock is running
	 */
	public boolean isRunning() {
		return running;
	}

	public boolean isPaused() {
		return paused;
	}
	
	/**
	 * Returns the current frequency based on the last tick delta or 0 if the clock is not running.
	 * 
	 * @return the current frequency
	 */
	public float getCurrentFrequency() {
		if(running) {
			return currentFrequency;
		} else {
			return 0;
		}
	}
	
	/**
	 * Returns an Array of all running Clocks.
	 * 
	 * @return Array of all running Clocks
	 */
	public static Clock[] getRunningClocks() {
		return (Clock[]) runningClocks.toArray();
	}
	
	@Override
	public void tick(long delta) {
		//Override in subclass.
	}

}
