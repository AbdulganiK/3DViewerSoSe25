package org.ea.utiltities;

import java.io.*;
import java.util.ArrayList;

public class STLByteReader extends FileInputStream {
    private String name;
    private File file;
    private FileDescriptor fileDescriptor;
    public STLByteReader(String name) throws FileNotFoundException {
        super(name);
        this.name = name;
    }

    public STLByteReader(File file) throws FileNotFoundException {
        super(file);
        this.file = file;
    }

    public STLByteReader(FileDescriptor fdObj) {
        super(fdObj);
        this.fileDescriptor = fdObj;
    }

    public ArrayList<Byte> readHeader() throws IOException {
        ArrayList<Byte> header = new ArrayList<>();
        while (this.read() != -1){
            //todo
        }
        return header;
    }
}
