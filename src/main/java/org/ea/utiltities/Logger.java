package org.ea.utiltities;

import org.ea.model.Triangle;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.Iterator;

public class Logger {
    // ANSI color codes
    private static final String RESET = "\u001B[0m";
    private static final String GREEN = "\u001B[32m";
    private static final String YELLOW = "\u001B[33m";
    private static final String RED = "\u001B[31m";

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    // === Info ===
    public static void info(String message) {
        log("INFO", message, null, GREEN);
    }

    public static void info(String message, Number value) {
        log("INFO", message, value, GREEN);
    }

    // === Warning ===
    public static void warning(String message) {
        log("WARNING", message, null, YELLOW);
    }

    public static void warning(String message, Number value) {
        log("WARNING", message, value, YELLOW);
    }

    // === Error ===
    public static void error(String message) {
        log("ERROR", message, null, RED);
    }

    public static void error(String message, Number value) {
        log("ERROR", message, value, RED);
    }

    // === Interner Log-Handler ===
    private static void log(String level, String message, Number value, String color) {
        String timestamp = LocalDateTime.now().format(formatter);
        StringBuilder sb = new StringBuilder();
        sb.append(color)
                .append("[")
                .append(timestamp)
                .append("] [")
                .append(level)
                .append("] ")
                .append(message);

        if (value != null) {
            sb.append(" (").append(value).append(")");
        }

        sb.append(RESET);
        System.out.println(sb.toString());
    }

    /**
     * Logs the first five elements of the given array as an info message.
     * If the array has fewer than five elements, logs all of them.
     * @param array the array to log the first five elements of
     * @param <T> type of elements in the array
     * @precondition array != null
     * @postcondition logged message contains at most 5 elements
     */
    public static <T> void logFirstFiveElements(T[] array) {
        if (array == null) {
            info("Array is null.");
            return;
        }

        StringBuilder sb = new StringBuilder("First five elements: [");
        int limit = Math.min(array.length, 5);
        for (int i = 0; i < limit; i++) {
            if (i > 0) sb.append(", ");
            sb.append(array[i]);
        }
        sb.append("]");
        info(sb.toString());
    }
}
