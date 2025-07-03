package org.ea.utiltities;

import org.ea.model.Triangle;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.Iterator;

/**
 * A utility logger class that outputs color-coded messages with timestamps.
 *
 * @precondition System.out must be available for printing messages.
 * @postcondition Log messages are displayed with proper formatting and coloring.
 */
public class Logger {

    private static final String RESET = "\u001B[0m";
    private static final String GREEN = "\u001B[32m";
    private static final String YELLOW = "\u001B[33m";
    private static final String RED = "\u001B[31m";

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /**
     * Logs an informational message.
     *
     * @param message the message to log
     * @precondition message is not null
     * @postcondition Message is printed with INFO level and green color
     */
    public static void info(String message) {
        log("INFO", message, null, GREEN);
    }

    /**
     * Logs an informational message with a numeric value.
     *
     * @param message the message text
     * @param value a numeric value to include in the log
     * @precondition message is not null
     * @postcondition Message with value is printed in green
     */
    public static void info(String message, Number value) {
        log("INFO", message, value, GREEN);
    }

    /**
     * Logs a warning message.
     *
     * @param message the warning message
     * @precondition message is not null
     * @postcondition Message is printed with WARNING level and yellow color
     */
    public static void warning(String message) {
        log("WARNING", message, null, YELLOW);
    }

    /**
     * Logs a warning message with a numeric value.
     *
     * @param message the warning text
     * @param value a numeric value to include
     * @precondition message is not null
     * @postcondition Message with value is printed in yellow
     */
    public static void warning(String message, Number value) {
        log("WARNING", message, value, YELLOW);
    }

    /**
     * Logs an error message.
     *
     * @param message the error message
     * @precondition message is not null
     * @postcondition Message is printed with ERROR level and red color
     */
    public static void error(String message) {
        log("ERROR", message, null, RED);
    }

    /**
     * Logs an error message with a numeric value.
     *
     * @param message the error text
     * @param value a numeric value to include
     * @precondition message is not null
     * @postcondition Message with value is printed in red
     */
    public static void error(String message, Number value) {
        log("ERROR", message, value, RED);
    }

    /**
     * Internal method to format and print a log message.
     *
     * @param level log level (INFO, WARNING, ERROR)
     * @param message the message content
     * @param value optional numeric value
     * @param color ANSI color code
     * @precondition message is not null
     * @postcondition Formatted message is printed to console
     */
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
     *
     * @param array the array to log the first five elements of
     * @param <T> type of elements in the array
     * @precondition array != null
     * @postcondition Logged message contains at most 5 elements from the array
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
