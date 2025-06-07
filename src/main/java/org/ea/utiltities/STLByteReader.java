package org.ea.utiltities;

import org.ea.constant.ExceptionMessages;
import org.ea.constant.Numbers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.nio.ByteOrder;

public class STLByteReader extends FileInputStream implements STLReader {
    public STLByteReader(File file) throws FileNotFoundException {
        super(file);
        if (isNotSTLFile(file.getName())) throw new RuntimeException(ExceptionMessages.notASTLFile);
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Byte> readHeader() throws IOException {
        byte[] header = this.readByteRange(Numbers.FILE_START, Numbers.HEADER_LENGTH);
        List<Byte> headerList = new ArrayList<>();
        for (byte b : header) {
            headerList.add(b);
        }
        return headerList ;
    }

    private byte[] readByteRange(long offset, int length) throws IOException {
        this.getChannel().position(offset); // Direktes Positionieren
        byte[] buffer = new byte[length];
        int bytesRead = 0;
        while (bytesRead < length) {
            int read = this.read(buffer, bytesRead, length - bytesRead);
            if (read == -1) {
                throw new IOException("Dateiende erreicht, bevor alle Bytes gelesen wurden");
            }
            bytesRead += read;
        }
        return buffer;
    }


    public int readAmountOfTriangles() throws IOException {
        byte[] triangleAmountData = this.readByteRange(Numbers.HEADER_LENGTH, Numbers.TRIANGLE_AMOUNT_LENGTH);
        ByteBuffer buffer = ByteBuffer.wrap(triangleAmountData).order(ByteOrder.LITTLE_ENDIAN);
        return buffer.getInt();
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Float> readTriangles() throws IOException {
        int triangleAmount = readAmountOfTriangles();
        byte[] triangleData = readByteRange(Numbers.HEADER_LENGTH + Numbers.TRIANGLE_AMOUNT_LENGTH, triangleAmount * 4 * 12);
        ByteBuffer buffer = ByteBuffer.wrap(triangleData).order(ByteOrder.LITTLE_ENDIAN);
        ArrayList<Float> floatList = new ArrayList<>();
        while (buffer.remaining() >= Float.BYTES) {
            floatList.add(buffer.getFloat());
        }
        return floatList;
    }
}
