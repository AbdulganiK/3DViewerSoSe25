package org.ea;
import org.ea.utiltities.STLAsciiReader;
import org.ea.utiltities.STLReader;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {
        STLReader stlReader = new STLAsciiReader(new File("src/main/resources/3dviewertest.stl"));
    }
}
