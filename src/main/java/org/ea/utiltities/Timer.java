package org.ea.utiltities;

public class Timer {
    private long startTime = 0;
    private long elapsed = 0;
    private boolean running = false;

    // Startet den Timer (erneut, wenn schon mal gestoppt wurde)
    public void start() {
        if (!running) {
            startTime = System.nanoTime();
            running = true;
        }
    }

    // Stoppt den Timer und speichert die Zeit
    public void stop() {
        if (running) {
            long endTime = System.nanoTime();
            elapsed += endTime - startTime;
            running = false;
        }
    }

    // Setzt den Timer zurück
    public void reset() {
        startTime = 0;
        elapsed = 0;
        running = false;
    }

    // Gibt die vergangene Zeit in Millisekunden zurück
    public long getElapsedMillis() {
        if (running) {
            long now = System.nanoTime();
            return (elapsed + (now - startTime)) / 1_000_000;
        }
        return elapsed / 1_000_000;
    }

    // Gibt die vergangene Zeit in Sekunden zurück (als double)
    public double getElapsedSeconds() {
        return getElapsedMillis() / 1000.0;
    }

    // Gibt an, ob der Timer gerade läuft
    public boolean isRunning() {
        return running;
    }
}

