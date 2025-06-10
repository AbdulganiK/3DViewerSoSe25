package org.ea;
import org.ea.constant.FilePaths;
import org.ea.model.Polygon;
import org.ea.model.Polyhedron;
import org.ea.model.Triangle;
import org.ea.utiltities.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.BlockingQueue;

public class Main {

    public static void main(String[] args) throws IOException {
        areBothReadersEqual();
    }


    private static void doTask2() throws IOException {
        Polyhedron polyhedron = PolyhedronFactory.buildPolyhedron(
                TriangleFactory.buildTriangles(
                        new STLByteReader(new File(FilePaths.SMALL_STL_BYTE_FILE))
                                .readTriangleData()));

        Triangle[] triangles = polyhedron.getSurfaces();
        Arrays.sort(triangles);
        for (Triangle triangle : triangles) {
            System.out.println(triangle.getArea());
        }
    }

    private static void areBothReadersEqual() throws IOException {
        STLReader stlByteReader = new STLByteReader(new File(FilePaths.SMALL_STL_BYTE_FILE));
        STLReader stlASCIIReader = new STLAsciiReader(new File(FilePaths.SMALL_STL_ASCII_FILE));
        System.out.println(stlByteReader.readTriangleData());
        System.out.println(stlASCIIReader.readTriangleData());

    }
}
