package org.ea;
import org.ea.model.Triangle;
import org.ea.utiltities.STLAsciiReader;
import org.ea.utiltities.STLByteReader;
import org.ea.utiltities.STLReader;
import org.ea.utiltities.TriangleFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) throws IOException {
        STLReader stlReader = new STLAsciiReader(new File("src/main/resources/viewerascii.stl"));
        List<Float> triangleData = stlReader.readTriangleData();
        List<Triangle> triangles = TriangleFactory.buildTriangles(triangleData);
    }


    private void doTask2() {

    }
}
