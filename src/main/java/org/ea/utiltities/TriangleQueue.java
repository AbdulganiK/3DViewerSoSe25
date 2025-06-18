package org.ea.utiltities;

import org.ea.model.Triangle; // Je nachdem, wo Triangle definiert ist

import java.util.concurrent.LinkedBlockingQueue;

public class TriangleQueue {
    private static volatile LinkedBlockingQueue<Triangle> instance;

    private TriangleQueue() {
        // privater Konstruktor
    }

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