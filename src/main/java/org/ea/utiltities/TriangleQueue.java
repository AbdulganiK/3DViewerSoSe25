package org.ea.utiltities;

import org.ea.model.Triangle;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Singleton wrapper for a thread-safe queue of {@link Triangle} objects.
 * Used to share constructed triangles between producer and consumer threads.
 *
 * @precondition None
 * @postcondition Singleton queue instance is available for shared access
 */
public class TriangleQueue {

    private static volatile LinkedBlockingQueue<Triangle> instance;

    /**
     * Private constructor to prevent external instantiation.
     *
     * @precondition None
     * @postcondition Class cannot be instantiated externally
     */
    private TriangleQueue() {
        // private constructor
    }

    /**
     * Returns the singleton instance of the triangle queue.
     *
     * @return a thread-safe {@link LinkedBlockingQueue} of {@link Triangle} objects
     * @precondition None
     * @postcondition Queue instance is initialized if not already present
     */
    public static LinkedBlockingQueue<Triangle> getInstance() {
        if (instance == null) {
            synchronized (TriangleQueue.class) {
                if (instance == null) {
                    instance = new LinkedBlockingQueue<>();
                }
            }
        }
        return instance;
    }
}
