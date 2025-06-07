package org.ea.utiltities;

import org.ea.constant.ExceptionMessages;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class STLAsciiReader extends BufferedReader implements STLReader {
    public STLAsciiReader(File file) throws FileNotFoundException {
        super(new FileReader(file));
        if (!isSTLFile(file.getName())) throw new RuntimeException(ExceptionMessages.notASTLFile);
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<String> readHeader() throws IOException {
        return Arrays.stream(this.readLine().split(" ")).toList();
    }

    @Override
    public <T> List<T> readTriangles() {
        return List.of();
    }
}
