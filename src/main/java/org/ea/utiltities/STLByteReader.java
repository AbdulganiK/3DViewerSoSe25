package org.ea.utiltities;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;

public class STLByteReader extends FileInputStream implements STLReader {
    public STLByteReader(String name) throws FileNotFoundException {
        super(name);
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
