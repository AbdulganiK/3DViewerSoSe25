package org.ea.utiltities;

import org.ea.constant.Arguments;
import org.ea.constant.Messages;
import org.ea.exceptions.EndOfFileReachedException;
import org.ea.constant.Numbers;
import org.ea.exceptions.NotAStlFileException;
import org.ea.exceptions.OffsetOutOfRangeException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.nio.ByteOrder;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Reads STL files in binary format and provides the triangle data.
 * Implements Runnable for asynchronous reading.
 */
public class STLByteReader extends FileInputStream implements STLReader, Runnable {
    private LinkedBlockingQueue<List<Float>> dataQueue;
    private static final int TRIANGLE_DATA_SIZE = 50;

    /**
     * Constructor that checks if the file is STL format.
     *
     * @param file the STL file
     * @throws FileNotFoundException if file does not exist
     * @throws NotAStlFileException if file is not a valid STL
     *
     * @precondition file != null && file.exists()
     * @postcondition throws NotAStlFileException if invalid STL, else object is constructed
     */
    public STLByteReader(File file) throws FileNotFoundException, NotAStlFileException {
        super(file);
        if (isNotSTLFile(file.getName())) throw new NotAStlFileException();
    }

    /**
     * Constructor with queue for pushing read triangle data.
     *
     * @param file the STL file
     * @param dataQueue queue to push triangle float data lists
     * @throws FileNotFoundException if file does not exist
     * @throws NotAStlFileException if file is not a valid STL
     *
     * @precondition file != null && file.exists() && dataQueue != null
     * @postcondition object is constructed and ready to read into queue
     */
    public STLByteReader(File file, LinkedBlockingQueue<List<Float>> dataQueue) throws FileNotFoundException, NotAStlFileException {
        this(file);
        this.dataQueue = dataQueue;
    }



    /**
     * Reads the header bytes from the STL file.
     *
     * @return list of bytes representing the STL header
     * @throws EndOfFileReachedException if file ends unexpectedly
     * @throws OffsetOutOfRangeException if offset is invalid
     * @throws IOException if IO error occurs
     *
     * @precondition file pointer valid and file readable
     * @postcondition returns list of HEADER_LENGTH bytes from start of file
     */
    @SuppressWarnings("unchecked")
    @Override
    public List<Byte> readHeader() throws EndOfFileReachedException, OffsetOutOfRangeException, IOException {
        byte[] header = this.readByteRange(Numbers.FILE_START, Numbers.HEADER_LENGTH);
        List<Byte> headerList = new ArrayList<>();
        for (byte b : header) {
            headerList.add(b);
        }
        return headerList;
    }

    /**
     * Reads a byte range from the file at given offset and length.
     *
     * @param offset starting byte offset
     * @param length number of bytes to read
     * @return byte array containing the requested bytes
     * @throws OffsetOutOfRangeException if position cannot be set
     * @throws EndOfFileReachedException if file ends before reading length bytes
     * @throws IOException if IO error occurs
     *
     * @precondition offset >= 0 && length > 0
     * @postcondition returns byte array of requested length or throws exception
     */
    private byte[] readByteRange(long offset, int length) throws OffsetOutOfRangeException, EndOfFileReachedException, IOException {
        try {
            this.getChannel().position(offset);
        } catch (IOException e) {
            throw new OffsetOutOfRangeException();
        }
        byte[] buffer = new byte[length];
        int bytesRead = 0;
        while (bytesRead < length) {
            int read = this.read(buffer, bytesRead, length - bytesRead);
            if (read == -1) {
                throw new EndOfFileReachedException();
            }
            bytesRead += read;
        }
        return buffer;
    }

    /**
     * Reads the amount of triangles described in the STL file.
     *
     * @return number of triangles
     * @throws EndOfFileReachedException if file ends unexpectedly
     * @throws OffsetOutOfRangeException if offset invalid
     * @throws IOException if IO error occurs
     *
     * @precondition STL header has been read successfully
     * @postcondition returns a non-negative integer representing triangle count
     */
    public int readAmountOfTriangles() throws EndOfFileReachedException, OffsetOutOfRangeException, IOException {
        byte[] triangleAmountData = this.readByteRange(Numbers.HEADER_LENGTH, Numbers.TRIANGLE_AMOUNT_LENGTH);
        ByteBuffer buffer = ByteBuffer.wrap(triangleAmountData).order(ByteOrder.LITTLE_ENDIAN);
        return buffer.getInt();
    }

    /**
     * Reads all triangle data as floats from the STL file.
     * Adds each triangle's float list to the queue if dataQueue is set.
     *
     * @return list of all floats for all triangles
     * @throws IOException if IO error occurs
     * @throws EndOfFileReachedException if file ends unexpectedly
     * @throws OffsetOutOfRangeException if offset invalid
     *
     * @precondition file pointer is at start of triangle data
     * @postcondition all triangle float data read and optionally pushed to queue
     */
    @SuppressWarnings("unchecked")
    @Override
    public List<Float> readTriangleData() throws IOException, EndOfFileReachedException, OffsetOutOfRangeException {
        Timer timer = new Timer();
        timer.start();
        int triangleAmount = readAmountOfTriangles();
        byte[] triangleData = readByteRange(
                Numbers.HEADER_LENGTH + Numbers.TRIANGLE_AMOUNT_LENGTH,
                triangleAmount * TRIANGLE_DATA_SIZE
        );

        ByteBuffer buffer = ByteBuffer.wrap(triangleData)
                .order(ByteOrder.LITTLE_ENDIAN);

        List<Float> allTriangles = new ArrayList<>(triangleAmount * 12); // Gesamtliste aller Dreiecke
        for (int i = 0; i < triangleAmount; i++) {
            // Neue temporäre Liste für das aktuelle Dreieck
            List<Float> currentTriangle = new ArrayList<>(12); // 12 Floats pro Dreieck

            // Lese 12 Floats (3 Normale + 9 Eckpunkte)
            for (int j = 0; j < 12; j++) {
                currentTriangle.add(buffer.getFloat());
            }

            // Füge das Dreieck zur Gesamtliste hinzu
            allTriangles.addAll(currentTriangle);

            // Füge das aktuelle Dreieck separat in die Queue ein
            if (this.dataQueue != null) {
                dataQueue.add(new ArrayList<>(currentTriangle)); // Neue Kopie für die Queue
            }

            // Überspringe 2 Attribut-Bytes
            buffer.position(buffer.position() + 2);
        }
        Logger.info(Messages.SUCCESSFUL_READ);
        Logger.info(String.format(Messages.FLOAT_COUNT_MESSAGE, allTriangles.size()));
        timer.stop();
        Logger.info(String.format(Messages.READ_TIME_MESSAGE, (double) timer.getElapsedMillis()));

        return allTriangles;
    }

    /**
     * Runnable method to read triangles and push them to queue.
     * Adds a terminating "poison pill" with a list containing null at the end.
     *
     * @precondition dataQueue != null
     * @postcondition all triangle data read and poison pill added to queue
     */
    @Override
    public void run() {
        try {
            this.readTriangleData();
        } catch (IOException | EndOfFileReachedException | OffsetOutOfRangeException e) {
            Logger.error(e.getMessage());
            System.exit(Arguments.EXIT_ERROR);
        }
        List<Float> end = new ArrayList<>();
        end.add(null);
        this.dataQueue.add(end);
    }
}
