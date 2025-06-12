package org.ea.utiltities;

import org.ea.exceptions.ExceptionMessages;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class STLAsciiReader extends BufferedReader implements STLReader, Runnable {
    public STLAsciiReader(File file) throws FileNotFoundException {
        super(new FileReader(file));
        if (isNotSTLFile(file.getName())) throw new RuntimeException(ExceptionMessages.NOT_A_STL_FILE);
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<String> readHeader() throws IOException {
        return Arrays.stream(this.readLine().split(" ")).toList();
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Float> readTriangleData() {
        List<Float> floats = new ArrayList<>();
        List<String> lines = this.lines().toList();
        for (String line : lines) {
            line = line.trim();
            if (line.startsWith("vertex") || line.startsWith("facet normal")) {
                String[] parts = line.split("\\s+");
                for (int i = 1; i < parts.length; i++) {
                    try {
                        floats.add(Float.parseFloat(parts[i]));
                    } catch (NumberFormatException e) {
                        // Ignoriere ungültige Einträge
                    }
                }
            }
        }

        return floats;
    }

    @Override
    public void run() {

    }
}
