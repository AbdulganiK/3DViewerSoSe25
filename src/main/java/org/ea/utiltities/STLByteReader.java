package org.ea.utiltities;

import org.ea.constant.ExceptionMessages;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class STLByteReader extends FileInputStream implements STLReader {
    public STLByteReader(File file) throws FileNotFoundException {
        super(file);
        if (isNotSTLFile(file.getName())) throw new RuntimeException(ExceptionMessages.notASTLFile);
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Byte> readHeader() throws IOException {
        byte[] header = this.readByteRange(0, 80);
        List<Byte> headerList = new ArrayList<>();
        for (byte b : header) {
            headerList.add(b);
        }
        return headerList ;
    }

    public byte[] readByteRange(long offset, int length) throws IOException {
        // Vorspulen (skip) bis Offset
        long toSkip = offset;
        while (toSkip > 0) {
            long skipped = this.skip(toSkip);
            if (skipped <= 0) {
                throw new IOException("Konnte nicht weiter überspringen");
            }
            toSkip -= skipped;
        }

        byte[] buffer = new byte[length];
        int bytesRead = 0;
        while (bytesRead < length) {
            int read = this.read(buffer, bytesRead, length - bytesRead);
            if (read == -1) {
                throw new IOException("Ende des Streams erreicht bevor alle Bytes gelesen wurden");
            }
            bytesRead += read;
        }
        return buffer;
    }


    public int readAmountOfTriangles() throws IOException {
        byte[] triangleAmountData = this.readByteRange(80, 4);
        ByteBuffer buffer = ByteBuffer.wrap(triangleAmountData).order(ByteOrder.LITTLE_ENDIAN);
        return buffer.getInt();
    }

    @Override
    public <T> List<T> readTriangles() {
        return List.of();
    }
}
