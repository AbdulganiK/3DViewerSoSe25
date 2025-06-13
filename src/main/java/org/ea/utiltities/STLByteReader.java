package org.ea.utiltities;

import org.ea.exceptions.EndOfFileReachedException;
import org.ea.exceptions.ExceptionMessages;
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

public class STLByteReader extends FileInputStream implements STLReader, Runnable {
    private LinkedBlockingQueue<List<Float>> dataQueue;
    private static final int TRIANGLE_DATA_SIZE = 50;

    public STLByteReader(File file) throws FileNotFoundException, NotAStlFileException {
        super(file);
        if (isNotSTLFile(file.getName())) throw new NotAStlFileException();
    }

    public STLByteReader(File file, LinkedBlockingQueue<List<Float>> dataQueue) throws FileNotFoundException, NotAStlFileException {
        this(file);
        this.dataQueue = dataQueue;
    }

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


    public int readAmountOfTriangles() throws EndOfFileReachedException, OffsetOutOfRangeException, IOException {
        byte[] triangleAmountData = this.readByteRange(Numbers.HEADER_LENGTH, Numbers.TRIANGLE_AMOUNT_LENGTH);
        ByteBuffer buffer = ByteBuffer.wrap(triangleAmountData).order(ByteOrder.LITTLE_ENDIAN);
        return buffer.getInt();
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Float> readTriangleData() throws IOException, EndOfFileReachedException, OffsetOutOfRangeException {
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
        return allTriangles;
    }

    @Override
    public void run() {
        try {
            this.readTriangleData();
        } catch (IOException | EndOfFileReachedException | OffsetOutOfRangeException e) {
            System.out.println(e.getMessage());
            System.exit(-1);
        }
        List<Float> end = new ArrayList<>();
        end.add(null);
        this.dataQueue.add(end);
    }
}
