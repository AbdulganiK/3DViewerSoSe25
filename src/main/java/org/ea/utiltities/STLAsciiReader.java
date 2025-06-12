package org.ea.utiltities;

import org.ea.exceptions.ExceptionMessages;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

public class STLAsciiReader extends BufferedReader implements STLReader, Runnable {
    private LinkedBlockingQueue<List<Float>> dataQueue;

    public STLAsciiReader(File file) throws FileNotFoundException {
        super(new FileReader(file));
        if (isNotSTLFile(file.getName())) throw new RuntimeException(ExceptionMessages.NOT_A_STL_FILE);
    }

    public STLAsciiReader(File file, LinkedBlockingQueue<List<Float>> dataQueue) throws FileNotFoundException {
        this(file);
        this.dataQueue = dataQueue;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<String> readHeader() throws IOException {
        return Arrays.stream(this.readLine().split(" ")).toList();
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Float> readTriangleData() {
        List<Float> allFloats = new ArrayList<>();
        List<String> lines = this.lines().toList();

        List<Float> currentTriangle = new ArrayList<>(12); // 12 Floats pro Dreieck
        final int FLOATS_PER_TRIANGLE = 12;

        for (String line : lines) {
            line = line.trim();
            if (line.startsWith("vertex") || line.startsWith("facet normal")) {
                String[] parts = line.split("\\s+");
                for (int i = 1; i < parts.length; i++) {
                    try {
                        float value = Float.parseFloat(parts[i]);
                        currentTriangle.add(value);

                        // Wenn genug Werte für ein Dreieck gesammelt wurden
                        if (currentTriangle.size() == FLOATS_PER_TRIANGLE) {
                            allFloats.addAll(currentTriangle);
                            if (this.dataQueue != null) {
                                dataQueue.add(new ArrayList<>(currentTriangle));
                            }
                            currentTriangle.clear();
                        }
                    } catch (NumberFormatException e) {
                        // Ignoriere ungültige Einträge
                    }
                }
            }
        }

        return allFloats;
    }

    @Override
    public void run() {
        this.readTriangleData();
    }
}
