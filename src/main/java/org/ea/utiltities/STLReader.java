package org.ea.utiltities;

import org.ea.constant.FileExtensions;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public interface STLReader {
    public <T> List<T> readHeader() throws IOException;
    public <T> List<T> readTriangles();
    default boolean isSTLFile(String fileName) {
        return fileName.endsWith(FileExtensions.STL);
    }
}
