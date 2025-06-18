package org.ea.utiltities;

import org.ea.constant.Messages;
import org.ea.exceptions.ExceptionMessages;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Reader for ASCII STL files.
 * Implements Runnable to read triangle data asynchronously.
 */
public class STLAsciiReader extends BufferedReader implements STLReader {
    private BlockingQueue<List<Float>> dataQueue;

    /**
     * Constructor that validates the file extension.
     *
     * @param file the ASCII STL file to read
     * @throws FileNotFoundException if file not found
     * @throws RuntimeException if file is not STL
     *
     * @precondition file != null && file.exists()
     * @postcondition object constructed or exception thrown
     */
    public STLAsciiReader(File file) throws FileNotFoundException {
        super(new FileReader(file));
        if (isNotSTLFile(file.getName())) throw new RuntimeException(ExceptionMessages.NOT_A_STL_FILE);
    }

    /**
     * Constructor with a data queue for asynchronously passing triangle data.
     *
     * @param file the ASCII STL file to read
     * @param dataQueue queue to push triangle float data lists
     * @throws FileNotFoundException if file not found
     *
     * @precondition file != null && file.exists() && dataQueue != null
     * @postcondition object constructed and ready to read
     */
    public STLAsciiReader(File file, BlockingQueue<List<Float>> dataQueue) throws FileNotFoundException {
        this(file);
        this.dataQueue = dataQueue;
    }

    /**
     * Reads the header line of the ASCII STL file and splits it by whitespace.
     *
     * @return list of strings in the header line
     * @throws IOException if reading fails
     *
     * @precondition file pointer at start
     * @postcondition returns list of header tokens from first line
     */
    @SuppressWarnings("unchecked")
    @Override
    public List<String> readHeader() throws IOException {
        return Arrays.stream(this.readLine().toLowerCase().split(" ")).toList();
    }

    /**
     * Reads all triangle data as floats from the ASCII STL file.
     * Parses lines starting with "vertex" or "facet normal" and extracts floats.
     * Adds each triangle's float list to the queue if dataQueue is set.
     *
     * @return list of all floats for all triangles
     *
     * @precondition file readable and formatted as ASCII STL
     * @postcondition all triangle data read and optionally pushed to queue
     */
    @SuppressWarnings("unchecked")
    @Override
    public List<Float> readTriangleData() {
        Timer timer = new Timer();
        List<Float> allFloats = new ArrayList<>();
        List<String> lines = this.lines().toList();

        List<Float> currentTriangle = new ArrayList<>(12); // 12 Floats pro Dreieck
        final int FLOATS_PER_TRIANGLE = 12;

        for (String line : lines) {
            line = line.trim().toLowerCase();
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
        Logger.info(Messages.SUCCESSFUL_READ);
        Logger.info(String.format(Messages.FLOAT_COUNT_MESSAGE, allFloats.size()));
        timer.stop();
        Logger.info(String.format(Messages.READ_TIME_MESSAGE, (double) timer.getElapsedMillis()));
        return allFloats;
    }

    /**
     * Runnable implementation: calls readTriangleData().
     *
     * @precondition dataQueue may be set to pass data asynchronously
     * @postcondition reads all triangle data, optionally pushing to queue
     */
    @Override
    public void run() {
        this.readTriangleData();
        List<Float> end = new ArrayList<>();
        end.add(null);
        this.dataQueue.add(end);
    }
}
