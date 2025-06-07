package org.ea.utiltities;

import java.io.BufferedReader;
import java.io.Reader;
import java.util.List;

public class STLAsciiReader extends BufferedReader implements STLReader {
    public STLAsciiReader(Reader in) {
        super(in);
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
