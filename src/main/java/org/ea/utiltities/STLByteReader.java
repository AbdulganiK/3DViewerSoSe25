package org.ea.utiltities;

import org.ea.constant.ExceptionMessages;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;

public class STLByteReader extends FileInputStream implements STLReader {
    public STLByteReader(File file) throws FileNotFoundException {
        super(file);
        if (!isSTLFile(file.getName())) throw new RuntimeException(ExceptionMessages.notASTLFile);
    }

    @Override
    public <T> List<T> readHeader() {
        return List.of();
    }

    public int readAmountOfTriangles() {
        return 0;
    }

    @Override
    public <T> List<T> readTriangles() {
        return List.of();
    }
}
