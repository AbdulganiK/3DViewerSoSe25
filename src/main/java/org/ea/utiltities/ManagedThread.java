package org.ea.utiltities;

import org.ea.constant.Arguments;

/**
 * A wrapper class that manages the lifecycle of a thread based on a Runnable target.
 *
 * @param <T> the type of the Runnable target
 * @precondition T must implement {@link Runnable}
 * @postcondition Provides thread control over the given runnable target
 */
public class ManagedThread<T extends Runnable> {

    private final T target;
    private final Thread thread;

    /**
     * Constructs a managed thread with the given target.
     *
     * @param target the runnable target
     * @precondition {@code target} must not be null
     * @postcondition The thread is initialized but not started
     */
    public ManagedThread(T target) {
        this.target = target;
        this.thread = new Thread(target);
    }

    /**
     * Returns the runnable target associated with this thread.
     *
     * @return the target object
     * @precondition None
     * @postcondition The target used to construct the thread is returned
     */
    public T getTarget() {
        return target;
    }

    /**
     * Starts the underlying thread.
     *
     * @precondition Thread must not already be started
     * @postcondition Target starts running in a new thread
     */
    public void start() {
        thread.start();
    }

    /**
     * Waits for the thread to complete execution.
     *
     * @precondition Thread must have been started
     * @postcondition Current thread is blocked until target thread finishes
     */
    public void join() {
        try {
            thread.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Checks whether the thread is still running.
     *
     * @return true if the thread is alive, false otherwise
     * @precondition None
     * @postcondition Reflects the current thread state
     */
    public boolean isAlive() {
        return thread.isAlive();
    }
}
