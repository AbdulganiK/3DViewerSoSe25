package org.ea.utiltities;

import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Singleton wrapper for a thread-safe triangle data queue.
 * Used to share triangle float data across different components.
 *
 * @precondition None
 * @postcondition Singleton instance can be accessed via {@link #getInstance()}
 */
public class TriangleDataQueue {

    private static volatile LinkedBlockingQueue<List<Float>> instance;

    /**
     * Private constructor to prevent instantiation.
     *
     * @precondition None
     * @postcondition Class cannot be instantiated from outside
     */
    private TriangleDataQueue() {
        // private constructor
    }

    /**
     * Returns the singleton instance of the triangle data queue.
     *
     * @return a thread-safe LinkedBlockingQueue for triangle data
     * @precondition None
     * @postcondition Singleton queue instance is returned
     */
    public static LinkedBlockingQueue<List<Float>> getInstance() {
        if (instance == null) {
            synchronized (TriangleDataQueue.class) {
                if (instance == null) {
                    instance = new LinkedBlockingQueue<>();
                }
            }
        }
        return instance;
    }
}