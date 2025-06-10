package org.ea.utiltities;

import org.ea.constant.FileExtensions;
import org.ea.exceptions.EndOfFileReachedException;
import org.ea.exceptions.OffsetOutOfRangeException;

import java.io.IOException;
import java.util.List;

public interface STLReader {
    public <T> List<T> readHeader() throws IOException, EndOfFileReachedException, OffsetOutOfRangeException;
    public <T> List<T> readTriangleData() throws IOException, EndOfFileReachedException, OffsetOutOfRangeException;
    default boolean isNotSTLFile(String fileName) {
        return !fileName.endsWith(FileExtensions.STL);
    }
}
