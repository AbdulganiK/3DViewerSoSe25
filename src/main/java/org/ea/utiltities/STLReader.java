package org.ea.utiltities;

import java.util.ArrayList;
import java.util.List;

public interface STLReader {
    public <T> List<T> readHeader();
    public <T> List<T> readTriangles();
}
