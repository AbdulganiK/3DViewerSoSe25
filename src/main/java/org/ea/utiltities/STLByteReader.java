package org.ea.utiltities;

import org.ea.constant.ExceptionMessages;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class STLByteReader extends FileInputStream implements STLReader {
    public STLByteReader(File file) throws FileNotFoundException {
        super(file);
        if (!isSTLFile(file.getName())) throw new RuntimeException(ExceptionMessages.notASTLFile);
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Byte> readHeader() throws IOException {
        byte[] header = new byte[40];
        int bytesRead = this.read(header); // liest die Bytes

        if (bytesRead != 40) {
            throw new IOException("Header zu kurz oder unvollständig gelesen");
        }

        List<Byte> headerList = new ArrayList<>();
        for (byte b : header) {
            headerList.add(b);
        }

        return headerList;
    }

    public int readAmountOfTriangles() {
        return 0;
    }

    @Override
    public <T> List<T> readTriangles() {
        return List.of();
    }
}
