package org.ea.utiltities;

import org.ea.constant.ExceptionMessages;

import java.io.*;
import java.util.List;

public class STLAsciiReader extends BufferedReader implements STLReader {
    public STLAsciiReader(File file) throws FileNotFoundException {
        super(new FileReader(file));
        if (!isSTLFile(file.getName())) throw new RuntimeException(ExceptionMessages.notASTLFile);
    }

    @Override
    public <T> List<T> readHeader() {
        return List.of();
    }

    @Override
    public <T> List<T> readTriangles() {
        return List.of();
    }
}
