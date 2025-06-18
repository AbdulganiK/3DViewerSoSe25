package org.ea.utiltities;

import org.ea.constant.Arguments;

public class ManagedThread<T extends Runnable> {
    private final T target;
    private final Thread thread;

    public ManagedThread(T target) {
        this.target = target;
        this.thread = new Thread(target); // klappt, weil T mindestens Runnable ist
    }

    public T getTarget() {
        return target;
    }

    public void start() {
        thread.start();
    }

    public void join() {
        try {
            thread.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean isAlive() {
        return thread.isAlive();
    }
}