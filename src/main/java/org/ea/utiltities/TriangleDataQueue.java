package org.ea.utiltities;

import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

public class TriangleDataQueue {
    private static volatile LinkedBlockingQueue<List<Float>> instance;

    private TriangleDataQueue() {
        // privater Konstruktor
    }

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