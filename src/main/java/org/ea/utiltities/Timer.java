package org.ea.utiltities;

/**
 * A simple timer utility class for measuring elapsed time in nanoseconds, milliseconds, or seconds.
 *
 * @precondition Timer must be explicitly started using {@link #start()} before measuring.
 * @postcondition Elapsed time is available using {@link #getElapsedMillis()} or {@link #getElapsedSeconds()}.
 */
public class Timer {
    private long startTime = 0;
    private long elapsed = 0;
    private boolean running = false;

    /**
     * Starts the timer. If already running, this method has no effect.
     *
     * @precondition Timer must not already be running
     * @postcondition Timer begins measuring elapsed time from current time
     */
    public void start() {
        if (!running) {
            startTime = System.nanoTime();
            running = true;
        }
    }

    /**
     * Stops the timer and accumulates the elapsed time.
     *
     * @precondition Timer must be running
     * @postcondition Elapsed time is updated and timer stops
     */
    public void stop() {
        if (running) {
            long endTime = System.nanoTime();
            elapsed += endTime - startTime;
            running = false;
        }
    }

    /**
     * Resets the timer to initial state.
     *
     * @precondition None
     * @postcondition Elapsed time is set to 0 and timer is stopped
     */
    public void reset() {
        startTime = 0;
        elapsed = 0;
        running = false;
    }

    /**
     * Returns the elapsed time in milliseconds.
     *
     * @return the elapsed time in ms
     * @precondition Timer may or may not be running
     * @postcondition No change in timer state
     */
    public long getElapsedMillis() {
        if (running) {
            long now = System.nanoTime();
            return (elapsed + (now - startTime)) / 1_000_000;
        }
        return elapsed / 1_000_000;
    }

    /**
     * Returns the elapsed time in seconds.
     *
     * @return the elapsed time in seconds
     * @precondition Timer may or may not be running
     * @postcondition No change in timer state
     */
    public double getElapsedSeconds() {
        return getElapsedMillis() / 1000.0;
    }

    /**
     * Returns whether the timer is currently running.
     *
     * @return true if the timer is running; false otherwise
     * @precondition None
     * @postcondition No change in timer state
     */
    public boolean isRunning() {
        return running;
    }
}